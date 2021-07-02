package it.polimi.ingsw.server.controller.strategy.resourcemarket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.jsonUtils.CommonGsonAdapters;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.controller.Match;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class MovingResourceTest {
    @Test
    public void executeFakeMoveResource() {



        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        boolean isSinglePlayer = true;
        Match match= new Match(1);
        GameModel gamemodel = new GameModel(players, isSinglePlayer,match, onlineUsers);

        Event clientEvent = new it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.MoveResourceEvent(1,1);

        clientEvent.setPlayerNickname("testPlayer1");
        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.MoveResourceEvent.class, gson);
        gamemodel.getPlayer("testPlayer1").get();

        //is idle for test
        gamemodel.setCurrentPlayerState(State.IDLE);
        assertEquals(new MovingResource().execute(gamemodel, serverEvent).getKey(), State.IDLE);

    }

    @Test
    public void executeMoveResource() {



        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        boolean isSinglePlayer = true;
        Match match= new Match(1);
        GameModel gamemodel = new GameModel(players, isSinglePlayer,match, onlineUsers);

        Event clientEvent = new it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.MoveResourceEvent(1,2);

        clientEvent.setPlayerNickname("testPlayer1");
        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.MoveResourceEvent.class, gson);
        gamemodel.getPlayer("testPlayer1").get();

        gamemodel.getCurrentPlayer().getPersonalBoard().getWarehouseLeadersDepots().addResource(new Pair<Integer, Resource>(0,Resource.GOLD));
        //is idle for test
        gamemodel.setCurrentPlayerState(State.IDLE);
        assertEquals(new MovingResource().execute(gamemodel, serverEvent).getKey(), State.IDLE);

        assertEquals(gamemodel.getCurrentPlayer().getPersonalBoard().getWarehouseLeadersDepots().getResourceAt(0), Resource.GOLD);


    }
}