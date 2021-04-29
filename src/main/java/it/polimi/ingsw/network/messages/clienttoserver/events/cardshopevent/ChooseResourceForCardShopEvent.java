package it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.servertoclient.State;
import it.polimi.ingsw.server.model.GameModel;

import java.util.List;

/**
 * Client side {@link Event} created when {@link GameModel#currentPlayer currentPlayer} has to pick {@link it.polimi.ingsw.server.model.Resource Resources}
 * from his {@link it.polimi.ingsw.server.model.player.board.PersonalBoard PersonalBoard} deposits during
 * {@link State#CHOOSING_RESOURCES_FOR_DEVCARD CHOOSING_RESOURCES_FOR_DEVCARD} phase by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class ChooseResourceForCardShopEvent extends CardShopEvent {

    /**
     * Array of ints representing current player chosen {@link it.polimi.ingsw.server.model.Resource Resources} in their int encoding,
     * for DevCard purchase.
     */
    protected int [] chosenResources;

    /**
     * Client side {@link Event} constructor invoked when {@link State#CHOOSING_RESOURCES_FOR_DEVCARD CHOOSING_RESOURCES_FOR_DEVCARD}
     * phase action is performed.
     * @param resources List of Integers representing current player chosen {@link it.polimi.ingsw.server.model.Resource Resources} in their int encoding,
     * for DevCard purchase.
     */
    public ChooseResourceForCardShopEvent(List<Integer> resources){
       chosenResources = resources.stream().mapToInt(i->i).toArray();
    }

    /**
     * Default constructor for handling {@link it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseResourceForCardShopEvent #SERVERChooseResourceForCardShopEvent}
     * server side equivalent inherited Event handler
     */
    public ChooseResourceForCardShopEvent(){}

}
