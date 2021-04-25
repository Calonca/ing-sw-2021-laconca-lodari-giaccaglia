package it.polimi.ingsw.server.controller.strategy.leader;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.controller.strategy.Middle;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

/**
 *  This implementation uses a flag variable Past, to decide wether or not MIDDLE PHASE has already been played.
 */
public class EndLeaders implements GameStrategy {
    public State execute(GameModel gamemodel)
    {
        //ON EVENT ACTIVATELEADEREVENT OR DISCARDLEADEREVENT
        if (gamemodel.getCurrentPlayer().anyLeaderPlayable())
            return gamemodel.getCurrentPlayer().getCurrentState();
        else if (gamemodel.getCurrentPlayer().getCurrentState()==State.SHOWING_LEADERS_INITIAL)
            return new Middle().execute(gamemodel);
        else return State.IDLE;
    }}