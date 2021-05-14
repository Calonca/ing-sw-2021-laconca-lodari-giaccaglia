package it.polimi.ingsw.server.controller.strategy.resourcemarket;

import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.controller.states.State;

/**
 * This implementation only sets the state for the view
 */
public class ShowingResourceMarket implements GameStrategy {
    public State execute(GameModel gamemodel, Validable event) throws EventValidationFailedException
    {
        //ON EVENT CHOOSEMARKETBOARD
        return State.SHOWING_MARKET_RESOURCES;
    }
}
