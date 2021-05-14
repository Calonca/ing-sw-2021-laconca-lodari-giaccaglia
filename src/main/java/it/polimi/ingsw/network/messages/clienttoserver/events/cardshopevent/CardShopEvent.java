package it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.clienttoserver.events.MiddlePhaseEvent;
import it.polimi.ingsw.server.model.states.State;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.CardShop;

/**
 * Client side {@link Event} created when {@link GameModel#currentPlayer currentPlayer} wants to view the {@link CardShop}
 * during {@link State#MIDDLE_PHASE MIDDLE_PHASE} by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class CardShopEvent extends MiddlePhaseEvent {
}
