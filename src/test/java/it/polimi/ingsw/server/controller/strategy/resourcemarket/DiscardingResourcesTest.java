package it.polimi.ingsw.server.controller.strategy.resourcemarket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.jsonutils.CommonGsonAdapters;
import it.polimi.ingsw.network.jsonutils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.controller.Match;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DiscardingResourcesTest {

    @Test
    public void executeDiscard() {



        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        players.put(1,"testPlayer2");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        Match match= new Match(2);
        GameModel gamemodel = new GameModel(players, false ,match, onlineUsers);

        Event clientEvent = new it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.DiscardResourcesEvent();


        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.DiscardResourcesEvent.class, gson);


        //is idle for test
        gamemodel.setCurrentPlayerState(State.INITIAL_PHASE);
        assertEquals(new DiscardingResources().execute(gamemodel, serverEvent).getKey(), State.FINAL_PHASE);

    }


    @Test
    public void executeDiscardWithoutLeaders() {



        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        players.put(1,"testPlayer2");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        Match match= new Match(2);
        GameModel gamemodel = new GameModel(players, false ,match, onlineUsers);

        Event clientEvent = new it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.DiscardResourcesEvent();


        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.DiscardResourcesEvent.class, gson);


        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(1)).orElseThrow().discard(gamemodel);
        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(2)).orElseThrow().discard(gamemodel);
        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(3)).orElseThrow().discard(gamemodel);
        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(0)).orElseThrow().discard(gamemodel);

        //is idle for test
        gamemodel.setCurrentPlayerState(State.INITIAL_PHASE);
        assertEquals(new DiscardingResources().execute(gamemodel, serverEvent).getKey(), State.IDLE);

    }


}