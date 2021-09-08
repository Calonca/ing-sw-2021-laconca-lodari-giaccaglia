package it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.model.market.MarketLine;

/**
 * Client side {@link Event} created when player wants to pick resources from {@link it.polimi.ingsw.server.model.market.MarketBoard MarketBoard}
 * when  game turn action is performed and
 * has to be processed to accomplish server-side client validation.
 */
public class ChooseLineEvent extends MarketBoardEvent {

    /**
     * Default constructor for handling {@link   it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.ChooseWhiteMarbleConversionEvent #SERVERChooseWhiteMarbleConversionEvent} server side equivalent inherited Event handler
     */
    public ChooseLineEvent(){}

    /**
     * Client side {@link Event} constructor invoked when
     * phase action is performed.
     * @param rowNumber int value matching an encoded {@link MarketLine}
     */
    public ChooseLineEvent(int rowNumber){
        this.chosenRow = rowNumber;
    }

}
