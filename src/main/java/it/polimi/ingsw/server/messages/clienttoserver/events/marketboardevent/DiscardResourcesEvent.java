package it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.servertoclient.State;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;

/**
 * Client side {@link Event} created if player discards any MarketBoard picked {@link it.polimi.ingsw.server.model.Resource Resources}
 * after {@link State#CHOOSING_POSITION_FOR_RESOURCES CHOOSING_POSITION_FOR_RESOURCES} game turn action is performed and
 * has to be processed to accomplish server-side client validation.
 */
public class DiscardResourcesEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.DiscardResourcesEvent implements Validable {

    @Override
    public boolean validate(GameModel model) {
        return isGameStarted(model);
    }
}
