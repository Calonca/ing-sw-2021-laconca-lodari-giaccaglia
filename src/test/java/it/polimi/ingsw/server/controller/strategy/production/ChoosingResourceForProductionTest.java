package it.polimi.ingsw.server.controller.strategy.production;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.jsonutils.CommonGsonAdapters;
import it.polimi.ingsw.network.jsonutils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.controller.Match;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.states.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ChoosingResourceForProductionTest {

    @Test
    public void executeNoResource() {


        Event clientEvent = new it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ChooseResourcesForProductionEvent(new ArrayList<>(), new ArrayList<>());

        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.ChooseResourcesForProductionEvent.class, gson);
        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        Match match= new Match(1);
        GameModel gamemodel = new GameModel(players, true ,match, onlineUsers);

        assertEquals(new ChoosingResourceForProduction().execute(gamemodel, serverEvent).getKey(), State.CHOOSING_PRODUCTION);

    }


    @Test
    public void executeInputResource() {


        List<Integer> input= new ArrayList<>();
        input.add(-8);
        input.add(-7);
        input.add(-6);
        input.add(-5);
        Event clientEvent = new it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ChooseResourcesForProductionEvent(input, new ArrayList<>());

        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.ChooseResourcesForProductionEvent.class, gson);
        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        Match match= new Match(1);
        GameModel gamemodel = new GameModel(players, true ,match, onlineUsers);

        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(new int[]{10,10,10,10});


        assertEquals(new ChoosingResourceForProduction().execute(gamemodel, serverEvent).getKey(), State.CHOOSING_PRODUCTION);
        assertEquals(gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().getNSelected(Resource.GOLD),1);

    }
}