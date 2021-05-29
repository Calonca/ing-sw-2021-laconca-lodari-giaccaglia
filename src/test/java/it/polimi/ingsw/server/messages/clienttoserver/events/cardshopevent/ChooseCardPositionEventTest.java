package it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent;

import com.google.gson.Gson;
import it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseCardPositionEvent;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.network.jsonUtils.JsonUtility.deserializeFromString;
import static it.polimi.ingsw.network.jsonUtils.JsonUtility.serialize;
import static org.junit.Assert.*;

public class ChooseCardPositionEventTest {

    ChooseCardPositionEvent clientEventTest;
    it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardPositionEvent serverEventTest;
    GameModel gameModelTest;
    String serializedEvent;
    Gson gson;

    @Before
    public void setup(){
        initializeGameModel();
        gameModelTest.setCurrentPlayer(gameModelTest.getPlayer("testPlayer2"));
        gameModelTest.purchaseCardFromCardShop(DevelopmentCardColor.fromInt(2),1);
        gson = new Gson();
    }

    @Test
    public void validationOkTest(){
        clientEventTest = new ChooseCardPositionEvent(1);
        serializedEvent = serialize(clientEventTest);
        serverEventTest = deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardPositionEvent.class , gson);
        assertTrue(serverEventTest.validate(gameModelTest));
    }

    @Test
    public void validationNotOkTest(){
        DevelopmentCard testCard = gameModelTest.getCardShop().getCopyOfCardOnTop(DevelopmentCardColor.fromInt(2), 1);
        gameModelTest.getCurrentPlayer().getPersonalBoard().addDevelopmentCardToCell(testCard, 1);
        clientEventTest = new ChooseCardPositionEvent(1);
        serializedEvent = serialize(clientEventTest);
        serverEventTest = deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardPositionEvent.class , gson);
        assertFalse(serverEventTest.validate(gameModelTest));
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
}