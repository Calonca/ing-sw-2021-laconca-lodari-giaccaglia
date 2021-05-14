package it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.model.states.State;
import it.polimi.ingsw.server.model.GameModel;

/**
 * Client side {@link Event} created when {@link GameModel#currentPlayer currentPlayer} has to place a {@link it.polimi.ingsw.server.model.cards.DevelopmentCard DevelopmentCard}
 * in his {@link it.polimi.ingsw.server.model.player.board.PersonalBoard PersonalBoard} during
 * {@link State#CHOOSING_POSITION_FOR_DEVCARD CHOOSING_POSITION_FOR_DEVCARD} phase by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class ChooseCardPositionEvent extends CardShopEvent {

    /**
     *  Represents a position of a {@link it.polimi.ingsw.server.model.cards.production.ProductionCardCell ProductionCardCell} inside
     * {@link it.polimi.ingsw.server.model.player.board.PersonalBoard PersonalBoard}.
     */
    protected int position;

    /**
     * Client side {@link Event} constructor invoked when {@link State#CHOOSING_POSITION_FOR_DEVCARD CHOOSING_POSITION_FOR_DEVCARD}
     * phase action is performed.
     * @param position int value representing a position of a {@link it.polimi.ingsw.server.model.cards.production.ProductionCardCell ProductionCardCell} inside
     * {@link it.polimi.ingsw.server.model.player.board.PersonalBoard PersonalBoard}.
     */
    public ChooseCardPositionEvent(int position){
        this.position = position;
    }

    /**
     * Default constructor for handling {@link it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardPositionEvent #SERVERChooseCardPositionEvent}
     * server side equivalent inherited Event handler
     */
    public ChooseCardPositionEvent(){}
}
