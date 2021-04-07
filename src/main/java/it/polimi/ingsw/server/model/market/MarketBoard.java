package it.polimi.ingsw.server.model.market;

import com.google.gson.Gson;
import it.polimi.ingsw.server.model.Resource;
import javafx.util.Pair;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import java.util.concurrent.ThreadLocalRandom;
import it.polimi.ingsw.server.model.player.*;

/**
 * Matrix data structure to store {@link Marble MarketMarbles} representing {@link Resource Resources}
 * available in the MarketBoard.
 */
public class MarketBoard {

    /**
     * <p>Represents the actual structure of the (rows x columns) marble matrix
     */
    private Marble[][] marbleMatrix;

    /**
     * {@link MarketBoard#marbleMatrix} number of rows.
     */
    private int rows;

    /**
     * {@link MarketBoard#marbleMatrix} number of columns.
     */
    private int columns;

    /**
     * Extra {@link Marble} to place on the top right corner of MarketBoard slide.
     * Changes value everytime {@link MarketBoard#updateMatrix} is invoked.
     */
    Marble slideMarble;

    /**
     * Each row and column of the {@link MarketBoard#marbleMatrix} has a corresponding {@link MarketLine}.
     * A <em>MarketLine</em> is needed to pick an entire row or column during {@link State#CHOOSING_RESOURCE_FOR_PRODUCTION}
     * turn phase.
     */
    private MarketLine Market;

    public static MarketBoard initializeMarketBoard(String configFilePath) {
        Gson gson = new Gson();
        MarketBoard test = null;
        try {

        String MarketBoardClassConfig = Files.readString(Path.of(configFilePath), StandardCharsets.US_ASCII);
        test = gson.fromJson(MarketBoardClassConfig, MarketBoard.class);
        List<Marble> marbles  = Arrays
                .stream(test.marbleMatrix)
                .flatMap(Arrays::stream).collect(Collectors.toList());

        Collections.shuffle(marbles);

        final int rows = test.rows;

        test.marbleMatrix = IntStream.range(0,test.columns * test.rows)
                .mapToObj((pos)->new Pair<>(pos,marbles.get(pos)))
                .collect(Collectors.groupingBy((e)->e.getKey()% rows))
                .values()
                .stream()
                .map(MarketBoard::pairToValue)
                    .toArray(Marble[][]::new);

        int randomRowPos = ThreadLocalRandom.current().nextInt(0, test.rows);
        int randomColumnPos = ThreadLocalRandom.current().nextInt(0, test.columns);
        Marble temporaryMarble = test.marbleMatrix[randomRowPos][randomColumnPos];
        test.marbleMatrix[randomRowPos][randomColumnPos] = test.slideMarble;
        test.slideMarble = temporaryMarble;


        } catch (IOException e) {
            System.out.println("Error while class initialization with json config file at path: " + configFilePath);
            e.printStackTrace();
        }

        return test;
    }

    private static Marble[] pairToValue(List<Pair<Integer, Marble>> pos_marArray){
        return pos_marArray.stream().map(Pair::getValue).toArray(Marble[]::new);
    }

    public static MarketBoard initializeMarketBoard(){
        File file = new File("src/main/resources/config/MarketBoardConfig.json");
        return initializeMarketBoard(file.getAbsolutePath());
    }

    /**
     * Gets a whole {@link MarketBoard#marbleMatrix} row or column as an array of {@link Marble Marbles}.
     *
     * @param line {@link MarketLine} corresponding to the row or column to get.
     *
     * @return array of {@link Marble Marbles} from the line passed as a parameter.
     */
    public Marble[] pickResources(MarketLine line){
        int lineNumber = line.getLineNumber();
        return (lineNumber < rows) ? marbleMatrix[lineNumber] : getColumn(lineNumber);
    }

    /**
     * <p>Invoked after {@link MarketBoard#pickResources} method execution, to update {@link MarketBoard#marbleMatrix}
     * structure by inserting the {@link MarketBoard#slideMarble} in the chosen line.<br>
     * If the chosen line is a row, Marble in first position becomes the next <em>slideMarble</em> and the current <em>slideMarble</em>
     * is inserted in the last position of the line, after Marbles shifting one position to
     * the left.<br>
     * If the chosen line is a column, Marble in first position becomes the next <em>slideMarble</em> and the current <em>slideMarble</em>
     * is inserted in the last position of the column, after Marbles shifting one position down.
     *
     * @param line {@link MarketLine} corresponding to the row or column to update.
     */
    public void updateMatrix(MarketLine line) {

        int lineNumber = line.getLineNumber();
        if (lineNumber < rows) {
                Marble nextExternalMarble = marbleMatrix[lineNumber][0];
                System.arraycopy(marbleMatrix[lineNumber], 1, marbleMatrix[lineNumber], 0, columns - 1);
                marbleMatrix[lineNumber][columns-1] = slideMarble;
                slideMarble = nextExternalMarble;
        }
        else {
            Marble nextExternalMarble = marbleMatrix[rows-1][lineNumber];
            for(int i=1; i<rows; i++)
                marbleMatrix[i][lineNumber] = marbleMatrix[i-1][lineNumber];

            marbleMatrix[0][lineNumber] = slideMarble;
            slideMarble = nextExternalMarble;
        }

    }

    private Marble[] getColumn(int column) {
        return pairToValue(
                    IntStream.range(0, rows)
                    .mapToObj(i -> new Pair<>(i, marbleMatrix[i][column-rows]))
                    .collect(Collectors.toList())
        );
    }


    /* public static void main(String[] args) {
        MarketBoard resourcesMarket = initializeMarketBoard("/Users/pablo/IdeaProjects/ing-sw-2021-laconca-lodari-giaccaglia/target/classes/config/MarketBoardConfig.json");
        System.out.println(Arrays.deepToString(resourcesMarket.marbleMatrix));
        System.out.println(Arrays.toString(resourcesMarket.pickResources(MarketLine.FIRST_COLUMN)));
        resourcesMarket.updateMatrix(MarketLine.FIRST_ROW);
        System.out.println(Arrays.deepToString(resourcesMarket.marbleMatrix));
    }*/
}


