package it.polimi.ingsw.controller.strategy.production;

import it.polimi.ingsw.controller.strategy.production.ProductionStrategy;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

public class ExecuteProduction extends ProductionStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT CHOOSEPRODUCTIONEVENT
        return State.CHOOSING_CARD_FOR_PRODUCTION;
    }
}
