package it.polimi.ingsw.server.controller.strategy.cardmarket;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.network.messages.servertoclient.State;

/**
 * This implementation simply sets the state for the view
 */
public class ShowCards implements GameStrategy {
    public State execute(GameModel gamemodel)
    {
        //ON EVENT CARDSHOPEVENT
        return State.SHOWING_CARD_SHOP;
    }
}
