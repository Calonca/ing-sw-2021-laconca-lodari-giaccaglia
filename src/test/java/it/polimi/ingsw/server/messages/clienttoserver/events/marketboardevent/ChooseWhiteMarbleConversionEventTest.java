package it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent;

import com.google.gson.Gson;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.ChooseWhiteMarbleConversionEvent;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.market.Marble;
import it.polimi.ingsw.server.model.market.MarketLine;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.network.jsonUtils.JsonUtility.deserializeFromString;
import static it.polimi.ingsw.network.jsonUtils.JsonUtility.serialize;
import static org.junit.Assert.*;

public class ChooseWhiteMarbleConversionEventTest {

    ChooseWhiteMarbleConversionEvent clientEventTest;
    it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.ChooseWhiteMarbleConversionEvent serverEventTest;
    GameModel gameModelTest;
    String serializedEvent;
    Gson gson;

    @Before
    public void setUp() throws Exception {
        initializeGameModel();
        gameModelTest.setCurrentPlayer(gameModelTest.getPlayer("testPlayer4"));
        gson = new Gson();
    }

    @Test
    public void validationOkTest(){
        eventCreation(1);
        findWhiteMarble();
        serializedEvent = serialize(clientEventTest);
        serverEventTest = deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.ChooseWhiteMarbleConversionEvent.class , gson);
        assertTrue(serverEventTest.validate(gameModelTest));
    }

    @Test
    public void validationNotOkTest(){
        eventCreation(55);
        serializedEvent = serialize(clientEventTest);
        serverEventTest = deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.ChooseWhiteMarbleConversionEvent.class , gson);
        assertFalse(serverEventTest.validate(gameModelTest));
    }
    private void eventCreation(int resourceNumber){
        clientEventTest = new ChooseWhiteMarbleConversionEvent(resourceNumber);
    }


    private void initializeGameModel(){
        List<String> players = new ArrayList<>();
        players.add("testPlayer1");
        players.add("testPlayer2");
        players.add("testPlayer3");
        players.add("testPlayer4");

        gameModelTest = new GameModel(players, false , null);
        gameModelTest.setGameStatus(true);
    }



    private void findWhiteMarble (){
        for(MarketLine  line : MarketLine.values()) {
            if (!line.equals(MarketLine.INVALID_LINE)) {
                Marble[] pickedMarbles;
                pickedMarbles = gameModelTest.chooseLineFromMarketBoard(line);
                if(Arrays.asList(pickedMarbles).contains(Marble.WHITE))
                    break;
            }
        }
    }
}