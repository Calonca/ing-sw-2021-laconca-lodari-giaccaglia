package it.polimi.ingsw.server.model.player.leaders;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.player.board.LeaderDepot;
import javafx.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DepositLeaderTest
{
    @Test
    public void ActivateAndDiscard()
    {
        //ROUTINE
        LeaderDepot depotTest= new LeaderDepot(2, Resource.GOLD);
        Pair<Resource, Integer> costTest = new Pair<>(Resource.SERVANT, 3);
        Pair<DevelopmentCardColor, Integer> cardcostTest = new Pair<>(DevelopmentCardColor.BLUE, 3);


        List<Pair<Resource, Integer>> requirementsTest = new ArrayList<>();
        List<Pair<DevelopmentCardColor, Integer>> requirementsCardsTest = new ArrayList<>();

        requirementsTest.add(costTest);
        requirementsCardsTest.add(cardcostTest);

        //player.personalBoard= new PersonalBoard();
        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        GameModel gamemodel = new GameModel(players, true,null, onlineUsers);
        Player player = gamemodel.getCurrentPlayer();

        Leader leadertest = new DepositLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, depotTest);
        assertEquals(LeaderState.INACTIVE, leadertest.getState());

        leadertest.activate(gamemodel);
        assertEquals(LeaderState.ACTIVE, leadertest.getState());
        assertEquals(Resource.EMPTY, player.getPersonalBoard().getWarehouseLeadersDepots().getResourceAt(0));
        assertEquals(Resource.EMPTY, player.getPersonalBoard().getWarehouseLeadersDepots().getResourceAt(1));

        leadertest = new DepositLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, depotTest);
        leadertest.discard(gamemodel);
        assertEquals(LeaderState.DISCARDED, leadertest.getState());

    }
}