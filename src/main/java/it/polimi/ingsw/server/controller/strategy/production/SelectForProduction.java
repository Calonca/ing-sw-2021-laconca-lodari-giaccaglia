package it.polimi.ingsw.server.controller.strategy.production;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

public class SelectForProduction extends ProductionStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT SELECTPRODUCTIONATPOSITION
        return State.CHOOSING_RESOURCE_FOR_PRODUCTION;
    }
}