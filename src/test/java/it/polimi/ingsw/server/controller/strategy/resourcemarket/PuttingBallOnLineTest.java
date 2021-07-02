package it.polimi.ingsw.server.controller.strategy.resourcemarket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.jsonUtils.CommonGsonAdapters;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.ChooseLineEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.ChooseWhiteMarbleConversionEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.market.MarketLine;
import it.polimi.ingsw.server.model.states.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class PuttingBallOnLineTest {

    boolean oneMarbleWay=false;
    boolean twoMarblesWay=false;
    @Test
    public void executeNormally() {

        Event clientEvent = new ChooseLineEvent(1);
        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.ChooseLineEvent.class, gson);
        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        boolean isSinglePlayer = true;
        GameModel gamemodel = new GameModel(players, isSinglePlayer,null, onlineUsers);



        assertEquals(new PuttingBallOnLine().execute(gamemodel, serverEvent).getKey(), State.CHOOSING_POSITION_FOR_RESOURCES);
    }



    public void executeWithLeaders() {

        Event clientEvent = new ChooseLineEvent(1);
        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.ChooseLineEvent.class, gson);
        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());


        boolean isSinglePlayer = true;
        GameModel gamemodel = new GameModel(players, isSinglePlayer,null, onlineUsers);

        gamemodel.getCurrentPlayer().applyMarketBonus(Resource.GOLD);
        gamemodel.getCurrentPlayer().applyMarketBonus(Resource.SERVANT);

        State state= new PuttingBallOnLine().execute(gamemodel, serverEvent).getKey();
        if(gamemodel.areThereWhiteMarblesInPickedLine())
        {
            assertEquals(state, State.CHOOSING_WHITEMARBLE_CONVERSION);
            twoMarblesWay=true;
        }
        else
        {
            assertEquals(state, State.CHOOSING_POSITION_FOR_RESOURCES);
            oneMarbleWay=true;

        }

    }


    @Test
    public void executeRandom() {

        do {
            executeWithLeaders();
        }while (!(oneMarbleWay&&twoMarblesWay));
    }
}