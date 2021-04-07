package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.player.LeaderState;
import it.polimi.ingsw.server.model.player.Player;
import javafx.util.Pair;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MarketLeaderTest {
    @Test
    public void ActivateAndDiscard() throws IOException
    {
        //ROUTINE
        Resource bonus=Resource.GOLD;
        Pair<Resource, Integer> costTest = new Pair<>(Resource.SERVANT, 3);
        Pair<DevelopmentCardColor, Integer> cardcostTest = new Pair<DevelopmentCardColor,Integer>(DevelopmentCardColor.BLUE, 3);


        List<Pair<Resource, Integer>> requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        List<Pair<DevelopmentCardColor, Integer>> requirementsCardsTest = new ArrayList<Pair<DevelopmentCardColor, Integer>>();

        requirementsTest.add(costTest);
        requirementsCardsTest.add(cardcostTest);

        Player player= new Player();
        GameModel gamemodel = new GameModel();
        gamemodel.setCurrentPlayer(player);

        MarketLeader leadertest = new MarketLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, bonus);
        assertEquals(LeaderState.INACTIVE, leadertest.getState());

        leadertest.activate(gamemodel);
        assertEquals(LeaderState.ACTIVE, leadertest.getState());
        //check bonus assertEquals(Resource.EMPTY,player.getPersonalBoard().getWarehouseLeadersDepots().getResourceAt(7));

        leadertest = new MarketLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, bonus);
        leadertest.discard(gamemodel);
        assertEquals(LeaderState.DISCARDED, leadertest.getState());

    }

}