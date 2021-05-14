package it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent;

import com.google.gson.Gson;
import it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.CardShopEvent;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.network.jsonUtils.JsonUtility.deserializeFromString;
import static it.polimi.ingsw.network.jsonUtils.JsonUtility.serialize;
import static org.junit.Assert.*;

public class CardShopEventTest {

    CardShopEvent clientEventTest;
    it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.CardShopEvent serverEventTest;
    GameModel gameModelTest;
    String serializedEvent;
    Gson gson;

    @Before
    public void setup(){
        gson = new Gson();
    }

    @Test
    public void validationOkTest(){
        initializeMultiPlayerGameModel();
        gameModelTest.setCurrentPlayer(gameModelTest.getPlayer("testPlayer3"));
        clientEventTest = new CardShopEvent();
        serializedEvent = serialize(clientEventTest);
        serverEventTest = deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.CardShopEvent.class , gson);
        assertTrue(serverEventTest.validate(gameModelTest));
    }

    @Test
    public void validationNotOkTest(){
        initializeSinglePlayerGameModel();
        invalidTestInitialization();
        clientEventTest = new CardShopEvent();
        serializedEvent = serialize(clientEventTest);
        serverEventTest = deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.CardShopEvent.class , gson);
        assertFalse(serverEventTest.validate(gameModelTest));
    }

    private void invalidTestInitialization(){
        for(int level=1; level<=3; level++){
            for(int i=0; i<4; i++){
                gameModelTest.purchaseCardFromCardShop(DevelopmentCardColor.YELLOW, level);
            }
        }
    }

    private void initializeSinglePlayerGameModel(){
        List<String> players = new ArrayList<>();
        players.add("testSinglePlayer");

        gameModelTest = new GameModel(players, true, null);
        gameModelTest.setGameStatus(true);
    }


    private void initializeMultiPlayerGameModel(){
        List<String> players = new ArrayList<>();
        players.add("testPlayer1");
        players.add("testPlayer2");
        players.add("testPlayer3");
        players.add("testPlayer4");

        gameModelTest = new GameModel(players, false, null);
        gameModelTest.setGameStatus(true);
    }


}