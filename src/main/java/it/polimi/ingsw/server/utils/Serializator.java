package it.polimi.ingsw.server.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.network.assets.*;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.marbles.MarbleAsset;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.assets.tokens.ActionTokenAsset;
import it.polimi.ingsw.network.jsonUtils.UUIDTypeAdapter;
import it.polimi.ingsw.server.controller.Match;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.server.model.cards.production.Production;
import it.polimi.ingsw.server.model.player.board.LeaderDepot;
import it.polimi.ingsw.server.model.player.board.StorageUnit;
import it.polimi.ingsw.server.model.player.leaders.*;
import it.polimi.ingsw.server.model.states.State;
import it.polimi.ingsw.server.model.states.StatesTransitionTable;
import javafx.util.Pair;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.text.DateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.*;

import static it.polimi.ingsw.server.utils.Deserializator.*;

public class Serializator extends JsonUtility {

    public static final String frontDevCardPathString = "/assets/devCards/raw/FRONT/Masters of Renaissance_Cards_FRONT_";
    public static final String backDevCardPathString = "/assets/devCards/raw/BACK/Masters of Renaissance__Cards_BACK_";
    public static final String frontDevCardGrayedOutPathString = "/assets/devCards/grayed out/FRONT/Masters of Renaissance_Cards_FRONT_";
    public static final String backDevCardGrayedOutPathString = "/assets/devCards/grayed out/BACK/Masters of Renaissance_Cards_BACK_";

    public static final String frontLeaderCardPathString = "/assets/leaders/raw/FRONT/Masters of Renaissance_Cards_FRONT_";
    public static final String backLeaderCardPathString = "/assets/leaders/raw/BACK/Masters of Renaissance__Cards_BACK.png";
    public static final String frontLeaderCardGrayedOutPathString = "/assets/leaders/grayed out/FRONT/Masters of Renaissance_Cards_FRONT_";
    public static final String backLeaderCardGrayedOutPathString = "/assets/leaders/grayed out/BACK/Masters of Renaissance__Cards_BACK.png";

    private static final GsonBuilder gsonBuilder = new GsonBuilder();
    //helper method to serialize cardshop configuration

