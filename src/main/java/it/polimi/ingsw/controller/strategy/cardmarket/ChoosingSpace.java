package it.polimi.ingsw.controller.strategy.cardmarket;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.cards.production.ProductionCardCell;
import it.polimi.ingsw.server.model.player.State;

public class ChoosingSpace extends CardMarketStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT CHOOSECARDPOSITIONEVENT
        //RICEVO MESSAGGIO INTERO 2
        ProductionCardCell chosencell=gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(2);
        if(chosencell.isSpotAvailable(gamemodel.getCardShop().pickPurchasedCard()))
            chosencell.addToTop(gamemodel.getCardShop().pickPurchasedCard());
        else
            return State.CHOOSING_POSITION_FOR_DEVCARD;

        return State.SHOWING_CARD_SHOP;
    }
}