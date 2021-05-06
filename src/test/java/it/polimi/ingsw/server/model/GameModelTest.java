package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameModelTest {

    GameModel gameModelTest;
    DevelopmentCard purchaseCardTest;
    Player testPlayer;


    @Before
    public void setUp() throws Exception {

    }

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

        List<String> players = new ArrayList<>();
        players.add("testPlayer");
        boolean isSinglePlayer = true;
        gameModelTest = new GameModel(players, isSinglePlayer, null);
        testPlayer = gameModelTest.getPlayer(players.get(0));

        assertEquals(testPlayer, gameModelTest.getSinglePlayer());
        assertEquals(testPlayer, gameModelTest.getNextPlayer());
        assertEquals(testPlayer, gameModelTest.getCurrentPlayer());
        assertEquals(testPlayer, gameModelTest.getPlayer(players.get(0)));

        gameModelTest.setGameStatus(true);

        assertEquals(0, gameModelTest.getPlayerPosition(testPlayer));
    }

    private void setUpNewMultiPlayerGameModel() {

        List<String> players = new ArrayList<>();
        players.add("testPlayer1");
        players.add("testPlayer2");
        players.add("testPlayer3");
        players.add("testPlayer4");

        boolean isSinglePlayer = false;
        gameModelTest = new GameModel(players, false, null);
    }

    private void testPurchaseCard(DevelopmentCardColor color){

        //turn 1
        gameModelTest.setCurrentPlayerState(State.CHOOSING_CARD_FOR_PRODUCTION);

        for(int level = 1; level<=3; level++) {

            assertFalse(gameModelTest.isCardColorOutOfStock(color));
            assertFalse(gameModelTest.isSomeDevCardColourOutOfStock());

            for (int i = 0; i < 4; i++) {

                assertTrue(gameModelTest.purchaseCardFromCardShop(color, level));
                purchaseCardTest = gameModelTest.getPurchasedCard();

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
        assertEquals(player,gameModelTest.getNextPlayer());

        gameModelTest.setOnlinePlayer(player);
        assertEquals(player,gameModelTest.getNextPlayer());
        assertEquals(player, gameModelTest.getOnlinePlayers().get(0));
        assertTrue(gameModelTest.isPlayerCurrentlyOnline(0));

    }

}
