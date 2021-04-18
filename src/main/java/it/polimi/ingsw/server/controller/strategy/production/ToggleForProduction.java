package it.polimi.ingsw.server.controller.strategy.production;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

/**
 * This implementation allows the user to toggle selections on the available productions. When a choice is needed, the
 * used needs to perform it to completion before being able to toggle another production.
 */
public class ToggleForProduction extends GameStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT SELECTPRODUCTIONATPOSITION
        //MESSAGE IS INTEGER OF AVAILABLE POSITION, ZERO IS STOP?
        int msg=3;
        if (msg==0)
        {
            gamemodel.getCurrentPlayer().getPersonalBoard().produce();
            return State.FINAL_PHASE;

        }
        if(gamemodel.getCurrentPlayer().getPersonalBoard().getAvailableProductions()[msg])
            gamemodel.getCurrentPlayer().getPersonalBoard().toggleSelectProductionAt(msg);
                if(gamemodel.getCurrentPlayer().getPersonalBoard().firstProductionSelectedWithChoice().isPresent())
                    return State.CHOOSING_RESOURCE_FOR_PRODUCTION;
        return State.CHOOSING_CARD_FOR_PRODUCTION;
    }
}
