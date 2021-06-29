package it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;

/**
 * Client side {@link Event} created when player wants to pick {@link it.polimi.ingsw.server.model.Resource Resources}
 * from {@link it.polimi.ingsw.server.model.market.MarketBoard}
 * when {@link State#CHOOSING_MARKET_LINE CHOOSING_MARKET_LINE} game turn action is performed and
 * has to be processed to accomplish server-side client validation.
 */
public class MarketBoardEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.MarketBoardEvent implements Validable {

    //no validation apart from default one needed

    @Override
    public boolean validate(GameModel model) {
        return isGameStarted(model);
    }

}
