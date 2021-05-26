package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;

import java.util.ArrayList;
import java.util.List;

/**
 * On turn start, if there is the possibility to play a Leader, the user will be able to decide wether to play
 * or a normal action
 */
public class Initial implements GameStrategy {

    public State execute(GameModel gamemodel, Validable event) throws EventValidationFailedException
    {
        if(!gamemodel.getCurrentPlayer().anyLeaderPlayable())
            return State.MIDDLE_PHASE;
        else
            return State.SHOWING_LEADERS_INITIAL;
    }

}
