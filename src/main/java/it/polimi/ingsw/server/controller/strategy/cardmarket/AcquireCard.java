package it.polimi.ingsw.server.controller.strategy.cardmarket;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.State;
/**
 * The chosen card gets acquired. If it's not possible, the user returns to the market view.
 * Received message is int, cards are mapped as follows INT%3 IS THE LEVEL, INT/3 IS THE COLOR
 * If the color is not out of stock, the method proceeds to acquire the card, and if it should not have
 * been possible, it will get back to the previous state
 */
public class AcquireCard implements GameStrategy {
    public State execute(GameModel gamemodel, Validable event)
    {
        //ON EVENT CHOOSECARDEVENT
        //MESSAGE IS 4
        //CARDS ARE MAPPED AS FOLLOWS       INT%3 IS THE LEVEL, INT/3 IS THE COLOR
        //
        State state;
        int chosencard=4;
        int level=chosencard%3; //1      LEVEL 2 CARD
        int color=chosencard/3; //1      BLUe
        if(!gamemodel.isCardColorOutOfStock(DevelopmentCardColor.fromInt(color)))
            gamemodel.purchaseCardFromCardShop(DevelopmentCardColor.fromInt(color),level);
                if(!gamemodel.getCurrentPlayer().getPersonalBoard().isDevelopmentCardAvailable(gamemodel.getPurchasedCard()))
                    return State.SHOWING_CARD_SHOP;

        return State.CHOOSING_RESOURCES_FOR_DEVCARD;
    }
}