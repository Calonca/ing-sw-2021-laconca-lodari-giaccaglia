package it.polimi.ingsw.controller.strategy;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

public class ShowResourcesStore extends GameStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT CHOOSEMARKETBOARD
        return State.SHOWING_MARKET_RESOURCES;
    }
}
