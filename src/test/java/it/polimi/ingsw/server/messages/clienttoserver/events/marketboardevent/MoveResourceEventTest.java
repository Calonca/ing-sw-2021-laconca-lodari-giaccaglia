package it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent;

import com.google.gson.Gson;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.MoveResourceEvent;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.market.MarketLine;
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

public class MoveResourceEventTest {

    MoveResourceEvent clientEventTest;
    it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.MoveResourceEvent serverEventTest;
    GameModel gameModelTest;
    String serializedEvent;
    Gson gson;
    int resourceNumber = 100;

    @Before
    public void setUp() {
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
        serverEventTest = deserializeFromString(serializedEvent,  it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.MoveResourceEvent.class , gson);
        assertTrue(serverEventTest.validate(gameModelTest));
    }

    @Test
    public void validationNotOkTest(){
        invalidTestInitialization();
        serializedEvent = serialize(clientEventTest);
        serverEventTest = deserializeFromString(serializedEvent,  it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.MoveResourceEvent.class , gson);
        assertFalse(serverEventTest.validate(gameModelTest));
    }

    private void invalidTestInitialization(){

        for(MarketLine line : MarketLine.values()) {
            if(line!=MarketLine.INVALID_LINE)
                gameModelTest.chooseLineFromMarketBoard(line);
            if(!gameModelTest.areThereWhiteMarblesInPickedLine()) {
                System.out.println(line);
                break;
            }
        }

        Box discardBox = gameModelTest.getBoxOfResourcesFromMarketBoard();

        for(Resource resource : Resource.values()){
            if( resource.getResourceNumber() < 4 && discardBox.getNumberOf(resource) == 0) {
                System.out.println(resource.getResourceNumber());
                System.out.println(discardBox.getNumberOf(resource));
                resourceNumber = resource.getResourceNumber();
                break;
            }
        }
        System.out.println(resourceNumber);
        gameModelTest.getCurrentPlayer().getPersonalBoard().setMarketBox(discardBox);
        clientEventTest = new MoveResourceEvent(resourceNumber - 4, resourceNumber);
        clientEventTest.setPlayerNickname(gameModelTest.getCurrentPlayer().getNickname());

    }

    private void validTestInitialization(){

       for(MarketLine line : MarketLine.values()) {
           if(line!=MarketLine.INVALID_LINE)
            gameModelTest.chooseLineFromMarketBoard(line);
           if(!gameModelTest.areThereWhiteMarblesInPickedLine()) {
              // System.out.println(line);
               break;
           }
       }

        Box discardBox = gameModelTest.getBoxOfResourcesFromMarketBoard();

        for(Resource resource : Resource.values()){
            if( resource.getResourceNumber() < 4 && discardBox.getNumberOf(resource) != 0) {
             //   System.out.println(discardBox.getNumberOf(resource));
                resourceNumber = resource.getResourceNumber();
                break;
            }
        }
      //  System.out.println(resourceNumber);
         gameModelTest.getCurrentPlayer().getPersonalBoard().setMarketBox(discardBox);
        clientEventTest = new MoveResourceEvent(resourceNumber - 4, resourceNumber);
        clientEventTest.setPlayerNickname(gameModelTest.getCurrentPlayer().getNickname());
    }
}