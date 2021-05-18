package it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.model.states.State;

/**
 * Client side {@link Event} created when player wants move {@link it.polimi.ingsw.server.model.Resource Resources} picked
 * from {@link it.polimi.ingsw.server.model.market.MarketBoard} and move the ones inside his Warehouse deposits
 * when {@link State#CHOOSING_POSITION_FOR_RESOURCES CHOOSING_POSITION_FOR_RESOURCES} game turn action is performed and
 * has to be processed to accomplish server-side client validation.
 */
public class MoveResourceEvent extends MarketBoardEvent {

    /**
     * Represents position of the {@link it.polimi.ingsw.server.model.Resource Resource} to move.
     */
    protected int startPos;

    /**
     * Represents the destination position for the {@link it.polimi.ingsw.server.model.Resource Resource} to move.
     */
    protected int endPos;


    /**
     * Client side {@link Event} constructor invoked when {@link State#CHOOSING_POSITION_FOR_RESOURCES CHOOSING_POSITION_FOR_RESOURCES}
     * phase action is performed.
     * @param startPos int value position of the {@link it.polimi.ingsw.server.model.Resource Resource} to move.
     * @param endPos  int value of the destination position for the {@link it.polimi.ingsw.server.model.Resource Resource} to move.
     */
    public MoveResourceEvent(int startPos, int endPos){
        this.startPos = startPos;
        this.endPos = endPos;
    }

    /**
     * Default constructor for handling {@link it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.MoveResourceEvent MoveResourceEvent} #SERVERMoveResourceEvent
     * server side equivalent inherited Event handler
     */
    public MoveResourceEvent(){}


}
