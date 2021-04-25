package it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.market.MarketLine;

/**
 * Client side {@link Event} created when player wants to pick resources from {@link it.polimi.ingsw.server.model.market.MarketBoard MarketBoard}
 * when {@link it.polimi.ingsw.server.model.player.State#SHOWING_MARKET_RESOURCES SHOWING_MARKET_RESOURCES} game turn action is performed and
 * has to be processed to accomplish server-side client validation.
 */
public class ChooseLineEvent extends MarketBoardEvent {

    /**
     * Client side {@link Event} constructor invoked when {@link it.polimi.ingsw.server.model.player.State#SHOWING_MARKET_RESOURCES SHOWING_MARKET_RESOURCES}
     * phase action is performed.
     * @param rowNumber int value matching an encoded {@link MarketLine}
     */
    public ChooseLineEvent(int rowNumber){
        this.chosenRow = rowNumber;
    }

}
