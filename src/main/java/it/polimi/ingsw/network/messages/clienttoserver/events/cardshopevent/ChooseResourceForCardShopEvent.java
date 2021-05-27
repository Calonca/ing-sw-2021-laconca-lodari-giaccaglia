package it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.model.states.State;
import it.polimi.ingsw.server.model.GameModel;

import java.util.List;
import java.util.Map;

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
    protected Map<Integer,Integer> chosenResources;

    /**
     * Client side {@link Event} constructor invoked when {@link State#CHOOSING_RESOURCES_FOR_DEVCARD CHOOSING_RESOURCES_FOR_DEVCARD}
     * phase action is performed.
     * @param resources Map representing current player chosen {@link it.polimi.ingsw.server.model.Resource Resources} in their int encoding,
     * for DevCard purchase. Each entry represents a Resource in a deposit spot: <br><br>
     *                                      key -> Resource global position in {@link it.polimi.ingsw.server.model.player.board.WarehouseLeadersDepots WarehouseLeaderDepots} or {@link it.polimi.ingsw.server.model.player.board.PersonalBoard#strongBox StrongBox}, <br>
     *                                      value -> Resource int encoding <br>
     *
     */
    public ChooseResourceForCardShopEvent(Map<Integer, Integer> resources){
       chosenResources = resources;
    }

    /**
     * Default constructor for handling {@link it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseResourceForCardShopEvent #SERVERChooseResourceForCardShopEvent}
     * server side equivalent inherited Event handler
     */
    public ChooseResourceForCardShopEvent(){}

}
