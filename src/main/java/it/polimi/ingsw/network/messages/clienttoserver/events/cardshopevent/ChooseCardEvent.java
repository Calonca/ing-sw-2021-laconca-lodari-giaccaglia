package it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.clienttoserver.events.MiddlePhaseEvent;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.states.State;

/**
 * Client side {@link Event} created when {@link GameModel#currentPlayer} wants to purchase a {@link it.polimi.ingsw.server.model.cards.DevelopmentCard NetworkDevelopmentCard}
 * during {@link State#CHOOSING_DEVELOPMENT_CARD CHOOSING_DEVELOPMENT_CARD} by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class ChooseCardEvent extends MiddlePhaseEvent {

    /**
     * Int value matching a {@link DevelopmentCardColor} corresponding integer value.
     */
    protected int cardColorNumber;

    /**
     *  Level of chosen card from {@link it.polimi.ingsw.server.model.cards.CardShop CardShop}
     */
    protected int cardLevel;

    /**
     * Default constructor for handling {@link it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardEvent #SERVERChooseCardEvent} server side equivalent inherited Event handler
     */
    public ChooseCardEvent() {}

    /**
     * Client side {@link Event} constructor invoked when {@link State#CHOOSING_DEVELOPMENT_CARD CHOOSING_DEVELOPMENT_CARD}
     * phase action is performed.
     * @param cardColor int matching a {@link DevelopmentCardColor} corresponding integer value.
     * @param cardLevel level of chosen card from {@link it.polimi.ingsw.server.model.cards.CardShop CardShop}
     */
    public ChooseCardEvent(int cardColor, int cardLevel){
        this.cardColorNumber = cardColor;
        this.cardLevel = cardLevel;
    }

}
