package it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent;

import com.google.gson.Gson;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.ChooseLineEvent;
import it.polimi.ingsw.server.model.GameModel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.server.model.jsonUtility.deserializeFromString;
import static it.polimi.ingsw.server.model.jsonUtility.serialize;
import static org.junit.Assert.*;

public class ChooseLineEventTest {

    ChooseLineEvent clientEventTest;
    it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.ChooseLineEvent serverEventTest;
    GameModel gameModelTest;
    String serializedEvent;
    Gson gson;

    @Before
    public void setUp() throws Exception {
        initializeGameModel();
        gameModelTest.setCurrentPlayer(gameModelTest.getPlayer("testPlayer4"));
        gson = new Gson();
    }

    private void initializeGameModel(){
        List<String> players = new ArrayList<>();
        players.add("testPlayer1");
        players.add("testPlayer2");
        players.add("testPlayer3");
        players.add("testPlayer4");

        gameModelTest = new GameModel(players, false, null);
        gameModelTest.setGameStatus(true);
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