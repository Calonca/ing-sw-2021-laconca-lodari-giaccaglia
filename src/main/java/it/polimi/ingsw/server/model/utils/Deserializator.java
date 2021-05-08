package it.polimi.ingsw.server.model.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.assets.devcards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.cards.CardShop;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.DevelopmentCardDeck;
import it.polimi.ingsw.server.model.market.MarketBoard;
import it.polimi.ingsw.server.model.player.leaders.*;
import it.polimi.ingsw.server.model.player.track.FaithTrack;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Deserializator extends JsonUtility {

    public static final String frontDevCardPathString = "src/main/resources/cards/devCards/FRONT/Masters of Renaissance_Cards_FRONT_";
    public static final String backDevCardPathString = "src/main/resources/cards/devCards/BACK/Masters of Renaissance__Cards_BACK_";
    public static final String frontLeaderCardPathString = "src/main/resources/cards.leaders/FRONT/Masters of Renaissance_Cards_FRONT_";
    public static final String backLeaderCardPathString = "src/main/resources/cards.leaders/BACK/Masters of Renaissance__Cards_BACK.png";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static MarketBoard marketBoardDeserialization(){
        return deserialize(configPathString + "MarketBoardConfig.json", MarketBoard.class);
    }

    public static CardShop cardShopDeserialization(){
        return deserialize(configPathString + "CardShopConfig.json", CardShop.class);
    }

    //helper method to build 12 devDecks from an array of 48 devcards
    public static  Map<DevelopmentCardColor, Map<Integer, DevelopmentCardDeck>> devCardsDeckDeserialization() {
        int cardsInDeck = 4;
        List<DevelopmentCard> cards = devCardsListDeserialization();
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

        return listOfDecks;
    }

    //helper method to initialize gameModel list of 16 leaders
    public static List<Leader> leaderCardsDeserialization(){

        RuntimeTypeAdapterFactory<Leader> jsonToLeaderListAdapter = RuntimeTypeAdapterFactory.of(Leader.class);

        //Register here all the Leader types
        jsonToLeaderListAdapter.registerSubtype(DepositLeader.class);
        jsonToLeaderListAdapter.registerSubtype(MarketLeader.class);
        jsonToLeaderListAdapter.registerSubtype(ProductionLeader.class);
        jsonToLeaderListAdapter.registerSubtype(DevelopmentDiscountLeader.class);

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(jsonToLeaderListAdapter)
                .create();

        Leader[] leaders = deserialize(configPathString + "LeadersConfig.json", Leader[].class, gson);
        return (Arrays.asList(leaders));
    }

    public static FaithTrack faithTrackDeserialization(){
        return deserialize(configPathString+ "FaithTrackConfig.json", FaithTrack.class);
    }

    //helper method to load a 48 devcards array from json
    public static List<DevelopmentCard> devCardsListDeserialization() {
        DevelopmentCard[] cardsArray = deserialize(configPathString + "DevelopmentCardConfig.json", DevelopmentCard[].class);
        return (Arrays.asList(cardsArray));
    }

    public static Map<UUID, DevelopmentCard> devCardsMap() {
        return devCardsListDeserialization().stream().collect(Collectors.toMap(x -> UUID.randomUUID(), Function.identity()));
    }

    public static Map<UUID , DevelopmentCardAsset> devCardsAssets() {
        Map<UUID, DevelopmentCard> cardsFromJsonHandlerMap = devCardsMap();

        return cardsFromJsonHandlerMap.keySet().stream().map(id -> {
            DevelopmentCard card = cardsFromJsonHandlerMap.get(id);
            String cardPathSuffix = card.getCardType().toString() + "_" + card.getLevel() + ".png";
            String front = frontDevCardPathString + cardPathSuffix;
            String back = backDevCardPathString + cardPathSuffix;
            return new DevelopmentCardAsset(card, front, back, id);
        }).collect(Collectors.toMap(DevelopmentCardAsset::getCardId, Function.identity()));
    }

    public static Map<UUID , LeaderCardAsset> leaderCardsAssetsMap() {

        return IntStream.range(0, leaderCardsDeserialization().size()).boxed().map(index -> {

            Leader card = leaderCardsDeserialization().get(index);
            String cardPathSuffix = index.toString() + ".png";
            String front = frontLeaderCardPathString + cardPathSuffix;
            return new LeaderCardAsset(card, front, backLeaderCardPathString, UUID.randomUUID());

        }).collect(Collectors.toMap(LeaderCardAsset::getCardId, Function.identity()));

    }
}
