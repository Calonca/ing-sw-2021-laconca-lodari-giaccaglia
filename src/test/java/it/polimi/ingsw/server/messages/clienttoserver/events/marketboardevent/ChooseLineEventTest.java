package it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent;

import com.google.gson.Gson;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.ChooseLineEvent;
import it.polimi.ingsw.server.model.GameModel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.network.jsonUtils.JsonUtility.deserializeFromString;
import static it.polimi.ingsw.network.jsonUtils.JsonUtility.serialize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ChooseLineEventTest {

    ChooseLineEvent clientEventTest;
    it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.ChooseLineEvent serverEventTest;
    GameModel gameModelTest;
    String serializedEvent;
    Gson gson;

    @Before
    public void setUp() throws Exception {
        initializeGameModel();
        gameModelTest.setCurrentPlayer(gameModelTest.getPlayer("testPlayer4").get());
        gson = new Gson();
    }

    private void initializeGameModel(){
        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        players.put(1,"testPlayer2");
        players.put(2,"testPlayer3");
        players.put(3,"testPlayer4");

        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        gameModelTest = new GameModel(players, false, null, onlineUsers);
        gameModelTest.start();
    }


    @Test
    public void validationOkTest(){
        validTestInitialization();
        serializedEvent = serialize(clientEventTest);
        serverEventTest = deserializeFromString(serializedEvent,  it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.ChooseLineEvent.class , gson);
        assertTrue(serverEventTest.validate(gameModelTest));
    }

    @Test
    public void validationNotOkTest(){
        invalidTestInitialization();
        serializedEvent = serialize(clientEventTest);
        serverEventTest = deserializeFromString(serializedEvent,  it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.ChooseLineEvent.class , gson);
        assertFalse(serverEventTest.validate(gameModelTest));
    }

    private void validTestInitialization(){
        clientEventTest = new ChooseLineEvent(3);
    }

    private void invalidTestInitialization(){
        clientEventTest = new ChooseLineEvent(38);
    }

}