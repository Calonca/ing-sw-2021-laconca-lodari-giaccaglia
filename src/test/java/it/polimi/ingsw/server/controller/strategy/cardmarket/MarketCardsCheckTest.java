package it.polimi.ingsw.server.controller.strategy.cardmarket;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.controller.Match;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.states.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MarketCardsCheckTest {

    @Test
    public void executeToEND() {


        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());


        GameModel gamemodel = new GameModel(players, true ,new Match(1), onlineUsers);
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(new int[]{10,10,10,10});


        assertEquals(MarketCardsCheck.checkMarketCards(gamemodel, new ArrayList<>()).getKey(), State.END_PHASE);

    }


    /**
     * Doesnt work
     */
    @Test
    public void executeToMoreThanSevenCards() {

        List<Integer> choices=new ArrayList<>();
        choices.add(-8);
        choices.add(-7);
        choices.add(-6);
        choices.add(-5);

        Event clientEvent = new it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseResourceForCardShopEvent(choices);

        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        GameModel gamemodel = new GameModel(players, true ,new Match(1), onlineUsers);
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(new int[]{10,10,10,10});
        gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(0).addToTop(new DevelopmentCard());
        gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(0).addToTop(new DevelopmentCard());
        gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(0).addToTop(new DevelopmentCard());
        gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(1).addToTop(new DevelopmentCard());
        gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(1).addToTop(new DevelopmentCard());
        gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(1).addToTop(new DevelopmentCard());
        gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(2).addToTop(new DevelopmentCard());


        assertEquals(MarketCardsCheck.checkMarketCards(gamemodel, new ArrayList<>()).getKey(), State.END_PHASE);

    }


    @Test
    public void executeToOutOfStock() {


        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());


        GameModel gamemodel = new GameModel(players, true ,new Match(1), onlineUsers);
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(new int[]{10,10,10,10});
        gamemodel.getCardShop().purchaseCard(DevelopmentCardColor.BLUE,1);
        gamemodel.getCardShop().purchaseCard(DevelopmentCardColor.BLUE,1);
        gamemodel.getCardShop().purchaseCard(DevelopmentCardColor.BLUE,1);
        gamemodel.getCardShop().purchaseCard(DevelopmentCardColor.BLUE,1);
        gamemodel.getCardShop().purchaseCard(DevelopmentCardColor.BLUE,2);
        gamemodel.getCardShop().purchaseCard(DevelopmentCardColor.BLUE,2);
        gamemodel.getCardShop().purchaseCard(DevelopmentCardColor.BLUE,2);
        gamemodel.getCardShop().purchaseCard(DevelopmentCardColor.BLUE,2);
        gamemodel.getCardShop().purchaseCard(DevelopmentCardColor.BLUE,3);
        gamemodel.getCardShop().purchaseCard(DevelopmentCardColor.BLUE,3);
        gamemodel.getCardShop().purchaseCard(DevelopmentCardColor.BLUE,3);
        gamemodel.getCardShop().purchaseCard(DevelopmentCardColor.BLUE,3);


        assertEquals(MarketCardsCheck.checkMarketCards(gamemodel, new ArrayList<>()).getKey(), State.END_PHASE);

    }
}