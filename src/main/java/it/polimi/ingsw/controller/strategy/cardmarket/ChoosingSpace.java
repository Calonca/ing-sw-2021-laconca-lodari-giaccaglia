package it.polimi.ingsw.controller.strategy.cardmarket;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.cards.production.ProductionCardCell;
import it.polimi.ingsw.server.model.player.State;

/**
 * This method allows the player to place the selected card in an available space. Upon calling this is
 * precalculated that at least one correct option exists
 */
public class ChoosingSpace extends CardMarketStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT CHOOSECARDPOSITIONEVENT
        //MESSAGE IS 2
        ProductionCardCell chosencell=gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(2);
        if(chosencell.isSpotAvailable(gamemodel.getPurchasedCard()))
            chosencell.addToTop(gamemodel.getPurchasedCard());
        else
            return State.CHOOSING_POSITION_FOR_DEVCARD;

        return State.FINAL_PHASE;
    }
}