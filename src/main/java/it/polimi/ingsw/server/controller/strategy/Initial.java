package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.State;

/**
 * On turn start, if there is the possibility to play a Leader, the user will be able to decide wether to play
 * or a normal action
 */
public class Initial implements GameStrategy {
    public State execute(GameModel gamemodel)
    {
        if(!gamemodel.getCurrentPlayer().anyLeaderPlayable())
            return State.MIDDLE_PHASE;
        else
            return State.SHOWING_LEADERS_INITIAL;
    }
}
