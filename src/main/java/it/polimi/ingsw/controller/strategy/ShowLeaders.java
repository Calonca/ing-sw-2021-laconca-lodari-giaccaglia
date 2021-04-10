package it.polimi.ingsw.controller.strategy;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

public class ShowLeaders extends GameStrategy
{
    public void execute(GameModel gamemodel)
    {
        //ON EVENT CHOOSELEADEREVENT
        gamemodel.getCurrentPlayer().setCurrentState(State.SHOWING_LEADERS_INITIAL);
    }
}
