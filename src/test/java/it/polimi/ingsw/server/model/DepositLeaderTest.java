package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.player.LeaderState;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.player.board.LeaderDepot;
import javafx.util.Pair;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DepositLeaderTest
{
    @Test
    public void ActivateAndDiscard() throws IOException
    {
        //ROUTINE
        LeaderDepot depotTest= new LeaderDepot(2,Resource.GOLD);
        Pair<Resource, Integer> costTest = new Pair<>(Resource.SERVANT, 3);
        Pair<DevelopmentCardColor, Integer> cardcostTest = new Pair<DevelopmentCardColor,Integer>(DevelopmentCardColor.BLUE, 3);


        List<Pair<Resource, Integer>> requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        List<Pair<DevelopmentCardColor, Integer>> requirementsCardsTest = new ArrayList<Pair<DevelopmentCardColor, Integer>>();

        requirementsTest.add(costTest);
        requirementsCardsTest.add(cardcostTest);

        Player player= new Player();
        GameModel gamemodel = new GameModel();
        gamemodel.setCurrentPlayer(player);

        DepositLeader leadertest = new DepositLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, depotTest);
        assertEquals(LeaderState.INACTIVE, leadertest.getState());

        leadertest.activate(gamemodel);
        assertEquals(LeaderState.ACTIVE, leadertest.getState());
        assertEquals(Resource.EMPTY,player.getPersonalBoard().getWarehouseLeadersDepots().getResourceAt(7));

        leadertest = new DepositLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, depotTest);
        leadertest.discard(gamemodel);
        assertEquals(LeaderState.DISCARDED, leadertest.getState());

    }
}