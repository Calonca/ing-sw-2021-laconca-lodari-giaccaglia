package it.polimi.ingsw.server.controller.strategy.production;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

public class DeselectResource extends ProductionStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT DESELECTRESOURCEATPOSITION
        return State.CHOOSING_RESOURCE_FOR_PRODUCTION;
    }
}
