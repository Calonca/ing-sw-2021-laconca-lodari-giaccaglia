package it.polimi.ingsw.server.controller.strategy.leader;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

/**
 *  This implementation uses a flag variable Past, to decide wether or not MIDDLE PHASE has already been played.
 */
public class EndLeaders implements GameStrategy {
    public State execute(GameModel gamemodel)
    {
        //ON EVENT ACTIVATELEADEREVENT OR DISCARDLEADEREVENT
        if(gamemodel.getCurrentPlayer().getMacroState()==State.FINAL_PHASE)
            return State.IDLE;
        else
            return State.MIDDLE_PHASE;
    }
}