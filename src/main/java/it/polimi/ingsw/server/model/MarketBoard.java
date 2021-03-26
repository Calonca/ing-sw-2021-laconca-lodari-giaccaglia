package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MarketBoard {

    private Marble[][] marbleMatrix;
    private int rows;
    private int columns;
    Marble externalMarble;

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
            test.marbleMatrix = IntStream.range(0,test.columns* test.rows)
                .mapToObj((pos)->new Pair<>(pos,marbles.get(pos)))
                .collect(Collectors.groupingBy((e)->e.getKey()% rows))
                .values()
                .stream()
                .map(MarketBoard::pairToValue)
                    .toArray(Marble[][]::new);
        
        
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


    public static void main(String[] args) {

        MarketBoard resourcesMarket = initializeMarketBoard();
        System.out.println(Arrays.deepToString(resourcesMarket.marbleMatrix));
    }
}


