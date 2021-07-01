package it.polimi.ingsw.server.controller.strategy.production;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.jsonUtils.CommonGsonAdapters;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.controller.Match;
import it.polimi.ingsw.server.controller.strategy.leader.ActivatingLeader;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class FinalProductionStrategyTest {

    @Test
    public void executeToFINAL() {


        Event clientEvent = new it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.FinalProductionPhaseEvent(0);

        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.FinalProductionPhaseEvent.class, gson);
        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        boolean isSinglePlayer = true;
        Match match= new Match(1);
        GameModel gamemodel = new GameModel(players, isSinglePlayer,match, onlineUsers);

        assertEquals(new FinalProductionStrategy().execute(gamemodel, serverEvent).getKey(), State.FINAL_PHASE);

    }

    @Test
    public void executeToMIDDLE() {


        Event clientEvent = new it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.FinalProductionPhaseEvent(1);

        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.FinalProductionPhaseEvent.class, gson);
        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        boolean isSinglePlayer = true;
        Match match= new Match(1);
        GameModel gamemodel = new GameModel(players, isSinglePlayer,match, onlineUsers);

        assertEquals(new FinalProductionStrategy().execute(gamemodel, serverEvent).getKey(), State.MIDDLE_PHASE);

    }
}