package it.polimi.ingsw.server.controller.strategy.production;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

public class DeselectForProduction extends ProductionStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT DESELECTPRODUCTIONATPOSITION
        return State.CHOOSING_CARD_FOR_PRODUCTION;
    }
}
