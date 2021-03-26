package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

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

        } catch (IOException e) {
            System.out.println("Error while class initialization with json config file at path: " + configFilePath);
            e.printStackTrace();
        }
        return test;
    }


    public static void main(String[] args) throws IOException {

        MarketBoard resourcesMarket = MarketBoard.initializeMarketBoard("MarketBoardConfigPATH");

        // List<Marble[]> testList = Arrays.asList(resourcesMarket.marbleMatrix);

        Marble[] test  = (Arrays
                .stream(resourcesMarket.marbleMatrix)
                .flatMap(Arrays::stream)
                .toArray(Marble[]::new));

        Util.shuffleArray(test);


        Gson gson = new GsonBuilder().create();
        Writer writer = new FileWriter("MareblesArray.json");
        gson.toJson(test, writer);
        System.out.println(5);
        writer.flush(); //flush data to file   <---
        writer.close(); //close write
    }
}


