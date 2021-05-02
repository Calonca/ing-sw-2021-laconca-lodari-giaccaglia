package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.server.controller.strategy.*;
import it.polimi.ingsw.server.controller.strategy.leader.ActivateLeader;
import it.polimi.ingsw.server.controller.strategy.leader.DiscardLeader;
import it.polimi.ingsw.server.controller.strategy.leader.EndLeaders;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent.DiscardLeaderEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent.PlayLeaderEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent.SkipLeaderEvent;
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
        return jsonUtility.deserialize(
                jsonUtility.configPathString+ singlePLayerTableFile,
                StatesTransitionTable.class,
                jsonWithAdapter()
        );
    }

    public static StatesTransitionTable multiPlayer() {
        return jsonUtility.deserialize(
                jsonUtility.configPathString+ multiPLayerTableFile,
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
        eventsAndStrategy.put(name(SkipLeaderEvent.class),new EndLeaders());
        eventsAndStrategy.put(name(PlayLeaderEvent.class),new ActivateLeader());
        eventsAndStrategy.put(name(DiscardLeaderEvent.class),new DiscardLeader());
        statesTransitionTable.table.put(State.SHOWING_LEADERS_INITIAL,eventsAndStrategy);
        //Todo add other states

        jsonUtility.serialize(jsonUtility.configPathString+singlePLayerTableFile,
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

        eventsAndStrategy = new HashMap<>();
        eventsAndStrategy.put(name(SkipLeaderEvent.class),new EndLeaders());
        eventsAndStrategy.put(name(PlayLeaderEvent.class),new ActivateLeader());
        eventsAndStrategy.put(name(DiscardLeaderEvent.class),new DiscardLeader());
        statesTransitionTable.table.put(State.SHOWING_LEADERS_INITIAL,eventsAndStrategy);
        //Todo add other states

        jsonUtility.serialize(jsonUtility.configPathString+multiPLayerTableFile,
                statesTransitionTable,
                StatesTransitionTable.class,
                jsonWithAdapter());
    }

    private static Gson jsonWithAdapter(){
        RuntimeTypeAdapterFactory<GameStrategy> strategyAdapter = RuntimeTypeAdapterFactory.of(GameStrategy.class);

        strategyAdapter.registerSubtype(Initial.class);
        strategyAdapter.registerSubtype(Middle.class);
        strategyAdapter.registerSubtype(Final.class);
        strategyAdapter.registerSubtype(EndLeaders.class);
        strategyAdapter.registerSubtype(DiscardLeader.class);
        strategyAdapter.registerSubtype(ActivateLeader.class);
        strategyAdapter.registerSubtype(ChooseInitialResource.class);
        //Todo add all strategies

        return new GsonBuilder()
                .registerTypeAdapterFactory(strategyAdapter)
                .create();
    }

}
