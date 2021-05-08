package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.server.model.utils.JsonUtility;
import it.polimi.ingsw.server.controller.strategy.*;
import it.polimi.ingsw.server.controller.strategy.cardmarket.AcquiringDevelopmentCard;
import it.polimi.ingsw.server.controller.strategy.cardmarket.ChoosingSpaceForDevelopmentCard;
import it.polimi.ingsw.server.controller.strategy.cardmarket.PayingResourcesForDevelopmentCard;
import it.polimi.ingsw.server.controller.strategy.cardmarket.ShowingDevelopmentCardsMarket;
import it.polimi.ingsw.server.controller.strategy.leader.ActivatingLeader;
import it.polimi.ingsw.server.controller.strategy.leader.DiscardingLeader;
import it.polimi.ingsw.server.controller.strategy.leader.EndingLeaderPhase;
import it.polimi.ingsw.server.controller.strategy.production.ChoosingResourceForProduction;
import it.polimi.ingsw.server.controller.strategy.production.ShowingProductionCards;
import it.polimi.ingsw.server.controller.strategy.production.TogglingForProduction;
import it.polimi.ingsw.server.controller.strategy.resourcemarket.*;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.CardShopEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardPositionEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseResourceForCardShopEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent.DiscardLeaderEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent.PlayLeaderEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent.SkipLeaderEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.*;
import it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.ChooseProductionAtPositionEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.ChooseResourcesForProductionEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.FinalProductionPhase;
import it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.ProductionEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;

import java.util.HashMap;
import java.util.Map;


public class StatesTransitionTable {
    /**
     * The String is the name of the {@link Validable}
     */
    private Map<State, Map<String, GameStrategy>> table;
    private transient static final String singlePLayerTableFile = "SinglePlayerStatesTransitionTable.json";
    private transient static final String multiPLayerTableFile = "MultiPlayerStatesTransitionTable.json";

    /**
     * Returns name of a class
     */
    private static String name(Class classClass){
        return classClass.getSimpleName();
    }

    public static StatesTransitionTable singlePlayer() {
        return JsonUtility.deserialize(
                JsonUtility.configPathString+ singlePLayerTableFile,
                StatesTransitionTable.class,
                jsonWithAdapter()
        );
    }

    public static StatesTransitionTable multiPlayer() {
        return JsonUtility.deserialize(
                JsonUtility.configPathString+ multiPLayerTableFile,
                StatesTransitionTable.class,
                jsonWithAdapter()
        );
    }

    /**
     * Returns the {@link GameStrategy} to use at the given {@link State} after the given {@link Validable event}.
     */
    public GameStrategy getStrategy(State state, Validable event){
        return table.get(state).get(name(event.getClass()));
    }

    /**
     * Returns the single player table when the game is single player and the multiplayer table when it is multiplayer<br>
     * @param isSinglePlayer a boolean indicating if the game is single player.
     */
    public static StatesTransitionTable fromIsSinglePlayer(boolean isSinglePlayer) {
        return isSinglePlayer?StatesTransitionTable.singlePlayer():StatesTransitionTable.multiPlayer();
    }

    public static void saveTables(){
        saveMultiPlayerStatesTransitionTable();
        saveSinglePlayerStatesTransitionTable();
    }

    private static void saveSinglePlayerStatesTransitionTable () {
        StatesTransitionTable statesTransitionTable = new StatesTransitionTable();
        statesTransitionTable.table = new HashMap<>();

        HashMap<String, GameStrategy> eventsAndStrategy = new HashMap<>();
        eventsAndStrategy.put(name(SetupPhaseEvent.class),new ChooseInitialResource());
        statesTransitionTable.table.put(State.SETUP_PHASE,eventsAndStrategy);

        eventsAndStrategy = new HashMap<>();
        eventsAndStrategy.put(name(SkipLeaderEvent.class),new EndingLeaderPhase());
        eventsAndStrategy.put(name(PlayLeaderEvent.class),new ActivatingLeader());
        eventsAndStrategy.put(name(DiscardLeaderEvent.class),new DiscardingLeader());
        statesTransitionTable.table.put(State.SHOWING_LEADERS_INITIAL,eventsAndStrategy);
        //Todo add other states

        JsonUtility.serialize(JsonUtility.configPathString+singlePLayerTableFile,
                statesTransitionTable,
                StatesTransitionTable.class,
                jsonWithAdapter());
    }

