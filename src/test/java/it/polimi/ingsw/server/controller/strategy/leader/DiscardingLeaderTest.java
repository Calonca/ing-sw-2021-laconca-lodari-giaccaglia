package it.polimi.ingsw.server.controller.strategy.leader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.jsonutils.CommonGsonAdapters;
import it.polimi.ingsw.network.jsonutils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.controller.Match;
import it.polimi.ingsw.server.controller.strategy.ExecuteLeaderAction;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DiscardingLeaderTest {


    @Test
    public void executeSkipOnInitialDiscardLeaderWithRemaining() {



        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        Match match= new Match(1);
        GameModel gamemodel = new GameModel(players, true ,match, onlineUsers);

        Event clientEvent = new it.polimi.ingsw.network.messages.clienttoserver.events.InitialOrFinalPhaseEvent(3,gamemodel.getCurrentPlayer().getLeadersUUIDs().get(0));

        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.InitialOrFinalPhaseEvent.class, gson);

        //is idle for test
        gamemodel.setCurrentPlayerState(State.INITIAL_PHASE);
        assertEquals(new ExecuteLeaderAction().execute(gamemodel, serverEvent).getKey(), State.MIDDLE_PHASE);

    }

    @Test
    public void executeDiscardLeaderWithRemaining() {



        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        Match match= new Match(1);
        GameModel gamemodel = new GameModel(players, true ,match, onlineUsers);

        Event clientEvent = new it.polimi.ingsw.network.messages.clienttoserver.events.InitialOrFinalPhaseEvent(0,gamemodel.getCurrentPlayer().getLeadersUUIDs().get(0));

        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.InitialOrFinalPhaseEvent.class, gson);

        //is idle for test
        gamemodel.setCurrentPlayerState(State.INITIAL_PHASE);
        assertEquals(new ExecuteLeaderAction().execute(gamemodel, serverEvent).getKey(), State.INITIAL_PHASE);

    }


    @Test
    public void executeDiscardLeaderWithoutRemaining() {



        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        Match match= new Match(1);
        GameModel gamemodel = new GameModel(players, true ,match, onlineUsers);

        Event clientEvent = new it.polimi.ingsw.network.messages.clienttoserver.events.InitialOrFinalPhaseEvent(0,gamemodel.getCurrentPlayer().getLeadersUUIDs().get(0));


        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.InitialOrFinalPhaseEvent.class, gson);

        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(1)).orElseThrow().discard(gamemodel);
        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(2)).orElseThrow().discard(gamemodel);
        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(3)).orElseThrow().discard(gamemodel);

        //is idle for test
        gamemodel.setCurrentPlayerState(State.INITIAL_PHASE);
        assertEquals(new ExecuteLeaderAction().execute(gamemodel, serverEvent).getKey(), State.MIDDLE_PHASE);

    }



    @Test
    public void executeDiscardLeaderOnEnd() {



        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        Match match= new Match(1);
        GameModel gamemodel = new GameModel(players, true ,match, onlineUsers);

        Event clientEvent = new it.polimi.ingsw.network.messages.clienttoserver.events.InitialOrFinalPhaseEvent(0,gamemodel.getCurrentPlayer().getLeadersUUIDs().get(0));


        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.InitialOrFinalPhaseEvent.class, gson);


        //is idle for test
        gamemodel.setCurrentPlayerState(State.FINAL_PHASE);
        assertEquals(new ExecuteLeaderAction().execute(gamemodel, serverEvent).getKey(), State.FINAL_PHASE);

    }

    @Test
    public void executeDiscardLeaderOnEndWithoutRemaining() {



        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        Match match= new Match(1);
        GameModel gamemodel = new GameModel(players, true ,match, onlineUsers);

        Event clientEvent = new it.polimi.ingsw.network.messages.clienttoserver.events.InitialOrFinalPhaseEvent(0,gamemodel.getCurrentPlayer().getLeadersUUIDs().get(0));


        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.InitialOrFinalPhaseEvent.class, gson);

        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(1)).orElseThrow().discard(gamemodel);
        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(2)).orElseThrow().discard(gamemodel);
        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(3)).orElseThrow().discard(gamemodel);



        //is idle for test
        gamemodel.setCurrentPlayerState(State.FINAL_PHASE);
        assertEquals(new ExecuteLeaderAction().execute(gamemodel, serverEvent).getKey(), State.IDLE);

    }

    @Test
    public void executeDiscardLeaderOnEndWithoutRemainingAndGameEnd() {



        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        Match match= new Match(1);
        GameModel gamemodel = new GameModel(players, true ,match, onlineUsers);

        Event clientEvent = new it.polimi.ingsw.network.messages.clienttoserver.events.InitialOrFinalPhaseEvent(0,gamemodel.getCurrentPlayer().getLeadersUUIDs().get(0));


        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.InitialOrFinalPhaseEvent.class, gson);

        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(1)).orElseThrow().discard(gamemodel);
        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(2)).orElseThrow().discard(gamemodel);
        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(3)).orElseThrow().discard(gamemodel);
        gamemodel.setMacroGamePhase(GameModel.MacroGamePhase.ACTIVE_GAME);
        for(int i=0;i<24;i++)
            gamemodel.getCurrentPlayer().moveOnePosition();


        //is idle for test
        gamemodel.setCurrentPlayerState(State.FINAL_PHASE);
        assertEquals(new ExecuteLeaderAction().execute(gamemodel, serverEvent).getKey(), State.END_PHASE);

    }



    @Test
    public void executeDiscardLeaderWithoutRemainingAndGameEnd() {



        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        Match match= new Match(1);
        GameModel gamemodel = new GameModel(players, true ,match, onlineUsers);

        Event clientEvent = new it.polimi.ingsw.network.messages.clienttoserver.events.InitialOrFinalPhaseEvent(0,gamemodel.getCurrentPlayer().getLeadersUUIDs().get(0));


        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.InitialOrFinalPhaseEvent.class, gson);

        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(1)).orElseThrow().discard(gamemodel);
        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(2)).orElseThrow().discard(gamemodel);
        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(3)).orElseThrow().discard(gamemodel);
        gamemodel.setMacroGamePhase(GameModel.MacroGamePhase.ACTIVE_GAME);
        for(int i=0;i<24;i++)
            gamemodel.getCurrentPlayer().moveOnePosition();

        //is idle for test
        gamemodel.setCurrentPlayerState(State.INITIAL_PHASE);
        assertEquals(new ExecuteLeaderAction().execute(gamemodel, serverEvent).getKey(), State.END_PHASE);

    }


    @Test
    public void executeDiscardLeaderWithoutRemainingAndGameEndMulti() {



        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        players.put(1,"testPlayer2");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        Match match= new Match(2);
        GameModel gamemodel = new GameModel(players, false ,match, onlineUsers);

        Event clientEvent = new it.polimi.ingsw.network.messages.clienttoserver.events.InitialOrFinalPhaseEvent(0,gamemodel.getCurrentPlayer().getLeadersUUIDs().get(0));


        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.InitialOrFinalPhaseEvent.class, gson);

        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(1)).orElseThrow().discard(gamemodel);
        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(2)).orElseThrow().discard(gamemodel);
        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(3)).orElseThrow().discard(gamemodel);
        gamemodel.setMacroGamePhase(GameModel.MacroGamePhase.ACTIVE_GAME);
        for(int i=0;i<24;i++)
            gamemodel.getCurrentPlayer().moveOnePosition();

        //is idle for test
        gamemodel.setCurrentPlayerState(State.INITIAL_PHASE);
        assertEquals(new ExecuteLeaderAction().execute(gamemodel, serverEvent).getKey(), State.IDLE);

    }
}