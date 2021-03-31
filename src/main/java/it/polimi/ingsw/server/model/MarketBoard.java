package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.concurrent.ThreadLocalRandom;

public class MarketBoard {

    private Marble[][] marbleMatrix;
    private int rows;
    private int columns;
    Marble externalMarble;
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
        test.marbleMatrix[randomRowPos][randomColumnPos] = test.externalMarble;
        test.externalMarble = temporaryMarble;


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

    public Marble[] pickResources(MarketLine line){
        int lineNumber = line.getLineNumber();
        return (lineNumber < rows) ? marbleMatrix[lineNumber] : getColumn(lineNumber);
    }

    public void updateMatrix(MarketLine line) {

        int lineNumber = line.getLineNumber();
        if (lineNumber < rows) {
                Marble nextExternalMarble = marbleMatrix[lineNumber][0];
                System.arraycopy(marbleMatrix[lineNumber], 1, marbleMatrix[lineNumber], 0, columns - 1);
                marbleMatrix[lineNumber][columns-1] = externalMarble;
                externalMarble = nextExternalMarble;
        }
        else {
            Marble nextExternalMarbel = marbleMatrix[rows-1][lineNumber];
            for(int i=1; i<rows; i++)
                marbleMatrix[i][lineNumber] = marbleMatrix[i-1][lineNumber];

            marbleMatrix[0][lineNumber] = externalMarble;
            externalMarble = nextExternalMarbel;
        }

    }

    private Marble[] getColumn(int column) {
        return pairToValue(
                    IntStream.range(0, rows)
                    .mapToObj(i -> new Pair<>(i, marbleMatrix[i][column-rows]))
                    .collect(Collectors.toList())
        );
    }


    public static void main(String[] args) {
        MarketBoard resourcesMarket = initializeMarketBoard("/Users/pablo/IdeaProjects/ing-sw-2021-laconca-lodari-giaccaglia/target/classes/config/MarketBoardConfig.json");
        System.out.println(Arrays.deepToString(resourcesMarket.marbleMatrix));
        System.out.println(Arrays.toString(resourcesMarket.pickResources(MarketLine.FIRST_COLUMN)));
        resourcesMarket.updateMatrix(MarketLine.FIRST_ROW);
        System.out.println(Arrays.deepToString(resourcesMarket.marbleMatrix));
    }
}


