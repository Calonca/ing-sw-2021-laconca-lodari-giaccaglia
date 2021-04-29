package it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.servertoclient.State;

/**
 * Client side {@link Event} created if player discards any MarketBoard picked {@link it.polimi.ingsw.server.model.Resource Resources}
 * after {@link State#CHOOSING_POSITION_FOR_RESOURCES CHOOSING_POSITION_FOR_RESOURCES} game turn action is performed and
 * has to be processed to accomplish server-side client validation.
 */
public class DiscardResourcesEvent extends MarketBoardEvent {
 //no constructor apart from basic one needed
}
