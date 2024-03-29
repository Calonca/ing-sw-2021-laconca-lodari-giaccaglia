package it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent;

import com.google.gson.Gson;
import it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseCardEvent;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.player.board.Box;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.network.jsonutils.JsonUtility.deserializeFromString;
import static it.polimi.ingsw.network.jsonutils.JsonUtility.serialize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ChooseCardEventTest {

    ChooseCardEvent clientEventTest;
    it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardEvent serverEventTest;
    GameModel gameModelTest;
    String serializedEvent;
    Gson gson;
    Box discardBox;

    @Before
    public void setup(){
        initializeGameModel();
        gameModelTest.setCurrentPlayer(gameModelTest.getPlayer("testPlayer2").get());
        gson = new Gson();
    }

    @Test
    public void validationOkTest(){
        testInitialization(2, 1);
        serializedEvent = serialize(clientEventTest);
        serverEventTest = deserializeFromString(serializedEvent,  it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardEvent.class , gson);
        assertTrue(serverEventTest.validate(gameModelTest));
    }

    @Test
    public void validationNotOkTest(){
        testInitialization(3, 2);
        serializedEvent = serialize(clientEventTest);
        serverEventTest = deserializeFromString(serializedEvent,  it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardEvent.class , gson);
        assertFalse(serverEventTest.validate(gameModelTest));
    }

    private void testInitialization(int cardColorNumber, int cardLevel){
        clientEventTest = new ChooseCardEvent(cardColorNumber, cardLevel );
        discardBox = Box.discardBox();
        fillDiscardBox();
        addResourcesToPlayer();
    }


    private void fillDiscardBox(){
        int[] resources =gameModelTest.getCardShop().getCopyOfCardOnTop(DevelopmentCardColor.fromInt(2),1).getCostAsArray();
        discardBox.addResources(resources);
    }

    private void addResourcesToPlayer() {

        gameModelTest.getCurrentPlayer().getPersonalBoard().setMarketBox(discardBox);
        for (int position = 0; position < 4; position++) {
            if (discardBox.getNumberOf(Resource.fromIntFixed(position)) > 0) {
                gameModelTest.getCurrentPlayer().getPersonalBoard().move(position - 4, position - 8);
            }
        }
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

}