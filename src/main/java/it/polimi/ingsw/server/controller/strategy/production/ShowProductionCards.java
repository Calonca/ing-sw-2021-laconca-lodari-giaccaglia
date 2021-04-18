package it.polimi.ingsw.server.controller.strategy.production;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;
/**
 * This implementation only sets the state for the view
 */
public class ShowProductionCards extends ProductionStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT CHOOSEPRODUCTIONEVENT
        return State.CHOOSING_CARD_FOR_PRODUCTION;
    }
}
