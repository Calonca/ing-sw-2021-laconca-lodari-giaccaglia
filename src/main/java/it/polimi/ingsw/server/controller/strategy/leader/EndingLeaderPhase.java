package it.polimi.ingsw.server.controller.strategy.leader;

import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.controller.states.State;

/**
 *  This implementation uses a flag variable Past, to decide wether or not MIDDLE PHASE has already been played.
 */
public class EndingLeaderPhase implements GameStrategy {
    public State execute(GameModel gamemodel, Validable event) throws EventValidationFailedException
    {
        //ON EVENT ACTIVATELEADEREVENT OR DISCARDLEADEREVENT
        if (gamemodel.getCurrentPlayer().anyLeaderPlayable())
            return gamemodel.getCurrentPlayer().getCurrentState();
        else if (gamemodel.getCurrentPlayer().getCurrentState()==State.SHOWING_LEADERS_INITIAL)
            return State.MIDDLE_PHASE;
        else return State.IDLE;
    }}