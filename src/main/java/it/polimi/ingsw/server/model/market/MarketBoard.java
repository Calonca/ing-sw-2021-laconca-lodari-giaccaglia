package it.polimi.ingsw.server.model.market;

import com.google.gson.Gson;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.board.Box;
import javafx.util.Pair;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.ThreadLocalRandom;
import it.polimi.ingsw.server.model.player.*;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

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
     * {@link Marble MarketMarbles} picked from the MarketBoard and stored for white marble identification
     * and conversion process.
     */
    private Marble[] pickedMarbles;

    /**
     * Converted {@link Marble MarketMarbles} picked from the MarketBoard and stored in a
     * {@link Box} of Resources for player to store;
     */
    private Box mappedResources;

    /**
     * Number of White {@link Marble MarketMarbles} picked from the MarketBoard, necessary for
     * conversion process.
     */
    private int whiteMarblesQuantity;

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
    private Marble slideMarble;

    /**
     * Each row and column of the {@link MarketBoard#marbleMatrix} has a corresponding {@link MarketLine}.
     * A <em>MarketLine</em> is needed to pick an entire row or column during {@link State#CHOOSING_RESOURCES_FOR_PRODUCTION}
     * turn phase.
     */
    private MarketLine line;

    public static MarketBoard initializeMarketBoard(String configFilePath) throws IOException {
        Gson gson = new Gson();
        MarketBoard marketBoard;
        String MarketBoardClassConfig = Files.readString(Path.of(configFilePath), StandardCharsets.US_ASCII);
        marketBoard = gson.fromJson(MarketBoardClassConfig, MarketBoard.class);
        List<Marble> marbles  = Arrays
                .stream(marketBoard.marbleMatrix)
                .flatMap(Arrays::stream).collect(Collectors.toList());

        Collections.shuffle(marbles);

        final int rows = marketBoard.rows;

        marketBoard.marbleMatrix = IntStream.range(0,marketBoard.columns * marketBoard.rows)
                .mapToObj((pos)->new Pair<>(pos,marbles.get(pos)))
                .collect(groupingBy((e)->e.getKey()% rows))
                .values()
                .stream()
                .map(MarketBoard::pairToValue)
                    .toArray(Marble[][]::new);

        int randomRowPos = ThreadLocalRandom.current().nextInt(0, marketBoard.rows);
        int randomColumnPos = ThreadLocalRandom.current().nextInt(0, marketBoard.columns);
        marketBoard.slideMarble = Marble.WHITE;
        Marble temporaryMarble = marketBoard.marbleMatrix[randomRowPos][randomColumnPos];
        marketBoard.marbleMatrix[randomRowPos][randomColumnPos] = marketBoard.slideMarble;
        marketBoard.slideMarble = temporaryMarble;

        return marketBoard;
    }

    private static Marble[] pairToValue(List<Pair<Integer, Marble>> pos_marArray){
        return pos_marArray.stream().map(Pair::getValue).toArray(Marble[]::new);
    }

    /**
     * Selects a whole {@link MarketBoard#marbleMatrix} row or column as an array of {@link Marble Marbles}.
     *
     * @param line {@link MarketLine} corresponding to the row or column to get.
     *
     */
    public void chooseMarketLine(MarketLine line){

        this.line = line;
        int lineNumber = line.getLineNumber();
        mappedResources = Box.discardBox();

        pickedMarbles =  (lineNumber < rows) ?
                marbleMatrix[lineNumber]
                : getColumn(lineNumber);

        Map<Marble, Long> resourceOccurrences = getResourcesOccurrences(pickedMarbles);

        //counts number of white marbles in the picked line
        whiteMarblesQuantity = resourceOccurrences
                .entrySet()
                .stream()
                .filter(e -> e.getKey()==Marble.WHITE)
                .mapToInt(e -> 1).sum();

        //all marbles but white ones are mapped to their corresponding resources
        resourceOccurrences
                .entrySet()
                .stream()
                .filter(e -> e.getKey()!=Marble.WHITE)
                .forEach(
                        e -> mapResource(e.getKey().getConvertedMarble().getResourceNumber(),
                                e.getValue().intValue())
                );

    }

    private void mapResource(int resourceNumber, int quantity){

        int [] resourcesQuantites =IntStream.range(0, 4)
                .map(i ->  (i==resourceNumber) ? quantity : 0).toArray();

        mappedResources.addResources(resourcesQuantites);

    }

    private Map<Marble, Long> getResourcesOccurrences(Marble [] pickedMarbles){
        return Arrays.stream(pickedMarbles)
                .collect(groupingBy(Function.identity(), counting()));
    }

    public boolean areThereWhiteMarbles(){
        return whiteMarblesQuantity>0;
    }

    public int getNumberOfWhiteMarbles(){
        return whiteMarblesQuantity;
    }

    public void convertWhiteMarble(Resource mappedResource) {
        mapResource(mappedResource.getResourceNumber(), 1);
    }

    public Box getMappedResourcesBox(){
        return mappedResources;
    }

    /**
     * <p>Invoked after {@link MarketBoard#chooseMarketLine} method execution, to update {@link MarketBoard#marbleMatrix}
     * structure by inserting the {@link MarketBoard#slideMarble} in the chosen line.<br>
     * If the chosen line is a row, Marble in first position becomes the next <em>slideMarble</em> and the current <em>slideMarble</em>
     * is inserted in the last position of the line, after Marbles shifting one position to
     * the left.<br>
     * If the chosen line is a column, Marble in first position becomes the next <em>slideMarble</em> and the current <em>slideMarble</em>
     * is inserted in the last position of the column, after Marbles shifting one position down.
     *
     */
    public void updateMatrix() {

        int lineNumber = line.getLineNumber();
        if (lineNumber < rows) {
                Marble nextExternalMarble = marbleMatrix[lineNumber][0];
                System.arraycopy(marbleMatrix[lineNumber], 1, marbleMatrix[lineNumber], 0, columns - 1);
                marbleMatrix[lineNumber][columns-1] = slideMarble;
                slideMarble = nextExternalMarble;
        }
        else {
            Marble nextExternalMarble = marbleMatrix[rows-1][lineNumber-3];
            for(int i=1; i<rows; i++)
                marbleMatrix[i][lineNumber-3] = marbleMatrix[i-1][lineNumber-3];

            marbleMatrix[0][lineNumber-3] = slideMarble;
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
        System.out.println(Arrays.toString(resourcesMarket.chooseMarketLine(MarketLine.FIRST_COLUMN)));
        resourcesMarket.updateMatrix(MarketLine.FIRST_ROW);
        System.out.println(Arrays.deepToString(resourcesMarket.marbleMatrix));
    }*/
}


