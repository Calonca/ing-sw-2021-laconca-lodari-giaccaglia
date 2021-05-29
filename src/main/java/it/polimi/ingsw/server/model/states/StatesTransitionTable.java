package it.polimi.ingsw.server.model.states;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.server.controller.strategy.*;
import it.polimi.ingsw.server.controller.strategy.cardmarket.*;
import it.polimi.ingsw.server.controller.strategy.leader.*;
import it.polimi.ingsw.server.controller.strategy.production.*;
import it.polimi.ingsw.server.controller.strategy.resourcemarket.*;
import it.polimi.ingsw.server.messages.clienttoserver.events.InitialOrFinalPhaseEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.MiddlePhaseEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.*;
import it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent.*;
import it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.*;
import it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.*;
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
                JsonUtility.configPathString + singlePLayerTableFile,
                StatesTransitionTable.class,
                jsonWithAdapter()
        );
    }

    public static StatesTransitionTable multiPlayer() {
        return JsonUtility.deserialize(
                JsonUtility.configPathString + multiPLayerTableFile,
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

    private static StatesTransitionTable setupCommonStatesTransitionTable(){

        StatesTransitionTable statesTransitionTable = new StatesTransitionTable();
        statesTransitionTable.table = new HashMap<>();
        HashMap<String, GameStrategy> eventsAndStrategy = new HashMap<>();

        // just an idea : player can freely move warehouse resources during idle phase
        //Idle Phase
        eventsAndStrategy.put(null, new IDLE());
        statesTransitionTable.table.put(State.IDLE, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();


        //Setup Phase
        eventsAndStrategy.put(name(SetupPhaseEvent.class), new Setup());
        statesTransitionTable.table.put(State.SETUP_PHASE, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();


        //Middle Phase
        eventsAndStrategy.put(name(MiddlePhaseEvent.class), new Middle());
        statesTransitionTable.table.put(State.MIDDLE_PHASE, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();


        //--- Activate the Production --//

        //Choosing Card For Production
        eventsAndStrategy.put(name(ChooseProductionAtPositionEvent.class), new TogglingForProduction());
        statesTransitionTable.table.put(State.CHOOSING_PRODUCTION, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();

        //Choosing Resource For Production
        eventsAndStrategy.put(name(ChooseResourcesForProductionEvent.class), new ChoosingResourceForProduction());
        statesTransitionTable.table.put(State.CHOOSING_RESOURCE_FOR_PRODUCTION, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();


        //--- Take Resources from Market --//

        //Choosing Market Line
        eventsAndStrategy.put(name(ChooseLineEvent.class),new PuttingBallOnLine());
        statesTransitionTable.table.put(State.CHOOSING_MARKET_LINE, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();

        //Choosing WhiteMarble Conversion
        eventsAndStrategy.put(name(ChooseWhiteMarbleConversionEvent.class), new TogglingForProduction());
        statesTransitionTable.table.put(State.CHOOSING_WHITEMARBLE_CONVERSION, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();

        //Choosing Position For Resources
        eventsAndStrategy.put(name(MoveResourceEvent.class), new AddingResourcesFromMarket());
        statesTransitionTable.table.put(State.CHOOSING_POSITION_FOR_RESOURCES, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();


        //--- Buy one Development Card --//

        //Choosing Development Card
        eventsAndStrategy.put(name(ChooseCardEvent.class), new AcquiringDevelopmentCard());
        statesTransitionTable.table.put(State.CHOOSING_DEVELOPMENT_CARD, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();

        //Choosing Resources For Development Card
        eventsAndStrategy.put(name(ChooseResourceForCardShopEvent.class), new PayingResourcesForDevelopmentCard());
        statesTransitionTable.table.put(State.CHOOSING_RESOURCES_FOR_DEVCARD, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();

        //Choosing Position For Development Card
        eventsAndStrategy.put(name(ChooseCardPositionEvent.class), new ChoosingSpaceForDevelopmentCard());
        statesTransitionTable.table.put(State.CHOOSING_POSITION_FOR_RESOURCES, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();


        //---Initial Leader Action --//

        eventsAndStrategy.put(name(InitialOrFinalPhaseEvent.class), new IDLE());
        eventsAndStrategy.put(name(PlayLeaderEvent.class),new ActivatingLeader());
        eventsAndStrategy.put(name(DiscardLeaderEvent.class),new DiscardingLeader());
        eventsAndStrategy.put(name(SkipLeaderEvent.class),new EndingLeaderPhase());
        statesTransitionTable.table.put(State.INITIAL_PHASE,eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();


        //--Final Leader Action --//

        eventsAndStrategy.put(name(InitialOrFinalPhaseEvent.class), new Final());
        eventsAndStrategy.put(name(PlayLeaderEvent.class),new ActivatingLeader());
        eventsAndStrategy.put(name(DiscardLeaderEvent.class),new DiscardingLeader());
        eventsAndStrategy.put(name(SkipLeaderEvent.class),new EndingLeaderPhase());
        statesTransitionTable.table.put(State.FINAL_PHASE,eventsAndStrategy);


        return statesTransitionTable;
    }

    private static void saveSinglePlayerStatesTransitionTable () {

        JsonUtility.serialize(
                        JsonUtility.configPathString + singlePLayerTableFile,
                                setupCommonStatesTransitionTable(),
                                StatesTransitionTable.class,
                                jsonWithAdapter()
        );

    }


    private static void saveMultiPlayerStatesTransitionTable() {

        JsonUtility.serialize(
                        JsonUtility.configPathString + multiPLayerTableFile,
                                setupCommonStatesTransitionTable(),
                                StatesTransitionTable.class,
                                jsonWithAdapter()
        );

    }

    private static Gson jsonWithAdapter(){

        RuntimeTypeAdapterFactory<GameStrategy> strategyAdapter = RuntimeTypeAdapterFactory.of(GameStrategy.class);

        strategyAdapter.registerSubtype(IDLE.class);
        strategyAdapter.registerSubtype(Initial.class);
        strategyAdapter.registerSubtype(Middle.class);
        strategyAdapter.registerSubtype(Final.class);
        strategyAdapter.registerSubtype(ActivatingLeader.class);
        strategyAdapter.registerSubtype(DiscardingLeader.class);
        strategyAdapter.registerSubtype(EndingLeaderPhase.class);
        strategyAdapter.registerSubtype(AcquiringDevelopmentCard.class);
        strategyAdapter.registerSubtype(ChoosingSpaceForDevelopmentCard.class);
        strategyAdapter.registerSubtype(PayingResourcesForDevelopmentCard.class);
        strategyAdapter.registerSubtype(ChoosingResourceForProduction.class);
        strategyAdapter.registerSubtype(AddingResourcesFromMarket.class);
        strategyAdapter.registerSubtype(ChoosingMarketBonus.class);
        strategyAdapter.registerSubtype(DiscardingResources.class);
        strategyAdapter.registerSubtype(PuttingBallOnLine.class);
        strategyAdapter.registerSubtype(TogglingForProduction.class);
        strategyAdapter.registerSubtype(Setup.class);

        return new GsonBuilder()
                .registerTypeAdapterFactory(strategyAdapter).setPrettyPrinting()
                .create();
    }

}
