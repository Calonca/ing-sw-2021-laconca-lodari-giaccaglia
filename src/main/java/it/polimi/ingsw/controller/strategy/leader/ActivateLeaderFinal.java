package it.polimi.ingsw.controller.strategy.leader;

import it.polimi.ingsw.controller.strategy.leader.LeaderStrategy;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

public class ActivateLeaderFinal extends LeaderStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT PLAYLEADEREVENTFINAL
        return State.SHOWING_LEADERS_INITIAL;
    }
}
