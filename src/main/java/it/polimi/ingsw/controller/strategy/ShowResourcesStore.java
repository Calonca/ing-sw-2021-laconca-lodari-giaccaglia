package it.polimi.ingsw.controller.strategy;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

public class ShowResourcesStore extends GameStrategy
{
    public void execute(GameModel gamemodel)
    {
        //ON EVENT CHOOSEMARKETBOARD
        gamemodel.getCurrentPlayer().setCurrentState(State.SHOWING_MARKET_RESOURCES);
    }
}
