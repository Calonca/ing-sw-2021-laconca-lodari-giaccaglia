package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.player.LeaderState;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.player.board.Depot;
import javafx.util.Pair;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LeaderTest
{
    @Test
    public void DevelopmentLeaderTest() throws IOException
    {
        //ROUTINE
        Pair<Resource, Integer> discountTest = new Pair<>(Resource.GOLD, 3);
        Pair<Resource, Integer> costTest = new Pair<>(Resource.SERVANT, 3);
        Pair<DevelopmentCardColor, Integer> cardcostTest = new Pair<DevelopmentCardColor,Integer>(DevelopmentCardColor.BLUE, 3);


        List<Pair<Resource, Integer>> requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        List<Pair<DevelopmentCardColor, Integer>> requirementsCardsTest = new ArrayList<Pair<DevelopmentCardColor, Integer>>();

        requirementsTest.add(costTest);
        requirementsCardsTest.add(cardcostTest);

        Player player= new Player();
        GameModel gamemodel = new GameModel();
        gamemodel.setCurrentPlayer(player);

        DevelopmentDiscountLeader leadertest = new DevelopmentDiscountLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, discountTest);
        assertEquals(LeaderState.INACTIVE, leadertest.getState());

        leadertest.activate(gamemodel);
        assertEquals(LeaderState.ACTIVE, leadertest.getState());
        int[] a=player.getDiscounts();
        assertEquals(java.util.Optional.of(a[leadertest.discount.getKey().getResourceNumber()]),leadertest.discount.getValue());

        leadertest = new DevelopmentDiscountLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, discountTest);
        leadertest.discard(gamemodel);
        assertEquals(LeaderState.DISCARDED, leadertest.getState());

    }

    @Test
    public void ProductionLeaderTest() throws IOException
    {
        //ROUTINE
        Production productiontest = Production.basicProduction();
        Pair<Resource, Integer> costTest = new Pair<>(Resource.SERVANT, 3);
        Pair<DevelopmentCardColor, Integer> cardcostTest = new Pair<DevelopmentCardColor,Integer>(DevelopmentCardColor.BLUE, 3);


        List<Pair<Resource, Integer>> requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        List<Pair<DevelopmentCardColor, Integer>> requirementsCardsTest = new ArrayList<Pair<DevelopmentCardColor, Integer>>();

        requirementsTest.add(costTest);
        requirementsCardsTest.add(cardcostTest);

        Player player= new Player();
        GameModel gamemodel = new GameModel();
        gamemodel.setCurrentPlayer(player);

        ProductionLeader leadertest = new ProductionLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, productiontest);
        assertEquals(LeaderState.INACTIVE, leadertest.getState());

        leadertest.activate(gamemodel);
        assertEquals(LeaderState.ACTIVE, leadertest.getState());
        assertFalse(player.getPersonalBoard().isProductionEmpty());

        leadertest = new ProductionLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, productiontest);
        leadertest.discard(gamemodel);
        assertEquals(LeaderState.DISCARDED, leadertest.getState());

    }

    public void DepositLeaderTest() throws IOException
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
        assertEquals(player.getPersonalBoard().getWarehouseLeadersDepots().isEmpty(),0);

        leadertest = new DepositLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, depotTest);
        leadertest.discard(gamemodel);
        assertEquals(LeaderState.DISCARDED, leadertest.getState());

    }
}