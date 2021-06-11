package it.polimi.ingsw.server.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.leaders.*;
import it.polimi.ingsw.network.jsonUtils.UUIDTypeAdapter;
import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.server.model.market.MarketBoard;
import it.polimi.ingsw.server.model.player.leaders.*;
import it.polimi.ingsw.server.model.player.track.FaithTrack;
import it.polimi.ingsw.server.model.states.StatesTransitionTable;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



public class Deserializator extends JsonUtility {

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();

    public static MarketBoard marketBoardDeserialization(){
        return deserialize(readConfigPathString + "MarketBoardConfig.json", MarketBoard.class);
    }

    public static CardShop cardShopDeserialization(){
        return deserialize(readConfigPathString + "CardShopConfig.json", CardShop.class, gson);
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

        RuntimeTypeAdapterFactory<Leader> jsonToLeaderListAdapter = RuntimeTypeAdapterFactory.of(Leader.class, "type");

        //Register here all the Leader types
        jsonToLeaderListAdapter.registerSubtype(DepositLeader.class , DepositLeader.class.getName());
        jsonToLeaderListAdapter.registerSubtype(MarketLeader.class, MarketLeader.class.getName());
        jsonToLeaderListAdapter.registerSubtype(ProductionLeader.class, ProductionLeader.class.getName());
        jsonToLeaderListAdapter.registerSubtype(DevelopmentDiscountLeader.class, DevelopmentDiscountLeader.class.getName());

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(jsonToLeaderListAdapter)
                .create();

        Leader[] leaders = deserialize(readConfigPathString + "LeadersConfig.json", Leader[].class, gson);
        return (Arrays.asList(leaders));

    }

    public static Map<UUID, NetworkLeaderCard> networkLeaderCardsDeserialization(){


        RuntimeTypeAdapterFactory<NetworkLeaderCard> jsonToNetworkLeaderListAdapter = RuntimeTypeAdapterFactory.of(NetworkLeaderCard.class);

        jsonToNetworkLeaderListAdapter.registerSubtype(NetworkDepositLeaderCard.class);
        jsonToNetworkLeaderListAdapter.registerSubtype(NetworkMarketLeaderCard.class);
        jsonToNetworkLeaderListAdapter.registerSubtype(NetworkProductionLeaderCard.class);
        jsonToNetworkLeaderListAdapter.registerSubtype(NetworkDevelopmentDiscountLeaderCard.class);

        Map<UUID, Leader> modelLeadersMap = leadersCardMapDeserialization();

        return modelLeadersMap.keySet().stream().collect(Collectors.toMap(leaderKey -> leaderKey, leaderKey -> {

            NetworkLeaderCard networkLeaderCard = null;

            Leader leader = modelLeadersMap.get(leaderKey);

            if(leader instanceof DepositLeader){
                String jsonString = Serializator.serialize(leader);
                NetworkDepositLeaderCard depositLeaderCard = Deserializator.deserializeFromString(jsonString, NetworkDepositLeaderCard.class, gson);
                depositLeaderCard.setResourcesTypeInDepot(((DepositLeader) leader).getDepotResourcesType());



                return depositLeaderCard;


            }

            if(leader instanceof MarketLeader){

                String jsonString = Serializator.serialize(leader);
                NetworkMarketLeaderCard marketLeaderCard = Deserializator.deserializeFromString(jsonString, NetworkMarketLeaderCard.class, gson);
                marketLeaderCard.setMarketBonusResource(((MarketLeader) leader).getResourceBonusType());


                return marketLeaderCard;

            }

            if(leader instanceof ProductionLeader){

                String jsonString = Serializator.serialize(leader);
                NetworkProductionLeaderCard productionLeaderCard = Deserializator.deserializeFromString(jsonString, NetworkProductionLeaderCard.class, gson);
                productionLeaderCard.setProductionInputResources(((ProductionLeader) leader).getProductionInputs());
                productionLeaderCard.setProductionOutputResources(((ProductionLeader) leader).getProductionOutputs());


                return productionLeaderCard;

            }

            if(leader instanceof DevelopmentDiscountLeader){
                String jsonString = Serializator.serialize(leader);
                NetworkDevelopmentDiscountLeaderCard developmentDiscountLeaderCard= Deserializator.deserializeFromString(jsonString, NetworkDevelopmentDiscountLeaderCard.class, gson);
                developmentDiscountLeaderCard.setResourcesDiscount(((DevelopmentDiscountLeader) leader).getDiscountAsIntegerPair());


                return developmentDiscountLeaderCard;
            }

            return networkLeaderCard;

        }));

    }

