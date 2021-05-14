package it.polimi.ingsw.server.controller.strategy.leader;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class EndingLeaderPhaseTest {

    @Test
    public void execute() {
        Validable v = gameModel -> true;
        List<String> nicknames = new ArrayList<>();
        nicknames.add("testPlayer");
        boolean isSinglePlayer = true;
        GameModel gamemodel = new GameModel(nicknames, isSinglePlayer,null);
        gamemodel.getCurrentPlayer().setCurrentState(State.SHOWING_LEADERS_INITIAL);

        try {
            assertEquals(State.SHOWING_LEADERS_INITIAL, new EndingLeaderPhase().execute(gamemodel, v) );
        } catch (it.polimi.ingsw.server.controller.EventValidationFailedException e) {
            e.printStackTrace();
        }

        gamemodel.getCurrentPlayer().setCurrentState(State.SHOWING_LEADERS_FINAL);
        try {
            assertEquals(State.SHOWING_LEADERS_FINAL,new EndingLeaderPhase().execute(gamemodel, v) );
        } catch (it.polimi.ingsw.server.controller.EventValidationFailedException e) {
            e.printStackTrace();
        }
    }
}