    public static void cardShopSerialization(){
        CardShop shop = new CardShop(Deserializator.devCardsDeckDeserialization());
        serialize(writeConfigPathString + "CardShopConfig.json", shop, CardShop.class, gsonBuilder.registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).setPrettyPrinting().create());
    }

    private static Map<UUID , DevelopmentCardAsset> devCardsAssetsBuilder() {

        Map<UUID, NetworkDevelopmentCard> cardsFromJsonHandlerMap = networkDevCardsMap();

        return cardsFromJsonHandlerMap.keySet().stream().map(id -> {

            NetworkDevelopmentCard card = cardsFromJsonHandlerMap.get(id);
            String cardPathSuffix = card.getCardType().toString() + "_" + card.getVictoryPoints() + ".png";
            String frontPurchasable = frontDevCardPathString + cardPathSuffix;
            String backPurchasable = backDevCardPathString + cardPathSuffix;
            String frontNotPurchasable = frontDevCardGrayedOutPathString + cardPathSuffix;
            String backNotPurchasable = backDevCardGrayedOutPathString + cardPathSuffix;
            return new DevelopmentCardAsset(card, frontPurchasable, backPurchasable, frontNotPurchasable, backNotPurchasable , id);

        }).collect(Collectors.toMap(DevelopmentCardAsset::getCardId, Function.identity()));

    }

    private static Map<UUID , LeaderCardAsset> networkLeaderCardsAssetsMapBuilder() {

            AtomicReference<Integer> i = new AtomicReference<>(0);

        return Deserializator.networkLeaderCardsDeserialization().keySet().stream().collect(Collectors.toMap(index -> index , index -> {


            String cardPathSuffix = i.toString() + ".png";
            String frontActivated = frontLeaderCardPathString + cardPathSuffix;
            String frontInactive = frontLeaderCardGrayedOutPathString + cardPathSuffix;

            i.getAndSet(i.get() + 1);

            return new LeaderCardAsset(Deserializator.networkLeaderCardsDeserialization().get(index), frontInactive, backLeaderCardGrayedOutPathString, frontActivated, backLeaderCardPathString, index);

        }));

    }

    public static void devCardsAssetsMapSerialization(){
        Gson customGson = gsonBuilder.enableComplexMapKeySerialization().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).registerTypeHierarchyAdapter(Path.class, new PathConverter()).setPrettyPrinting().create();
        serialize(writeConfigPathString + "DevCardsAssetsMapConfig.json", devCardsAssetsBuilder(), Map.class, customGson);
    }

    public static void networkLeaderCardsAssetsMapSerialization(){

        Gson customGson =
                gsonBuilder
                .enableComplexMapKeySerialization()
                .registerTypeAdapterFactory(GsonAdapters.gsonNetworkLeaderAdapter)
                .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
                .registerTypeHierarchyAdapter(Path.class, new PathConverter())
                .setPrettyPrinting()
                .create();

        serialize(writeConfigPathString + "NetworkLeaderCardsAssetsMap.json", networkLeaderCardsAssetsMapBuilder(), Map.class, customGson);
    }

    public static void leaderCardsArraySerialization() {
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
        requirementsTest = new ArrayList<>();
        requirementsTest.add(costTest);
        victoryPoints=1;
        color=DevelopmentCardColor.GREEN;
        level=1;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));




        addProd=new Production(new int[]{2,0,0,0,0,0,0},new int[]{0,1,1,1,0,0,0});
        costTest = new Pair<>(Resource.SERVANT, 3);
        requirementsTest = new ArrayList<>();
        requirementsTest.add(costTest);
        victoryPoints=3;
        level=1;
        color=DevelopmentCardColor.PURPLE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        addProd=new Production(new int[]{0,0,0,2,0,0,0},new int[]{1,1,1,0,0,0,0});
        costTest = new Pair<>(Resource.GOLD, 3);
        requirementsTest = new ArrayList<>();
        requirementsTest.add(costTest);
        victoryPoints=3;
        level=1;
        color=DevelopmentCardColor.BLUE;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));


        addProd=new Production(new int[]{0,2,0,0,0,0,0},new int[]{1,0,1,1,0,0,0});
        costTest = new Pair<>(Resource.STONE, 3);
        requirementsTest = new ArrayList<>();
        requirementsTest.add(costTest);
        victoryPoints=3;
        level=1;
        color=DevelopmentCardColor.YELLOW;

        series.add(new DevelopmentCard(level, color,addProd,victoryPoints,requirementsTest));

        addProd=new Production(new int[]{0,1,0,1,0,0,0},new int[]{2,0,0,0,1,0,0});
        costTest = new Pair<>(Resource.SHIELD, 2);
        costTest2 = new Pair<>(Resource.GOLD, 2);

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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

        requirementsTest = new ArrayList<>();
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
        requirementsTest = new ArrayList<>();
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
        requirementsTest = new ArrayList<>();
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
        requirementsTest = new ArrayList<>();
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
        requirementsTest = new ArrayList<>();
        requirementsTest.add(costTest);
        requirementsTest.add(costTest2);
        requirementsTest.add(costTest3);

        victoryPoints=2;
        level=1;
        color=DevelopmentCardColor.GREEN ;

       /* series.add(new NetworkDevelopmentCard(level, color,addProd,points,requirementsTest));
        Gson gsonprint= new Gson();
        System.out.println(gsonprint.toJson(series));
        */


        Leader leader;
        ProductionLeader cjclem;


        //61
        addProd=new Production(new int[]{1,0,0,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.GREEN, 1);

        requirementsTest = new ArrayList<>();

        requirementsTestCards = new ArrayList<>();
        requirementsTestCards.add(costTestCards);

        victoryPoints=4;
        level=2;

        series2.add(new ProductionLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,addProd,level));



        //60
        addProd=new Production(new int[]{0,0,0,1,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.PURPLE, 1);

        requirementsTest = new ArrayList<>();

        requirementsTestCards = new ArrayList<>();
        requirementsTestCards.add(costTestCards);

        victoryPoints=4;
        level=2;

        series2.add(new ProductionLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,addProd,level));


        //59
        addProd=new Production(new int[]{0,1,0,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.BLUE, 1);

        requirementsTest = new ArrayList<>();

        requirementsTestCards = new ArrayList<>();
        requirementsTestCards.add(costTestCards);

        victoryPoints=4;
        level=2;

        series2.add(new ProductionLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,addProd,level));

        //58
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.YELLOW, 1);

        requirementsTest = new ArrayList<>();

        requirementsTestCards = new ArrayList<>();
        requirementsTestCards.add(costTestCards);

        victoryPoints=4;
        level=2;

        series2.add(new ProductionLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,addProd,level));


        //57
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.PURPLE, 2);
        costTestCards2 = new Pair<>(DevelopmentCardColor.GREEN, 1);

        requirementsTest = new ArrayList<>();

        requirementsTestCards = new ArrayList<>();
        requirementsTestCards.add(costTestCards);
        requirementsTestCards.add(costTestCards2);

        victoryPoints=5;
        level=1;
        bonus= Resource.GOLD;

        series2.add(new MarketLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,bonus));


        //55
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.BLUE, 2);
        costTestCards2 = new Pair<>(DevelopmentCardColor.YELLOW, 1);

        requirementsTest = new ArrayList<>();

        requirementsTestCards = new ArrayList<>();
        requirementsTestCards.add(costTestCards);
        requirementsTestCards.add(costTestCards2);

        victoryPoints=5;
        bonus= Resource.STONE;


        series2.add(new MarketLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,bonus));



        //54
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.GREEN, 2);
        costTestCards2 = new Pair<>(DevelopmentCardColor.PURPLE, 1);

        requirementsTest = new ArrayList<>();

        requirementsTestCards = new ArrayList<>();
        requirementsTestCards.add(costTestCards);
        requirementsTestCards.add(costTestCards2);

        victoryPoints=5;
        bonus = Resource.SHIELD;


        series2.add(new MarketLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,bonus));


        //53
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.YELLOW, 2);
        costTestCards2 = new Pair<>(DevelopmentCardColor.BLUE, 1);

        requirementsTest = new ArrayList<>();

        requirementsTestCards = new ArrayList<>();
        requirementsTestCards.add(costTestCards);
        requirementsTestCards.add(costTestCards2);

        victoryPoints=5;
        bonus = Resource.SERVANT;


        series2.add(new MarketLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,bonus));


        LeaderDepot depo;

        //52
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTest = new Pair<>(Resource.SHIELD, 5);
        costTestCards2 = new Pair<>(DevelopmentCardColor.BLUE, 1);

        requirementsTest = new ArrayList<>();
        requirementsTestCards = new ArrayList<>();

        requirementsTest.add(costTest);

        victoryPoints=3;
        depo=new LeaderDepot(0, Resource.GOLD);

        series2.add(new DepositLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,depo));


        //51
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTest = new Pair<>(Resource.SERVANT, 5);
        costTestCards2 = new Pair<>(DevelopmentCardColor.BLUE, 1);

        requirementsTest = new ArrayList<>();
        requirementsTestCards = new ArrayList<>();

        requirementsTest.add(costTest);

        victoryPoints=3;
        depo=new LeaderDepot(6, Resource.SHIELD);

        series2.add(new DepositLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,depo));



        //50
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTest = new Pair<>(Resource.STONE, 5);
        costTestCards2 = new Pair<>(DevelopmentCardColor.BLUE, 1);

        requirementsTest = new ArrayList<>();
        requirementsTestCards = new ArrayList<>();

        requirementsTest.add(costTest);

        victoryPoints=3;
        depo=new LeaderDepot(0, Resource.SERVANT);

        series2.add(new DepositLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,depo));




        //49
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTest = new Pair<>(Resource.GOLD, 5);
        costTestCards2 = new Pair<>(DevelopmentCardColor.BLUE, 1);

        requirementsTest = new ArrayList<>();
        requirementsTestCards = new ArrayList<>();

        requirementsTest.add(costTest);

        victoryPoints=3;
        depo=new LeaderDepot(0, Resource.STONE);

        series2.add(new DepositLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,depo));




        //48
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.YELLOW, 1);
        costTestCards2 = new Pair<>(DevelopmentCardColor.PURPLE, 1);


        requirementsTest = new ArrayList<>();
        requirementsTestCards = new ArrayList<>();

        requirementsTestCards.add(costTestCards);
        requirementsTestCards.add(costTestCards2);


        victoryPoints=2;
        costTest = new Pair<>(Resource.GOLD, 1);


        series2.add(new DevelopmentDiscountLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,costTest));



        //47
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.GREEN, 1);
        costTestCards2 = new Pair<>(DevelopmentCardColor.BLUE, 1);


        requirementsTest = new ArrayList<>();
        requirementsTestCards = new ArrayList<>();

        requirementsTestCards.add(costTestCards);
        requirementsTestCards.add(costTestCards2);


        victoryPoints=2;
        costTest = new Pair<>(Resource.STONE, 1);


        series2.add(new DevelopmentDiscountLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,costTest));



        //46
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.PURPLE, 1);
        costTestCards2 = new Pair<>(DevelopmentCardColor.BLUE, 1);


        requirementsTest = new ArrayList<>();
        requirementsTestCards = new ArrayList<>();

        requirementsTestCards.add(costTestCards);
        requirementsTestCards.add(costTestCards2);


        victoryPoints=2;
        costTest = new Pair<>(Resource.SHIELD, 1);


        series2.add(new DevelopmentDiscountLeader(  LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,costTest));



        //45
        addProd=new Production(new int[]{0,0,1,0,0,0,0},new int[]{0,0,0,0,1,0,1});
        costTestCards = new Pair<>(DevelopmentCardColor.YELLOW, 1);
        costTestCards2 = new Pair<>(DevelopmentCardColor.GREEN, 1);


        requirementsTest = new ArrayList<>();
        requirementsTestCards = new ArrayList<>();

        requirementsTestCards.add(costTestCards);
        requirementsTestCards.add(costTestCards2);


        victoryPoints=2;
        costTest = new Pair<>(Resource.SERVANT, 1);


        series2.add(new DevelopmentDiscountLeader(LeaderState.INACTIVE,victoryPoints,requirementsTest,requirementsTestCards,costTest));



        Gson gson1 = gsonBuilder
                .registerTypeAdapterFactory(GsonAdapters.gsonLeaderAdapter).setPrettyPrinting()
                .create();

        serialize(writeConfigPathString +"LeadersConfig.json", series2.toArray(Leader[]::new), Leader[].class, gson1);
     /*   String serialized = gson1.toJson(series2.toArray(Leader[]::new), Leader[].class);
        Writer writer = new FileWriter("src/main/resources/config/LeadersConfig.json");
        writer.write(serialized);
        writer.flush(); //flush data to file   <---
        writer.close(); //close write          <---  */


        //   Leader[] cards.leaders = deserialize("src/main/resources/config/LeadersConfig.json" , Leader[].class);
    }




    public static void leaderCardsMapSerialization(){


        Gson customGson = new GsonBuilder().enableComplexMapKeySerialization()
                .registerTypeAdapterFactory(GsonAdapters.gsonLeaderAdapter)
                .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
                .registerTypeHierarchyAdapter(Path.class, new PathConverter())
                .enableComplexMapKeySerialization()
                .setPrettyPrinting().create();

        Type type = (new TypeToken<Map<UUID, Leader>>() {}).getType();

        serialize("src/main/resources/config/LeadersCardMapConfig.json", Deserializator.leadersCardMapBuilder(), type, customGson);
    }

    public static void serializeMarblesAssets(){
        Map<MarbleAsset, Path> marbles = Arrays.stream(MarbleAsset.values()).collect(Collectors.toMap(marble -> marble, MarbleAsset::getPath));
        Gson customGson = gsonBuilder.enableComplexMapKeySerialization().registerTypeHierarchyAdapter(Path.class, new PathConverter()).setPrettyPrinting().create();
        String path = "src/main/resources/clientconfig/MarblesAssetsConfig.json";
        serialize(path ,marbles, Map.class, customGson);
    }

    public static void serializeTokensAssets(){

        Map<ActionTokenAsset, Pair<Path, String>> tokens = Arrays.stream(ActionTokenAsset.values()).collect(Collectors.toMap(token -> token,
                token -> new Pair<>(token.getFrontPath(), token.getEffectDescription())));
        Gson customGson = gsonBuilder.enableComplexMapKeySerialization().registerTypeHierarchyAdapter(Path.class, new PathConverter()).setPrettyPrinting().create();
        String path = "src/main/resources/clientconfig/TokenAssetsConfig.json";
        serialize(path ,tokens, Map.class, customGson);

    }

    public static void serializeResources(){

        Map<ResourceAsset, Pair<Integer, Path>> resources = Arrays.stream(ResourceAsset.values()).collect(Collectors.toMap(
                resource -> resource,
                resource -> new Pair<>(resource.getResourceNumber(), resource.getResourcePath())
        ));
        Gson customGson = gsonBuilder.enableComplexMapKeySerialization().registerTypeHierarchyAdapter(Path.class, new PathConverter()).setPrettyPrinting().create();
        String path = "src/main/resources/clientconfig/ResourcesAssetsConfig.json";
        serialize(path, resources, Map.class, customGson);
    }

    private static void serializeSinglePlayerStatesTransitionTable () {

        Gson customGson = new GsonBuilder()
                .registerTypeAdapterFactory(GsonAdapters.gsonStrategyAdapter).setPrettyPrinting()
                .create();

        JsonUtility.serialize(
                JsonUtility.writeConfigPathString + StatesTransitionTable.singlePLayerTableFile,
                StatesTransitionTable.setupCommonStatesTransitionTable(),
                StatesTransitionTable.class,
                customGson
        );

    }

    private static void serializeMultiPlayerStatesTransitionTable() {

        Gson customGson = new GsonBuilder()
                .registerTypeAdapterFactory(GsonAdapters.gsonStrategyAdapter).setPrettyPrinting()
                .create();

        JsonUtility.serialize(
                JsonUtility.writeConfigPathString + StatesTransitionTable.multiPLayerTableFile,
                StatesTransitionTable.setupCommonStatesTransitionTable(),
                StatesTransitionTable.class,
                customGson
        );

    }

    public static void serializeMatch(Match match, String path) throws IOException {

        Gson customGson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .registerTypeAdapterFactory(GsonAdapters.gsonLeaderAdapter)
                .registerTypeAdapterFactory(GsonAdapters.gsonDepotAdapter)
                .registerTypeAdapterFactory(GsonAdapters.gsonStrategyAdapter)
                .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
                .setPrettyPrinting()
                .setDateFormat(DateFormat.FULL, DateFormat.FULL)
                .create();

        JsonUtility.serializeBigObject(path, match, Match.class, customGson);

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
/*
        leaderCardsArraySerialization();
        devCardsAssetsMapSerialization();
        networkLeaderCardsAssetsMapSerialization();

        serializeMarbles();
         serializeSinglePlayerStatesTransitionTable();
        serializeMultiPlayerStatesTransitionTable();
        cardShopSerialization();
        serializeTokensAssets();
        it.polimi.ingsw.client.json.Deserializator.initializeActionTokenFromConfig();
        List<ActionTokenAsset> assets = Arrays.asList(ActionTokenAsset.values().clone());
        int cioa= 5;
          serializeResources();
        leaderCardsArraySerialization();

       leaderCardsArraySerialization();
        devCardsAssetsMapSerialization();
        networkLeaderCardsAssetsMapSerialization();

        serializeMarblesAssets();

        cardShopSerialization();
        serializeTokensAssets();

        serializeSinglePlayerStatesTransitionTable();
        serializeMultiPlayerStatesTransitionTable();
        Match matchDeserialized = Deserializator.deserializeMatch("/savedMatches/pa|076cc1e7-5606-4710-92a9-fd907f564b5e.json");

 */







    }

}
