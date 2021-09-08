package it.polimi.ingsw.server.controller.strategy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.jsonutils.CommonGsonAdapters;
import it.polimi.ingsw.network.jsonutils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.clienttoserver.events.MiddlePhaseEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class IDLETest {

    @Test
    public void executeToEND() {

        Event clientEvent = new MiddlePhaseEvent(0);
        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.MiddlePhaseEvent.class, gson);
        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        GameModel gamemodel = new GameModel(players, true ,null, onlineUsers);

        gamemodel.setMacroGamePhase(GameModel.MacroGamePhase.LAST_TURN);
        assertEquals(new IDLE().execute(gamemodel, serverEvent).getKey(), State.END_PHASE);

    }

    @Test
    public void executeToINITIAL() {

        Event clientEvent = new MiddlePhaseEvent(0);
        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.MiddlePhaseEvent.class, gson);
        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        GameModel gamemodel = new GameModel(players, true ,null, onlineUsers);

        gamemodel.setMacroGamePhase(GameModel.MacroGamePhase.GAME_ENDED);
        assertEquals(new IDLE().execute(gamemodel, serverEvent).getKey(), State.INITIAL_PHASE);
    }


    @Test
    public void executeToMIDDLE() {

        Event clientEvent = new MiddlePhaseEvent(0);
        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.MiddlePhaseEvent.class, gson);
        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        GameModel gamemodel = new GameModel(players, true ,null, onlineUsers);

        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(0)).orElseThrow().discard(gamemodel);
        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(1)).orElseThrow().discard(gamemodel);
        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(2)).orElseThrow().discard(gamemodel);
        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(3)).orElseThrow().discard(gamemodel);

        gamemodel.setMacroGamePhase(GameModel.MacroGamePhase.ACTIVE_GAME);
        assertEquals(new IDLE().execute(gamemodel, serverEvent).getKey(), State.MIDDLE_PHASE);

    }
}