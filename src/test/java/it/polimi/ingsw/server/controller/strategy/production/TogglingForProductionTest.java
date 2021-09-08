package it.polimi.ingsw.server.controller.strategy.production;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.jsonutils.CommonGsonAdapters;
import it.polimi.ingsw.network.jsonutils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ToggleProductionAtPosition;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TogglingForProductionTest {

    @Test
    public void executeToCHOOSING() {

        Event clientEvent = new ToggleProductionAtPosition(2);
        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.ToggleProductionAtPosition.class, gson);
        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        GameModel gamemodel = new GameModel(players, true ,null, onlineUsers);

        assertEquals(new TogglingForProduction().execute(gamemodel, serverEvent).getKey(), State.CHOOSING_PRODUCTION);
        System.out.println(gamemodel.getCurrentPlayer().getPersonalBoard().getLastSelectedProductionPosition());

    }

    @Test
    public void executeToRESOURCE() {

        Event clientEvent = new ToggleProductionAtPosition(-1);
        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.ToggleProductionAtPosition.class, gson);
        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        GameModel gamemodel = new GameModel(players, true ,null, onlineUsers);

        assertEquals(new TogglingForProduction().execute(gamemodel, serverEvent).getKey(), State.CHOOSING_RESOURCE_FOR_PRODUCTION);

    }

}