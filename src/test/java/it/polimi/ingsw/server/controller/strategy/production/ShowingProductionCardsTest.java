package it.polimi.ingsw.server.controller.strategy.production;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.controller.states.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ShowingProductionCardsTest {

    @Test
    public void execute() {
        Validable validable = gameModel -> true;

        List<String> nicknames = new ArrayList<>();
        nicknames.add("testPlayer");
        boolean isSinglePlayer = true;
        GameModel gamemodel = new GameModel(nicknames, isSinglePlayer,null);
        try {
            assertEquals(new ShowingProductionCards().execute(gamemodel, validable), State.CHOOSING_CARD_FOR_PRODUCTION);
        } catch (it.polimi.ingsw.server.controller.EventValidationFailedException e) {
            e.printStackTrace();
        }
    }
}