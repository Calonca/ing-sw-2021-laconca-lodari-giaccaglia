package it.polimi.ingsw.server.model.player.leaders;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.production.Production;
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

public class ProductionLeaderTest {

    @Test
    public void ActivateAndDiscard()
    {
        //ROUTINE
        Production productiontest = Production.basicProduction();
        Pair<Resource, Integer> costTest = new Pair<>(Resource.SERVANT, 3);
        Pair<DevelopmentCardColor, Integer> cardcostTest = new Pair<>(DevelopmentCardColor.BLUE, 3);


        List<Pair<Resource, Integer>> requirementsTest = new ArrayList<>();
        List<Pair<DevelopmentCardColor, Integer>> requirementsCardsTest = new ArrayList<>();

        requirementsTest.add(costTest);
        requirementsCardsTest.add(cardcostTest);

        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");

        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        boolean isSinglePlayer = true;
        GameModel gamemodel = new GameModel(players, isSinglePlayer,null, onlineUsers);
        Player player = gamemodel.getCurrentPlayer();

        Leader leadertest = new ProductionLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, productiontest);
        assertEquals(LeaderState.INACTIVE, leadertest.getState());

        leadertest.activate(gamemodel);
        assertEquals(LeaderState.ACTIVE, leadertest.getState());
        assertNotEquals(1, player.getPersonalBoard().getSelectedProduction().length);

        leadertest = new ProductionLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, productiontest);
        leadertest.discard(gamemodel);
        assertEquals(LeaderState.DISCARDED, leadertest.getState());

    }



}