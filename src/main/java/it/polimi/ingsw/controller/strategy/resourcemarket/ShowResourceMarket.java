package it.polimi.ingsw.controller.strategy.resourcemarket;

import it.polimi.ingsw.controller.strategy.resourcemarket.ResourceMarketStrategy;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

public class ShowResourceMarket extends ResourceMarketStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT CHOOSEMARKETBOARD
        return State.SHOWING_MARKET_RESOURCES;
    }
}
