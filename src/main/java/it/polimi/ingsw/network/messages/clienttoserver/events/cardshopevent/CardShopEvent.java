package it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.clienttoserver.events.MiddlePhaseEvent;
import it.polimi.ingsw.server.controller.states.State;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.CardShop;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;

/**
 * Client side {@link Event} created when {@link GameModel#currentPlayer currentPlayer} wants to view the {@link CardShop}
 * during {@link State#MIDDLE_PHASE MIDDLE_PHASE} by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class CardShopEvent extends MiddlePhaseEvent {

    /**
     * Method to verify if current player's {@link it.polimi.ingsw.server.model.cards.CardShop#purchasedCard devCard}
     * from {@link it.polimi.ingsw.server.model.cards.CardShop CardShop} in
     * {@link State#CHOOSING_DEVELOPMENT_CARD CHOOSING_DEVELOPMENT_CARD} phase has
     * resources and level requirements matching current player's {@link it.polimi.ingsw.server.model.player.board.PersonalBoard PersonalBoard}
     * availability.
     * @return true if resources and level requirements are satisfied, otherwise false
     */
    protected boolean validateDevCardRequirements(DevelopmentCard card){
        return currentPlayerPersonalBoard.isDevelopmentCardAvailable(card);
    }
}
