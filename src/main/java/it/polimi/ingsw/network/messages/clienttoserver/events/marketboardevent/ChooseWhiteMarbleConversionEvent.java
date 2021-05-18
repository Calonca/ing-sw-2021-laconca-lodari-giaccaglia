package it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.model.states.State;
import it.polimi.ingsw.server.model.Resource;

/**
 * Client side {@link Event} created when player has to choose a resource for {@link it.polimi.ingsw.server.model.market.Marble#WHITE WHITEMARBLE} conversion
 * when {@link State#CHOOSING_WHITEMARBLE_CONVERSION CHOOSING_WHITEMARBLE_CONVERSION} game turn action is performed and
 * has to be processed to accomplish server-side client validation.
 */
public class ChooseWhiteMarbleConversionEvent extends MarketBoardEvent {

    /**
     * Represents current player chosen {@link Resource Resource} in the int encoding,
     * for {@link it.polimi.ingsw.server.model.market.Marble#WHITE WHITEMARBLE} conversion.
     */
    protected int resourceNumber;

    /**
     * Default constructor for handling {@link it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.ChooseWhiteMarbleConversionEvent #SERVERChooseWhiteMarbleConversionEvent} server side equivalent inherited Event handler
     */
    public ChooseWhiteMarbleConversionEvent(){}

    /**
     * Client side {@link Event} constructor invoked when {@link State#CHOOSING_WHITEMARBLE_CONVERSION CHOOSING_WHITEMARBLE_CONVERSION}
     * phase action is performed.
     * @param resourceNumber int representing current player chosen {@link it.polimi.ingsw.server.model.Resource Resource} in the int encoding,
     * for {@link it.polimi.ingsw.server.model.market.Marble#WHITE WHITEMARBLE} conversion.
     */
    public ChooseWhiteMarbleConversionEvent(int resourceNumber){
        this.resourceNumber = resourceNumber;
    }

}
