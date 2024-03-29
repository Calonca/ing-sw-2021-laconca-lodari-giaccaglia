package it.polimi.ingsw.server.model.player.leaders;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.player.Player;
import javafx.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class MarketLeaderTest {
    @Test
    public void ActivateAndDiscard()
    {
        //ROUTINE
        Resource bonus=Resource.GOLD;
        Pair<Resource, Integer> costTest = new Pair<>(Resource.SERVANT, 3);
        Pair<DevelopmentCardColor, Integer> cardcostTest = new Pair<>(DevelopmentCardColor.BLUE, 3);


        List<Pair<Resource, Integer>> requirementsTest = new ArrayList<>();
        List<Pair<DevelopmentCardColor, Integer>> requirementsCardsTest = new ArrayList<>();

        requirementsTest.add(costTest);
        requirementsCardsTest.add(cardcostTest);

        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        GameModel gamemodel = new GameModel(players, true ,null, onlineUsers);
        Player player = gamemodel.getCurrentPlayer();

        MarketLeader leadertest = new MarketLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, bonus);
        assertEquals(LeaderState.INACTIVE, leadertest.getState());

        leadertest.activate(gamemodel);
        assertEquals(LeaderState.ACTIVE, leadertest.getState());
        assertTrue(player.getMarketBonus()[bonus.getResourceNumber()]);



    }

}