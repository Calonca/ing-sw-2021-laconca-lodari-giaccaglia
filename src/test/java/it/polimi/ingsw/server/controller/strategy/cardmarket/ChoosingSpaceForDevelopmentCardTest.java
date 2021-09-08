package it.polimi.ingsw.server.controller.strategy.cardmarket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.jsonutils.CommonGsonAdapters;
import it.polimi.ingsw.network.jsonutils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseCardPositionEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.states.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ChoosingSpaceForDevelopmentCardTest {



    @Test
    public void executeToFinal()
    {

        Event clientEvent = new ChooseCardPositionEvent(2);
        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardPositionEvent.class, gson);

        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        GameModel gamemodel = new GameModel(players, true ,null, onlineUsers);

        gamemodel.getCardShop().purchaseCard(DevelopmentCardColor.BLUE,1);
        assertEquals(State.FINAL_PHASE, new ChoosingSpaceForDevelopmentCard().execute(gamemodel, serverEvent).getKey());

    }

    @Test
    public void executeToIDLE()
    {

        Event clientEvent = new ChooseCardPositionEvent(2);
        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardPositionEvent.class, gson);

        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        GameModel gamemodel = new GameModel(players, true ,null, onlineUsers);

        int index = 0;
        gamemodel.getCardShop().purchaseCard(DevelopmentCardColor.BLUE,1);

        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(index)).orElseThrow().discard(gamemodel);
        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(1)).orElseThrow().discard(gamemodel);
        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(2)).orElseThrow().discard(gamemodel);
        gamemodel.getCurrentPlayer().getLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(3)).orElseThrow().discard(gamemodel);


        assertEquals(State.IDLE, new ChoosingSpaceForDevelopmentCard().execute(gamemodel, serverEvent).getKey());

    }
}