    public static Map<UUID, Leader> leadersCardMapBuilder(){
        List<Leader> leaderList = leaderCardsDeserialization();
        return IntStream.range(0, Deserializator.leaderCardsDeserialization().size()).boxed().collect(Collectors.toMap(index ->
                UUID.nameUUIDFromBytes(leaderList.get(index).toString().getBytes(StandardCharsets.UTF_8)), leaderList::get));
    }



    public static FaithTrack faithTrackDeserialization(){
        return deserialize(readConfigPathString + "FaithTrackConfig.json", FaithTrack.class);
    }

    //helper method to load a 48 devcards array from json
    public static List<DevelopmentCard> devCardsListDeserialization() {
        DevelopmentCard[] cardsArray = deserialize(readConfigPathString + "DevelopmentCardConfig.json", DevelopmentCard[].class, gson);
        return (Arrays.asList(cardsArray));
    }

    public static List<NetworkDevelopmentCard> networkDevCardsListDeserialization(){
        NetworkDevelopmentCard[] cardsArray = deserialize(readConfigPathString + "cardsfixed.json",
                NetworkDevelopmentCard[].class, gson);
        return (Arrays.asList(cardsArray));
    }

    public static Map<UUID, NetworkDevelopmentCard> networkDevCardsMap() {
        return networkDevCardsListDeserialization().stream().collect(Collectors.toMap(NetworkDevelopmentCard::getCardId, Function.identity()));
    }

    public static Map<UUID, DevelopmentCard> devCardsMap(){
        return devCardsListDeserialization().stream().collect(Collectors.toMap(DevelopmentCard::getCardId, Function.identity()));
    }

    public static Map<UUID, Leader> leadersCardMapDeserialization(){

        RuntimeTypeAdapterFactory<Leader> jsonToLeaderListAdapter = RuntimeTypeAdapterFactory.of(Leader.class, "type");

        //Register here all the Leader types
        jsonToLeaderListAdapter.registerSubtype(DepositLeader.class , DepositLeader.class.getName());
        jsonToLeaderListAdapter.registerSubtype(MarketLeader.class, MarketLeader.class.getName());
        jsonToLeaderListAdapter.registerSubtype(ProductionLeader.class, ProductionLeader.class.getName());
        jsonToLeaderListAdapter.registerSubtype(DevelopmentDiscountLeader.class, DevelopmentDiscountLeader.class.getName());

        Gson customGson = new GsonBuilder()
                .registerTypeAdapterFactory(jsonToLeaderListAdapter)
                .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
                .registerTypeHierarchyAdapter(Path.class, new PathConverter())
                .enableComplexMapKeySerialization()
                .setPrettyPrinting().create();

        Type type = new TypeToken<Map<UUID, Leader> >(){}.getType();
        return deserialize(readConfigPathString + "LeadersCardMapConfig.json", type ,customGson);

    }

    public static StatesTransitionTable deserializeSinglePlayerStatesTransitionTable() {
        return JsonUtility.deserialize(
                JsonUtility.readConfigPathString + StatesTransitionTable.singlePLayerTableFile,
                StatesTransitionTable.class,
                StatesTransitionTable.jsonWithAdapter()
        );
    }

    public static StatesTransitionTable deserializeMultiPlayerStatesTransitionTable() {
        return JsonUtility.deserialize(
                JsonUtility.readConfigPathString + StatesTransitionTable.multiPLayerTableFile,
                StatesTransitionTable.class,
                StatesTransitionTable.jsonWithAdapter()
        );
    }

}
