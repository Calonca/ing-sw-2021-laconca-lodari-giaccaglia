package it.polimi.ingsw.server.controller.strategy.leader;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class EndLeadersTest {

    @Test
    public void execute() {
        List<String> nicknames = new ArrayList<>();
        nicknames.add("testPlayer");
        boolean isSinglePlayer = true;
        GameModel gamemodel = new GameModel(nicknames, isSinglePlayer);
        gamemodel.getCurrentPlayer().setCurrentState(State.SHOWING_LEADERS_INITIAL);
        assertEquals(State.MIDDLE_PHASE,new EndLeaders().execute(gamemodel) );
        gamemodel.getCurrentPlayer().setCurrentState(State.SHOWING_LEADERS_FINAL);
        assertEquals(State.IDLE,new EndLeaders().execute(gamemodel) );


    }
}