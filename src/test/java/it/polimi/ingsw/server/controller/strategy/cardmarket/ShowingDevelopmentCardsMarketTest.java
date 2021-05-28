package it.polimi.ingsw.server.controller.strategy.cardmarket;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ShowingDevelopmentCardsMarketTest {


    public void execute()
    {
        Validable v = gameModel -> true;

        List<String> nicknames = new ArrayList<>();
        nicknames.add("testPlayer");
        boolean isSinglePlayer = true;
        GameModel gamemodel = new GameModel(nicknames, isSinglePlayer,null);
        try {
            assertEquals(State.SHOWING_CARD_SHOP,new ShowingDevelopmentCardsMarket().execute(gamemodel,v).getKey());
        } catch (it.polimi.ingsw.server.controller.EventValidationFailedException e) {
            e.printStackTrace();
        }
    }
}