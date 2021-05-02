package it.polimi.ingsw.server.controller.strategy.resourcemarket;

import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.State;
/**
 * This implementation allows the user to move one at a time the resources from one of the depot lines to an empty
 * space. Before being able to proceed, the user needs to place all of the resources from the market either in the
 * DiscardBox or in one of their depot lines. For each resource in the DiscardBox all the other players get a faith point
 */
public class AddResources implements GameStrategy {
    public State execute(GameModel gamemodel, Validable event) throws EventValidationFailedException
    {
        //ON EVENT MOVERESOURCEEVENT
        //MESSAGE IS TWO INT, STARTING POSITION AND ENDING POSITION. 0,0 IS FOR TERMINATION
        int msg1=3;
        int msg2=4;

        gamemodel.getCurrentPlayer().getPersonalBoard().move(msg1,msg2);
        return State.CHOOSING_POSITION_FOR_RESOURCES;

    }}
