package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.player.LeaderState;
import it.polimi.ingsw.server.model.player.Player;
import javafx.util.Pair;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LeaderTest
{
    @Test
    public void DevelopmentTest() throws IOException
    {
        //ROUTINE
        Pair<Resource, Integer> discountTest = new Pair<>(Resource.GOLD, 3);
        Pair<Resource, Integer> costTest = new Pair<>(Resource.SERVANT, 3);

        List<Pair<Resource, Integer>> requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        requirementsTest.add(costTest);
        GameModel gamemodel = new GameModel();
        /**
         * Testing basic functions
         */
        DevelopmentDiscountLeader leadertest = new DevelopmentDiscountLeader(LeaderState.INACTIVE, 3, requirementsTest, discountTest);
        assertEquals(LeaderState.INACTIVE, leadertest.getState());
        leadertest.activate(gamemodel);
        assertEquals(LeaderState.ACTIVE, leadertest.getState());
        leadertest = new DevelopmentDiscountLeader(LeaderState.INACTIVE, 3, requirementsTest, discountTest);
        leadertest.discard(gamemodel);
        assertEquals(LeaderState.DISCARDED, leadertest.getState());

        /**
         *  TODO TESTING REQUIREMENT FUNCTION
         */
        /**
        *  Testing specific functions
        */
        discountTest = new Pair<>(Resource.SERVANT, 3);
        leadertest = new DevelopmentDiscountLeader(LeaderState.INACTIVE, 3, requirementsTest, discountTest);
        Player playerTest= new Player();
        assertEquals(playerTest.getDiscounts()[leadertest.discount.getKey().getResourceNumber()], (int) leadertest.discount.getValue());

    }
}