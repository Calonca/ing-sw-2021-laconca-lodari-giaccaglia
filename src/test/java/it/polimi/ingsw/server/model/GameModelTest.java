package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.states.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class GameModelTest {

    GameModel gameModelTest;
    DevelopmentCard purchaseCardTest;
    Player testPlayer;


    @Test
    public void singlePlayerGameTest() {

    // test pick cards from cardshop, resources check not tested
    for(DevelopmentCardColor color : DevelopmentCardColor.values()) {

        if(!color.equals(DevelopmentCardColor.INVALID)) {
            setUpNewSingleGamerGameModel();
            testPurchaseCard(color);
        }

    }

    setUpNewSingleGamerGameModel();

    testDisconnection(testPlayer);

    }

    @Test
    public void multiPlayerGameTest() {

        setUpNewMultiPlayerGameModel();


    }

    private void setUpNewSingleGamerGameModel() {

        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        gameModelTest = new GameModel(players, true , null, onlineUsers);
        testPlayer = gameModelTest.getPlayer(players.get(0)).get();

        assertEquals(testPlayer, gameModelTest.getSinglePlayer());
        assertEquals(testPlayer, gameModelTest.getNextPlayer().get());
        assertEquals(testPlayer, gameModelTest.getCurrentPlayer());
        assertEquals(testPlayer, gameModelTest.getPlayer(players.get(0)).get());

        gameModelTest.start();

        assertEquals(0, gameModelTest.getPlayerPosition(testPlayer));
    }

    private void setUpNewMultiPlayerGameModel() {
        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        players.put(1,"testPlayer2");
        players.put(2,"testPlayer3");
        players.put(3,"testPlayer4");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        gameModelTest = new GameModel(players, false, null, onlineUsers);
    }

    private void testPurchaseCard(DevelopmentCardColor color){

        //turn 1
        gameModelTest.setCurrentPlayerState(State.CHOOSING_PRODUCTION);

        for(int level = 1; level<=3; level++) {

            assertFalse(gameModelTest.isCardColorOutOfStock(color));
            assertFalse(gameModelTest.isSomeDevCardColourOutOfStock());

            for (int i = 0; i < 4; i++) {

                assertTrue(gameModelTest.purchaseCardFromCardShop(color, level));

                purchaseCardTest = gameModelTest.takePurchasedCardFromShop();

                assertEquals(color, purchaseCardTest.getCardType());
                assertEquals(level, purchaseCardTest.getLevel());

            }

            assertFalse(gameModelTest.purchaseCardFromCardShop(color, level));

        }

        assertTrue(gameModelTest.isCardColorOutOfStock(color));
        assertEquals(color, gameModelTest.getDevCardColorOutOfStock());
        assertTrue(gameModelTest.isCardColorOutOfStock(color));

    }

    private void testDisconnection(Player player){

        gameModelTest.setOfflinePlayer(player);
        assertEquals(player, gameModelTest.getOfflinePlayers().get(0));
        assertFalse(gameModelTest.isPlayerCurrentlyOnline(0));
        gameModelTest.setOnlinePlayer(player);
        assertEquals(player,gameModelTest.getNextPlayer().get());

        gameModelTest.setOnlinePlayer(player);
        assertEquals(player,gameModelTest.getNextPlayer().get());
        assertEquals(player, gameModelTest.getOnlinePlayers().get(0));
        assertTrue(gameModelTest.isPlayerCurrentlyOnline(0));

    }

}
