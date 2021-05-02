package it.polimi.ingsw.server.controller.strategy.leader;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class EndLeadersTest {

    @Test
    public void execute() {
        Validable v = gameModel -> true;
        List<String> nicknames = new ArrayList<>();
        nicknames.add("testPlayer");
        boolean isSinglePlayer = true;
        GameModel gamemodel = new GameModel(nicknames, isSinglePlayer);
        gamemodel.getCurrentPlayer().setCurrentState(State.SHOWING_LEADERS_INITIAL);
        try {
            assertEquals(State.MIDDLE_PHASE,new EndLeaders().execute(gamemodel, v) );
        } catch (it.polimi.ingsw.server.controller.EventValidationFailedException e) {
            e.printStackTrace();
        }
        gamemodel.getCurrentPlayer().setCurrentState(State.SHOWING_LEADERS_FINAL);
        try {
            assertEquals(State.IDLE,new EndLeaders().execute(gamemodel, v) );
        } catch (it.polimi.ingsw.server.controller.EventValidationFailedException e) {
            e.printStackTrace();
        }
    }
}