    private static void saveMultiPlayerStatesTransitionTable() {
        StatesTransitionTable statesTransitionTable = new StatesTransitionTable();
        statesTransitionTable.table = new HashMap<>();



        HashMap<String, GameStrategy> eventsAndStrategy = new HashMap<>();
        eventsAndStrategy.put(name(SetupPhaseEvent.class),new ChooseInitialResource());
        statesTransitionTable.table.put(State.SETUP_PHASE,eventsAndStrategy);


        //Middle Phase
        eventsAndStrategy = new HashMap<>();
        eventsAndStrategy.put(name(CardShopEvent.class),new ShowingDevelopmentCardsMarket());
        eventsAndStrategy.put(name(ProductionEvent.class),new ShowingProductionCards());
        eventsAndStrategy.put(name(MarketBoardEvent.class),new ShowingResourceMarket());
        statesTransitionTable.table.put(State.MIDDLE_PHASE,eventsAndStrategy);

        //CARDSHOP
        eventsAndStrategy = new HashMap<>();
        eventsAndStrategy.put(name(ChooseCardEvent.class),new AcquiringDevelopmentCard());
        statesTransitionTable.table.put(State.SHOWING_CARD_SHOP,eventsAndStrategy);


        eventsAndStrategy = new HashMap<>();
        eventsAndStrategy.put(name(ChooseCardPositionEvent.class),new ChoosingSpaceForDevelopmentCard());
        statesTransitionTable.table.put(State.CHOOSING_POSITION_FOR_DEVCARD,eventsAndStrategy);

        eventsAndStrategy = new HashMap<>();
        eventsAndStrategy.put(name(ChooseResourceForCardShopEvent.class),new PayingResourcesForDevelopmentCard());
        statesTransitionTable.table.put(State.CHOOSING_RESOURCES_FOR_DEVCARD,eventsAndStrategy);

        //PRODUCTION

        eventsAndStrategy = new HashMap<>();
        eventsAndStrategy.put(name(ChooseProductionAtPositionEvent.class),new TogglingForProduction());
        eventsAndStrategy.put(name(FinalProductionPhase.class),new TogglingForProduction());
        statesTransitionTable.table.put(State.CHOOSING_CARD_FOR_PRODUCTION,eventsAndStrategy);

        eventsAndStrategy = new HashMap<>();
        eventsAndStrategy.put(name(ChooseResourcesForProductionEvent.class),new ChoosingResourceForProduction());
        statesTransitionTable.table.put(State.CHOOSING_RESOURCE_FOR_PRODUCTION,eventsAndStrategy);


        //manca strategy per selezionare?
        eventsAndStrategy = new HashMap<>();
        eventsAndStrategy.put(name(PlayLeaderEvent.class),new ActivatingLeader());
        eventsAndStrategy.put(name(DiscardLeaderEvent.class),new DiscardingLeader());
        eventsAndStrategy.put(name(SkipLeaderEvent.class),new EndingLeaderPhase());
        statesTransitionTable.table.put(State.SHOWING_LEADERS_INITIAL,eventsAndStrategy);

        eventsAndStrategy = new HashMap<>();
        eventsAndStrategy.put(name(PlayLeaderEvent.class),new ActivatingLeader());
        eventsAndStrategy.put(name(DiscardLeaderEvent.class),new DiscardingLeader());
        eventsAndStrategy.put(name(SkipLeaderEvent.class),new EndingLeaderPhase());
        statesTransitionTable.table.put(State.SHOWING_LEADERS_FINAL,eventsAndStrategy);

        ///MARKET

        eventsAndStrategy = new HashMap<>();
        eventsAndStrategy.put(name(ChooseLineEvent.class),new PuttingBallOnLine());
        statesTransitionTable.table.put(State.SHOWING_MARKET_RESOURCES,eventsAndStrategy);


        eventsAndStrategy = new HashMap<>();
        eventsAndStrategy.put(name(ChooseWhiteMarbleConversionEvent.class),new ChoosingMarketBonus());
        statesTransitionTable.table.put(State.CHOOSING_WHITEMARBLE_CONVERSION,eventsAndStrategy);

        eventsAndStrategy = new HashMap<>();
        eventsAndStrategy.put(name(MoveResourceEvent.class),new AddingResourcesFromMarket());
        eventsAndStrategy.put(name(DiscardResourcesEvent.class),new DiscardingResources());
        statesTransitionTable.table.put(State.CHOOSING_POSITION_FOR_RESOURCES,eventsAndStrategy);






        //Todo add other states

        JsonUtility.serialize(JsonUtility.configPathString+multiPLayerTableFile,
                statesTransitionTable,
                StatesTransitionTable.class,
                jsonWithAdapter());
    }

    private static Gson jsonWithAdapter(){
        RuntimeTypeAdapterFactory<GameStrategy> strategyAdapter = RuntimeTypeAdapterFactory.of(GameStrategy.class);

        strategyAdapter.registerSubtype(Initial.class);
        strategyAdapter.registerSubtype(Middle.class);
        strategyAdapter.registerSubtype(Final.class);
        strategyAdapter.registerSubtype(ActivatingLeader.class);
        strategyAdapter.registerSubtype(DiscardingLeader.class);
        strategyAdapter.registerSubtype(EndingLeaderPhase.class);
        strategyAdapter.registerSubtype(AcquiringDevelopmentCard.class);
        strategyAdapter.registerSubtype(ChoosingSpaceForDevelopmentCard.class);
        strategyAdapter.registerSubtype(PayingResourcesForDevelopmentCard.class);
        strategyAdapter.registerSubtype(ShowingDevelopmentCardsMarket.class);
        strategyAdapter.registerSubtype(ChoosingResourceForProduction.class);
        strategyAdapter.registerSubtype(ShowingProductionCards.class);
        strategyAdapter.registerSubtype(AddingResourcesFromMarket.class);
        strategyAdapter.registerSubtype(ChoosingMarketBonus.class);
        strategyAdapter.registerSubtype(DiscardingResources.class);
        strategyAdapter.registerSubtype(PuttingBallOnLine.class);
        strategyAdapter.registerSubtype(ShowingResourceMarket.class);
        strategyAdapter.registerSubtype(TogglingForProduction.class);
        strategyAdapter.registerSubtype(ChooseInitialResource.class);

        return new GsonBuilder()
                .registerTypeAdapterFactory(strategyAdapter).setPrettyPrinting()
                .create();
    }

}
