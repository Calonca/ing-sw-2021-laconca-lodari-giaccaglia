package it.polimi.ingsw.server.controller.strategy.cardmarket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.jsonUtils.CommonGsonAdapters;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.controller.Match;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.states.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.server.messages.messagebuilders.Element.EndGameInfo;
import static org.junit.Assert.*;

public class MarketCardsCheckTest {

    @Test
    public void executeToEND() {

        List<Integer> choices=new ArrayList<>();
        choices.add(-8);
        choices.add(-7);
        choices.add(-6);
        choices.add(-5);

        Event clientEvent = new it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseResourceForCardShopEvent(choices);

        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseResourceForCardShopEvent.class, gson);
        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        boolean isSinglePlayer = true;

        GameModel gamemodel = new GameModel(players, isSinglePlayer,new Match(1), onlineUsers);
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(new int[]{10,10,10,10});


        assertEquals(MarketCardsCheck.checkMarketCards(gamemodel,new ArrayList<Element>()).getKey(), State.END_PHASE);

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

        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseResourceForCardShopEvent.class, gson);
        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        boolean isSinglePlayer = true;

        GameModel gamemodel = new GameModel(players, isSinglePlayer,new Match(1), onlineUsers);
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(new int[]{10,10,10,10});
        gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(0).addToTop(new DevelopmentCard());
        gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(0).addToTop(new DevelopmentCard());
        gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(0).addToTop(new DevelopmentCard());
        gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(1).addToTop(new DevelopmentCard());
        gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(1).addToTop(new DevelopmentCard());
        gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(1).addToTop(new DevelopmentCard());
        gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(2).addToTop(new DevelopmentCard());


        assertEquals(MarketCardsCheck.checkMarketCards(gamemodel,new ArrayList<Element>()).getKey(), State.END_PHASE);

    }


    @Test
    public void executeToOutOfStock() {


        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        boolean isSinglePlayer = true;


        GameModel gamemodel = new GameModel(players, isSinglePlayer,new Match(1), onlineUsers);
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


        assertEquals(MarketCardsCheck.checkMarketCards(gamemodel,new ArrayList<Element>()).getKey(), State.END_PHASE);

    }
}