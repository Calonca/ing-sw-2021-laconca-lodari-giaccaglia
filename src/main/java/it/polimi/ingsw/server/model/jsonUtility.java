package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.server.model.cards.CardShop;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.cards.DevelopmentCardDeck;
import it.polimi.ingsw.server.model.cards.production.Production;
import it.polimi.ingsw.server.model.player.board.LeaderDepot;
import it.polimi.ingsw.server.model.player.leaders.*;
import javafx.util.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class jsonUtility {

    private static Gson sGson = new Gson();

    public static String serializeVarArgs(Object ...o){
        return sGson.toJson(Arrays.stream(o).collect(Collectors.toList()));
    }

    //helper method to load a 48 devcards array from json
    public static List<DevelopmentCard> cardsFromJsonHandler(){
        Gson gson = new Gson();
        String Cards = null;
        try {
            Cards = Files.readString(Path.of("src/main/resources/config/DevelopmentCardConfig.json"), StandardCharsets.US_ASCII);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DevelopmentCard[] cardsArray = gson.fromJson(Cards, DevelopmentCard[].class);
        return (Arrays.asList(cardsArray));

    }
    //helper method to build CardShop json initialization from an array of 48 devcards
    public static void devCardsDeckDeserialization() {
        int cardsInDeck = 4;
        List<DevelopmentCard> cards = jsonUtility.cardsFromJsonHandler();
        List<DevelopmentCard> blueCards = cards.stream().filter(card -> card.getCardType() == DevelopmentCardColor.BLUE).collect(Collectors.toList());
        List<DevelopmentCard> yellowCards = cards.stream().filter(card -> card.getCardType() == DevelopmentCardColor.YELLOW).collect(Collectors.toList());
        List<DevelopmentCard> greenCards = cards.stream().filter(card -> card.getCardType() == DevelopmentCardColor.GREEN).collect(Collectors.toList());
        List<DevelopmentCard> purpleCards = cards.stream().filter(card -> card.getCardType() == DevelopmentCardColor.PURPLE).collect(Collectors.toList());

        List<List<DevelopmentCard>> decksByColor = new ArrayList<>();
        decksByColor.add(blueCards);
        decksByColor.add(yellowCards);
        decksByColor.add(greenCards);
        decksByColor.add(purpleCards);

        List<List<DevelopmentCard>> blueCardsByLevel = IntStream.range(1, cardsInDeck).mapToObj(i ->
                ((blueCards.stream().filter(blueCard -> blueCard.getLevel()==i))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());

        List<List<DevelopmentCard>> yellowCardsByLevel = IntStream.range(1, cardsInDeck).mapToObj(i ->
                ((yellowCards.stream().filter(yellowCard -> yellowCard.getLevel()==i))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());

        List<List<DevelopmentCard>> greenCardsByLevel = IntStream.range(1, cardsInDeck).mapToObj(i ->
                ((greenCards.stream().filter(greenCard -> greenCard.getLevel()==i))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());

        List<List<DevelopmentCard>> purpleCardsByLevel = IntStream.range(1, cardsInDeck).mapToObj(i ->
                ((purpleCards.stream().filter(purpleCard -> purpleCard.getLevel()==i))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());

        Map<DevelopmentCardColor, List<List<DevelopmentCard>>> decksOfCardsByColor = new HashMap<>();

        decksOfCardsByColor.put(DevelopmentCardColor.BLUE, blueCardsByLevel);
        decksOfCardsByColor.put(DevelopmentCardColor.YELLOW, yellowCardsByLevel);
        decksOfCardsByColor.put(DevelopmentCardColor.GREEN, greenCardsByLevel);
        decksOfCardsByColor.put(DevelopmentCardColor.PURPLE, purpleCardsByLevel);

        Map<Integer, List<DevelopmentCard>> blueCardsList = IntStream.range(1, cardsInDeck).boxed()
                .collect(Collectors.toMap(
                        level -> level, level -> decksOfCardsByColor.get(DevelopmentCardColor.BLUE).get(level-1)));

        Map<Integer, List<DevelopmentCard>> yellowCardsList = IntStream.range(1, cardsInDeck).boxed()
                .collect(Collectors.toMap(
                        level -> level, level -> decksOfCardsByColor.get(DevelopmentCardColor.YELLOW).get(level-1)));

        Map<Integer, List<DevelopmentCard>> greenCardsList = IntStream.range(1, cardsInDeck).boxed()
                .collect(Collectors.toMap(
                        level -> level, level -> decksOfCardsByColor.get(DevelopmentCardColor.GREEN).get(level-1)));

        Map<Integer, List<DevelopmentCard>> purpleCardsList = IntStream.range(1, cardsInDeck).boxed()
                .collect(Collectors.toMap(
                        level -> level, level -> decksOfCardsByColor.get(DevelopmentCardColor.PURPLE).get(level-1)));

        Map<Integer, DevelopmentCardDeck> blueDecks = IntStream.range(1, cardsInDeck).boxed().collect(Collectors.toMap(level -> level, level ->
                new DevelopmentCardDeck(level, DevelopmentCardColor.BLUE, blueCardsList.get(level), blueCardsList.get(level).size(), blueCardsList.get(level).size())));

        Map<Integer, DevelopmentCardDeck> purpleDecks = IntStream.range(1, cardsInDeck).boxed().collect(Collectors.toMap(level -> level, level ->
                new DevelopmentCardDeck(level, DevelopmentCardColor.PURPLE, purpleCardsList.get(level), purpleCardsList.get(level).size(), purpleCardsList.get(level).size() )));

        Map<Integer, DevelopmentCardDeck> yellowDecks = IntStream.range(1, cardsInDeck).boxed().collect(Collectors.toMap(level -> level, level ->
                new DevelopmentCardDeck(level, DevelopmentCardColor.YELLOW, yellowCardsList.get(level), yellowCardsList.get(level).size(), yellowCardsList.get(level).size() )));

        Map<Integer, DevelopmentCardDeck> greenDecks = IntStream.range(1, cardsInDeck).boxed().collect(Collectors.toMap(level -> level, level ->
                new DevelopmentCardDeck(level, DevelopmentCardColor.GREEN, greenCardsList.get(level), greenCardsList.get(level).size(), greenCardsList.get(level).size() )));

        Map<DevelopmentCardColor, Map<Integer, DevelopmentCardDeck>> listOfDecks = new HashMap<>();
        listOfDecks.put(DevelopmentCardColor.BLUE, blueDecks);
        listOfDecks.put(DevelopmentCardColor.PURPLE, purpleDecks);
        listOfDecks.put(DevelopmentCardColor.YELLOW, yellowDecks);
        listOfDecks.put(DevelopmentCardColor.GREEN, greenDecks);

      /*
        CardShop shop = new CardShop(listOfDecks);
        Writer writer = new FileWriter("src/main/resources/config/CardShopConfig.json");
        Gson gson = new GsonBuilder().create();
        gson.toJson(shop, writer);
        writer.flush(); //flush data to file   <---
        writer.close(); //close write          <---
       */

    }

    //helper method to initialize gameModel list of 16 leaders
    public static List<Leader> leaderCardsDeserialization(String jsonString) throws IOException{

        RuntimeTypeAdapterFactory<Leader> jsonToLeaderListAdapter = RuntimeTypeAdapterFactory.of(Leader.class);

        //Register here all the Leader types
        jsonToLeaderListAdapter.registerSubtype(DepositLeader.class);
        jsonToLeaderListAdapter.registerSubtype(MarketLeader.class);
        jsonToLeaderListAdapter.registerSubtype(ProductionLeader.class);
        jsonToLeaderListAdapter.registerSubtype(DevelopmentDiscountLeader.class);

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(jsonToLeaderListAdapter)
                .create();

      //  Type listOfLeaders = new TypeToken<ArrayList<Leader>>() {}.getType();
        String leadersJsonConfig = Files.readString(Path.of(jsonString), StandardCharsets.US_ASCII);
        Leader[] leaders = gson.fromJson(leadersJsonConfig, Leader[].class);
        return (Arrays.asList(leaders));
    }

    public void leaderCardsArrayserialization() throws IOException {
        Resource bonus;
        Production addProd;
        Pair<Resource, Integer> costTest;
        Pair<Resource, Integer> costTest2;
        Pair<Resource, Integer> costTest3;
        List<Pair<Resource, Integer>> requirementsTest;
        Pair<DevelopmentCardColor, Integer> costTestCards;
        Pair<DevelopmentCardColor, Integer> costTestCards2;
        List<Pair<DevelopmentCardColor, Integer>> requirementsTestCards;

        ArrayList<DevelopmentCard> series=new ArrayList<>();
        ArrayList<Leader> series2=new ArrayList<>();

        int victoryPoints;
        DevelopmentCardColor color;
        int level;

        addProd=new Production(new int[]{1,0,0,0,0,0,0},new int[]{0,0,0,0,2,0,0});
        costTest = new Pair<>(Resource.SHIELD, 2);
        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        victoryPoints=1;
        color=DevelopmentCardColor.GREEN;
        level=1;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));




        addProd=new Production(new int[]{2,0,0,0,0,0,0},new int[]{0,1,1,1,0,0,0});
        costTest = new Pair<>(Resource.SERVANT, 3);
        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        victoryPoints=3;
        level=1;
        color=DevelopmentCardColor.PURPLE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        addProd=new Production(new int[]{0,0,0,2,0,0,0},new int[]{1,1,1,0,0,0,0});
        costTest = new Pair<>(Resource.GOLD, 3);
        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        victoryPoints=3;
        level=1;
        color=DevelopmentCardColor.BLUE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        addProd=new Production(new int[]{0,2,0,0,0,0,0},new int[]{1,0,1,1,0,0,0});
        costTest = new Pair<>(Resource.STONE, 3);
        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        victoryPoints=3;
        level=1;
        color=DevelopmentCardColor.YELLOW;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));

        addProd=new Production(new int[]{0,1,0,1,0,0,0},new int[]{2,0,0,0,1,0,0});
        costTest = new Pair<>(Resource.SHIELD, 2);
        costTest2 = new Pair<>(Resource.GOLD, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        victoryPoints=4;
        level=1;
        color=DevelopmentCardColor.GREEN;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        //6
        addProd=new Production(new int[]{1,0,1,0,0,0,0},new int[]{0,0,0,2,1,0,0});
        costTest = new Pair<>(Resource.SERVANT, 2);
        costTest2 = new Pair<>(Resource.STONE, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        victoryPoints=4;
        level=1;
        color=DevelopmentCardColor.PURPLE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));

        //7

        addProd=new Production(new int[]{0,0,1,1,0,0,0},new int[]{0,2,0,0,1,0,0});
        costTest = new Pair<>(Resource.GOLD, 2);
        costTest2 = new Pair<>(Resource.SERVANT, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        victoryPoints=4;
        level=1;
        color=DevelopmentCardColor.BLUE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));



        //8

        addProd=new Production(new int[]{1,1,0,0,0,0,0},new int[]{0,0,2,0,1,0,0});
        costTest = new Pair<>(Resource.STONE, 2);
        costTest2 = new Pair<>(Resource.SHIELD, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        victoryPoints=4;
        level=1;
        color=DevelopmentCardColor.YELLOW;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        //9

        addProd=new Production(new int[]{0,0,0,1,0,0,0},new int[]{0,0,0,0,2,0,0});
        costTest = new Pair<>(Resource.SHIELD, 4);
        //costTest2 = new Pair<>(Resource.SHIELD, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        //requirementsTest.add(costTest2);
        victoryPoints=5;
        level=2;
        color=DevelopmentCardColor.GREEN;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));

//10
        addProd=new Production(new int[]{1,0,0,0,0,0,0},new int[]{0,0,0,0,2,0,0});
        costTest = new Pair<>(Resource.SERVANT, 4);
        //costTest2 = new Pair<>(Resource.SHIELD, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        //requirementsTest.add(costTest2);
        victoryPoints=5;
        level=2;
        color=DevelopmentCardColor.PURPLE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));
        //11

        addProd=new Production(new int[]{0,1,0,0,0,0,0},new int[]{0,0,0,0,2,0,0});
        costTest = new Pair<>(Resource.GOLD, 4);
        //costTest2 = new Pair<>(Resource.SHIELD, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        //requirementsTest.add(costTest2);
        victoryPoints=5;
        level=2;
        color=DevelopmentCardColor.BLUE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        //12
        addProd=new Production(new int[]{0,0,0,1,0,0,0},new int[]{0,0,0,0,1,0,0});
        costTest = new Pair<>(Resource.SERVANT, 2);
        //costTest2 = new Pair<>(Resource.SHIELD, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        //requirementsTest.add(costTest2);
        victoryPoints=1;
        level=1;
        color=DevelopmentCardColor.PURPLE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));



        //13
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,2,0,0});
        costTest = new Pair<>(Resource.STONE, 4);
        //costTest2 = new Pair<>(Resource.SHIELD, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        //requirementsTest.add(costTest2);
        victoryPoints=5;
        level=2;
        color=DevelopmentCardColor.YELLOW;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));



        //14
        addProd=new Production(new int[]{0,1,1,0,0,0,0},new int[]{0,0,0,3,0,0,0});
        costTest = new Pair<>(Resource.SHIELD, 3);
        costTest2 = new Pair<>(Resource.SERVANT, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        victoryPoints=5;
        level=2;
        color=DevelopmentCardColor.GREEN;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));



        //15
        addProd=new Production(new int[]{1,1,0,0,0,0,0},new int[]{0,0,3,0,0,0,0});
        costTest = new Pair<>(Resource.GOLD, 2);
        costTest2 = new Pair<>(Resource.SERVANT, 3);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        victoryPoints=6 ;
        level=2;
        color=DevelopmentCardColor.PURPLE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        //16
        addProd=new Production(new int[]{1,0,0,1,0,0,0},new int[]{0,3,0,0,0,0,0});
        costTest = new Pair<>(Resource.GOLD, 3);
        costTest2 = new Pair<>(Resource.STONE, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        victoryPoints=6 ;
        level=2;
        color=DevelopmentCardColor.BLUE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        //17
        addProd=new Production(new int[]{0,1,0,1,0,0,0},new int[]{3,0,0,0,0,0,0});
        costTest = new Pair<>(Resource.STONE, 3);
        costTest2 = new Pair<>(Resource.SHIELD, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        victoryPoints=6 ;
        level=2;
        color=DevelopmentCardColor.YELLOW;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));



        //18
        addProd=new Production(new int[]{2,0,0,1,0,0,0},new int[]{0,0,0,2,2,0,0});
        costTest = new Pair<>(Resource.SHIELD, 5);
        //costTest2 = new Pair<>(Resource.SHIELD, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        //requirementsTest.add(costTest2);
        victoryPoints=7 ;
        level=2;
        color=DevelopmentCardColor.GREEN;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));

        //19
        addProd=new Production(new int[]{0,0,0,2,0,0,0},new int[]{2,0,0,0,2,0,0});
        costTest = new Pair<>(Resource.SERVANT, 5);
        //costTest2 = new Pair<>(Resource.SHIELD, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        //requirementsTest.add(costTest2);
        victoryPoints=7 ;
        level=2;
        color=DevelopmentCardColor.PURPLE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));

        //20
        addProd=new Production(new int[]{0,2,0,0,0,0,0},new int[]{0,0,2,0,2,0,0});
        costTest = new Pair<>(Resource.GOLD, 5);
        //costTest2 = new Pair<>(Resource.SHIELD, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        //requirementsTest.add(costTest2);
        victoryPoints=7 ;
        level=2;
        color=DevelopmentCardColor.BLUE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        //21
        addProd=new Production(new int[]{0,0,2,0,0,0,0},new int[]{0,2,0,0,2,0,0});
        costTest = new Pair<>(Resource.STONE, 5);
        //costTest2 = new Pair<>(Resource.SHIELD, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        //requirementsTest.add(costTest2);
        victoryPoints=7 ;
        level=2;
        color=DevelopmentCardColor.YELLOW;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        //22
        addProd=new Production(new int[]{1,0,0,0,0,0,0},new int[]{0,0,2,0,1,0,0});
        costTest = new Pair<>(Resource.SHIELD, 3);
        costTest2 = new Pair<>(Resource.GOLD, 3);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        victoryPoints=8;
        level=2;
        color=DevelopmentCardColor.GREEN;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        //23
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,0});
        costTest = new Pair<>(Resource.GOLD, 2);
        //costTest2 = new Pair<>(Resource.GOLD, 3);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        //requirementsTest.add(costTest2);
        victoryPoints=1;
        level=1;
        color=DevelopmentCardColor.BLUE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        //24
        addProd=new Production(new int[]{0,0,0,1,0,0,0},new int[]{0,2,0,0,1,0,0});
        costTest = new Pair<>(Resource.SERVANT, 3);
        costTest2 = new Pair<>(Resource.SHIELD, 3);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        victoryPoints=8;
        level=2;
        color=DevelopmentCardColor.PURPLE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));



        //25
        addProd=new Production(new int[]{0,1,0,0,0,0,0},new int[]{0,0,0,2,1,0,0});
        costTest = new Pair<>(Resource.GOLD, 3);
        costTest2 = new Pair<>(Resource.STONE, 3);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        victoryPoints=8;
        level=2;
        color=DevelopmentCardColor.BLUE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));



        //26
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{2,0,0,0,1,0,0});
        costTest = new Pair<>(Resource.SERVANT, 3);
        costTest2 = new Pair<>(Resource.STONE, 3);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        victoryPoints=8;
        level=2;
        color=DevelopmentCardColor.YELLOW;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        //27
        addProd=new Production(new int[]{2,0,0,0,0,0,0},new int[]{0,0,0,3,2,0,0});
        costTest = new Pair<>(Resource.SHIELD, 6);
        //costTest2 = new Pair<>(Resource.STONE, 3);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        //requirementsTest.add(costTest2);
        victoryPoints=9;
        level=3;
        color=DevelopmentCardColor.GREEN;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        //28
        addProd=new Production(new int[]{0,0,0,2,0,0,0},new int[]{3,0,0,0,2,0,0});
        costTest = new Pair<>(Resource.SERVANT, 6);
        //costTest2 = new Pair<>(Resource.STONE, 3);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        //requirementsTest.add(costTest2);
        victoryPoints=9;
        level=3;
        color=DevelopmentCardColor.PURPLE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));




        //29
        addProd=new Production(new int[]{0,2,0,0,0,0,0},new int[]{0,0,3,0,2,0,0});
        costTest = new Pair<>(Resource.GOLD, 6);
        //costTest2 = new Pair<>(Resource.STONE, 3);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        //requirementsTest.add(costTest2);
        victoryPoints=9;
        level=3;
        color=DevelopmentCardColor.BLUE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));



        //30
        addProd=new Production(new int[]{0,0,2,0,0,0,0},new int[]{0,3,0,0,2,0,0});
        costTest = new Pair<>(Resource.STONE, 6);
        //costTest2 = new Pair<>(Resource.STONE, 3);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        //requirementsTest.add(costTest2);
        victoryPoints=9;
        level=3;
        color=DevelopmentCardColor.YELLOW;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        //31
        addProd=new Production(new int[]{1,1,0,0,0,0,0},new int[]{0,0,2,2,1,0,0});
        costTest = new Pair<>(Resource.SHIELD, 5);
        costTest2 = new Pair<>(Resource.SERVANT, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        victoryPoints=10;
        level=3;
        color=DevelopmentCardColor.GREEN;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));

        //32
        addProd=new Production(new int[]{0,0,1,1,0,0,0},new int[]{2,2,0,0,1,0,0});
        costTest = new Pair<>(Resource.SERVANT, 5);
        costTest2 = new Pair<>(Resource.GOLD, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        victoryPoints=10;
        level=3;
        color=DevelopmentCardColor.PURPLE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));



        //33
        addProd=new Production(new int[]{1,0,1,0,0,0,0},new int[]{0,2,0,2,1,0,0});
        costTest = new Pair<>(Resource.GOLD, 5);
        costTest2 = new Pair<>(Resource.STONE, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        victoryPoints=10;
        level=3;
        color=DevelopmentCardColor.BLUE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        //34
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,1,0,0,0,0,0});
        costTest = new Pair<>(Resource.STONE, 2);
        //costTest2 = new Pair<>(Resource.STONE, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        //requirementsTest.add(costTest2);
        victoryPoints=1;
        level=1;
        color=DevelopmentCardColor.YELLOW;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));



        //35
        addProd=new Production(new int[]{0,1,0,1,0,0,0},new int[]{2,0,2,0,1,0,0});
        costTest = new Pair<>(Resource.STONE, 5);
        costTest2 = new Pair<>(Resource.SERVANT, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        victoryPoints=10;
        level=3;
        color=DevelopmentCardColor.YELLOW;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));



        //36
        addProd=new Production(new int[]{0,1,0,0    ,0,0,0},new int[]{1,0,0,0,3,0,0});
        costTest = new Pair<>(Resource.SHIELD, 7);
        costTest2 = new Pair<>(Resource.SERVANT, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        //requirementsTest.add(costTest2);
        victoryPoints=11;
        level=3;
        color=DevelopmentCardColor.GREEN;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));




        //37
        addProd=new Production(new int[]{0,1,0,0,0,0,0},new int[]{0,0,0,1,3,0,0});
        costTest = new Pair<>(Resource.SERVANT, 7);
        costTest2 = new Pair<>(Resource.SERVANT, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        //requirementsTest.add(costTest2);
        victoryPoints=11;
        level=3;
        color=DevelopmentCardColor.PURPLE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));




        //38
        addProd=new Production(new int[]{0,0,0,1,0,0,0},new int[]{0,0,1,0,3,0,0});
        costTest = new Pair<>(Resource.GOLD, 7);
        costTest2 = new Pair<>(Resource.SERVANT, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        //requirementsTest.add(costTest2);
        victoryPoints=11;
        level=3;
        color=DevelopmentCardColor.BLUE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        //39
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,1,0,0,0,3,0,0});
        costTest = new Pair<>(Resource.STONE, 7);
        costTest2 = new Pair<>(Resource.SERVANT, 2);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        //requirementsTest.add(costTest2);
        victoryPoints=11;
        level=3;
        color=DevelopmentCardColor.YELLOW;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));



        //40
        addProd=new Production(new int[]{0,0,0,1,0,0,0},new int[]{3,0,1,0,0,0,0});
        costTest = new Pair<>(Resource.SHIELD, 4);
        costTest2 = new Pair<>(Resource.GOLD, 4);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        victoryPoints=12;
        level=3;
        color=DevelopmentCardColor.GREEN;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));






        //41
        addProd=new Production(new int[]{1,0,0,0,0,0,0},new int[]{0,1,0,3,0,0,0});
        costTest = new Pair<>(Resource.SERVANT, 4);
        costTest2 = new Pair<>(Resource.SHIELD, 4);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        victoryPoints=12;
        level=3;
        color=DevelopmentCardColor.PURPLE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        //42
        addProd=new Production(new int[]{0,1,0,0,0,0,0},new int[]{1,0,3,0,0,0,0});
        costTest = new Pair<>(Resource.GOLD, 4);
        costTest2 = new Pair<>(Resource.STONE, 4);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        victoryPoints=12;
        level=3;
        color=DevelopmentCardColor.BLUE ;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));



        //43
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,3,0,1,0,0,0});
        costTest = new Pair<>(Resource.STONE, 4);
        costTest2 = new Pair<>(Resource.SERVANT, 4);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        victoryPoints=12;
        level=3;
        color=DevelopmentCardColor.YELLOW ;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));




        //64
        addProd=new Production(new int[]{0,2,0,0,0,0,0},new int[]{1,0,1,1,0,0,0});
        costTest = new Pair<>(Resource.SHIELD, 3);
        costTest2 = new Pair<>(Resource.SERVANT, 4);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        //requirementsTest.add(costTest2);
        victoryPoints=3;
        level=1;
        color=DevelopmentCardColor.GREEN ;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));



        //63
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{1,0,0,0,0,0,0});
        costTest = new Pair<>(Resource.STONE, 1);
        costTest2 = new Pair<>(Resource.SHIELD, 1);
        costTest3 = new Pair<>(Resource.GOLD, 1);
        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        requirementsTest.add(costTest3);

        victoryPoints=2;
        level=1;
        color=DevelopmentCardColor.YELLOW ;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        //62
        addProd=new Production(new int[]{0,1,0,0,0,0,0},new int[]{0,0,0,1,0,0,0});
        costTest = new Pair<>(Resource.STONE, 1);
        costTest2 = new Pair<>(Resource.SERVANT, 1);
        costTest3 = new Pair<>(Resource.GOLD, 1);
        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        requirementsTest.add(costTest3);

        victoryPoints=2;
        level=1;
        color=DevelopmentCardColor.BLUE ;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        //56
        addProd=new Production(new int[]{1,0,0,0,0,0,0},new int[]{0,0,1,0,0,0,0});
        costTest = new Pair<>(Resource.SHIELD, 1);
        costTest2 = new Pair<>(Resource.SERVANT, 1);
        costTest3 = new Pair<>(Resource.GOLD, 1);
        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        requirementsTest.add(costTest3);

        victoryPoints=2;
        level=1;
        color=DevelopmentCardColor.PURPLE ;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));



        //45
        addProd=new Production(new int[]{1,0,0,0,0,0,0},new int[]{0,0,1,0,0,0,0});
        costTest = new Pair<>(Resource.SHIELD, 1);
        costTest2 = new Pair<>(Resource.SERVANT, 1);
        costTest3 = new Pair<>(Resource.STONE, 1);
        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        requirementsTest.add(costTest3);

        victoryPoints=2;
        level=1;
        color=DevelopmentCardColor.GREEN ;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));
        Gson gsonprint= new Gson();
        System.out.println(gsonprint.toJson(series));


        Leader leader;
        ProductionLeader cjclem;


        //61
        addProd=new Production(new int[]{1,0,0,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.GREEN, 1);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();

        requirementsTestCards = new ArrayList<Pair<DevelopmentCardColor, Integer>>();
        requirementsTestCards.add(costTestCards);

        victoryPoints=4;
        level=2;

        series2.add(new ProductionLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,addProd,level));



        //60
        addProd=new Production(new int[]{0,0,0,1,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.PURPLE, 1);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();

        requirementsTestCards = new ArrayList<Pair<DevelopmentCardColor, Integer>>();
        requirementsTestCards.add(costTestCards);

        victoryPoints=4;
        level=2;

        series2.add(new ProductionLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,addProd,level));


        //59
        addProd=new Production(new int[]{0,1,0,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.BLUE, 1);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();

        requirementsTestCards = new ArrayList<Pair<DevelopmentCardColor, Integer>>();
        requirementsTestCards.add(costTestCards);

        victoryPoints=4;
        level=2;

        series2.add(new ProductionLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,addProd,level));

        //58
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.YELLOW, 1);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();

        requirementsTestCards = new ArrayList<Pair<DevelopmentCardColor, Integer>>();
        requirementsTestCards.add(costTestCards);

        victoryPoints=4;
        level=2;

        series2.add(new ProductionLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,addProd,level));


        //57
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.PURPLE, 2);
        costTestCards2 = new Pair<>(DevelopmentCardColor.GREEN, 1);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();

        requirementsTestCards = new ArrayList<Pair<DevelopmentCardColor, Integer>>();
        requirementsTestCards.add(costTestCards);
        requirementsTestCards.add(costTestCards2);

        victoryPoints=5;
        level=1;
        bonus=Resource.GOLD;

        series2.add(new MarketLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,bonus));


        //55
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.BLUE, 2);
        costTestCards2 = new Pair<>(DevelopmentCardColor.YELLOW, 1);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();

        requirementsTestCards = new ArrayList<Pair<DevelopmentCardColor, Integer>>();
        requirementsTestCards.add(costTestCards);
        requirementsTestCards.add(costTestCards2);

        victoryPoints=5;
        bonus=Resource.STONE;


        series2.add(new MarketLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,bonus));



        //54
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.GREEN, 2);
        costTestCards2 = new Pair<>(DevelopmentCardColor.PURPLE, 1);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();

        requirementsTestCards = new ArrayList<Pair<DevelopmentCardColor, Integer>>();
        requirementsTestCards.add(costTestCards);
        requirementsTestCards.add(costTestCards2);

        victoryPoints=5;
        bonus =Resource.SHIELD;


        series2.add(new MarketLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,bonus));


        //53
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.YELLOW, 2);
        costTestCards2 = new Pair<>(DevelopmentCardColor.BLUE, 1);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();

        requirementsTestCards = new ArrayList<Pair<DevelopmentCardColor, Integer>>();
        requirementsTestCards.add(costTestCards);
        requirementsTestCards.add(costTestCards2);

        victoryPoints=5;
        bonus =Resource.SERVANT;


        series2.add(new MarketLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,bonus));


        LeaderDepot depo;

        //52
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTest = new Pair<>(Resource.SHIELD, 5);
        costTestCards2 = new Pair<>(DevelopmentCardColor.BLUE, 1);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTestCards = new ArrayList<Pair<DevelopmentCardColor, Integer>>();

        requirementsTest.add(costTest);

        victoryPoints=3;
        depo=new LeaderDepot(0,Resource.GOLD);

        series2.add(new DepositLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,depo));


        //51
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTest = new Pair<>(Resource.SERVANT, 5);
        costTestCards2 = new Pair<>(DevelopmentCardColor.BLUE, 1);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTestCards = new ArrayList<Pair<DevelopmentCardColor, Integer>>();

        requirementsTest.add(costTest);

        victoryPoints=3;
        depo=new LeaderDepot(0,Resource.SHIELD);

        series2.add(new DepositLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,depo));



        //50
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTest = new Pair<>(Resource.STONE, 5);
        costTestCards2 = new Pair<>(DevelopmentCardColor.BLUE, 1);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTestCards = new ArrayList<Pair<DevelopmentCardColor, Integer>>();

        requirementsTest.add(costTest);

        victoryPoints=3;
        depo=new LeaderDepot(0,Resource.SERVANT);

        series2.add(new DepositLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,depo));




        //49
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTest = new Pair<>(Resource.GOLD, 5);
        costTestCards2 = new Pair<>(DevelopmentCardColor.BLUE, 1);

        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTestCards = new ArrayList<Pair<DevelopmentCardColor, Integer>>();

        requirementsTest.add(costTest);

        victoryPoints=3;
        depo=new LeaderDepot(0,Resource.STONE);

        series2.add(new DepositLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,depo));




        //48
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.YELLOW, 1);
        costTestCards2 = new Pair<>(DevelopmentCardColor.PURPLE, 1);


        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTestCards = new ArrayList<Pair<DevelopmentCardColor, Integer>>();

        requirementsTestCards.add(costTestCards);
        requirementsTestCards.add(costTestCards2);


        victoryPoints=2;
        costTest = new Pair<>(Resource.GOLD, 1);


        series2.add(new DevelopmentDiscountLeader(  LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,costTest));



        //47
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.GREEN, 1);
        costTestCards2 = new Pair<>(DevelopmentCardColor.BLUE, 1);


        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTestCards = new ArrayList<Pair<DevelopmentCardColor, Integer>>();

        requirementsTestCards.add(costTestCards);
        requirementsTestCards.add(costTestCards2);


        victoryPoints=2;
        costTest = new Pair<>(Resource.STONE, 1);


        series2.add(new DevelopmentDiscountLeader(  LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,costTest));



        //46
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.PURPLE, 1);
        costTestCards2 = new Pair<>(DevelopmentCardColor.BLUE, 1);


        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTestCards = new ArrayList<Pair<DevelopmentCardColor, Integer>>();

        requirementsTestCards.add(costTestCards);
        requirementsTestCards.add(costTestCards2);


        victoryPoints=2;
        costTest = new Pair<>(Resource.SHIELD, 1);


        series2.add(new DevelopmentDiscountLeader(  LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,costTest));



        //45
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.PURPLE, 1);
        costTestCards2 = new Pair<>(DevelopmentCardColor.BLUE, 1);


        requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTestCards = new ArrayList<Pair<DevelopmentCardColor, Integer>>();

        requirementsTestCards.add(costTestCards);
        requirementsTestCards.add(costTestCards2);


        victoryPoints=2;
        costTest = new Pair<>(Resource.SHIELD, 1);


        series2.add(new DevelopmentDiscountLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,costTest));


        RuntimeTypeAdapterFactory<Leader> shapeAdapterFactory = RuntimeTypeAdapterFactory.of(Leader.class);


        shapeAdapterFactory.registerSubtype(DepositLeader.class);
        shapeAdapterFactory.registerSubtype(MarketLeader.class);
        shapeAdapterFactory.registerSubtype(ProductionLeader.class);
        shapeAdapterFactory.registerSubtype(DevelopmentDiscountLeader.class);


        Gson gson1 = new GsonBuilder()
                .registerTypeAdapterFactory(shapeAdapterFactory)
                .create();

        String serialized = gson1.toJson(series2.toArray(Leader[]::new), Leader[].class);
        Writer writer = new FileWriter("src/main/resources/config/LeadersConfig.json");
        writer.write(serialized);
        writer.flush(); //flush data to file   <---
        writer.close(); //close write          <---
        Leader[] leaders = gson1.fromJson(serialized,Leader[].class);
    }
}

