package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.controller.states.State;
/**
 * On turn end, if there is the possibility to play a Leader, the user will be able to decide wether to play
 * or end the turn
 */
public class Final implements GameStrategy {
    public State execute(GameModel gamemodel, Validable event) throws EventValidationFailedException
    {
        //MESSAGE IS SKIPLEADER OR CHOOSELEADER (0 or 1)
        //gamemodel.getCurrentPlayer().setMacroState(State.INITIAL_PHASE);
        if(!gamemodel.getCurrentPlayer().anyLeaderPlayable())
            return State.IDLE;
        else
            return State.SHOWING_LEADERS_FINAL;
    }
}
