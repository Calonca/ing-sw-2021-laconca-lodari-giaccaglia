package it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.clienttoserver.events.MiddlePhaseEvent;
import it.polimi.ingsw.server.controller.states.State;
import it.polimi.ingsw.server.model.market.MarketLine;

/**
 * Client side {@link Event} created when player wants to pick {@link it.polimi.ingsw.server.model.Resource Resources}
 * from {@link it.polimi.ingsw.server.model.market.MarketBoard}
 * when {@link State#SHOWING_MARKET_RESOURCES SHOWING_MARKET_RESOURCES} game turn action is performed and
 * has to be processed to accomplish server-side client validation.
 */
public class MarketBoardEvent extends MiddlePhaseEvent {

    /**
     *  Value matching an encoded {@link MarketLine}
     */
    protected int chosenRow;


    //no constructor apart from basic one needed

}
