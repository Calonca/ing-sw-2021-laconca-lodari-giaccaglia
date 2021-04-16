package it.polimi.ingsw.server.controller.strategy.production;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

public class ExecuteProduction extends ProductionStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT EXECUTEPRODUCTIONEVENT
        return State.FINAL_PHASE;
    }
}
