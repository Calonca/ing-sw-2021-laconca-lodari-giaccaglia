package it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;

/**
 * Client side {@link Event} created when player wants move {@link it.polimi.ingsw.server.model.Resource Resources} picked
 * from {@link it.polimi.ingsw.server.model.market.MarketBoard} and move the ones inside his Warehouse deposits
 * when {@link State#CHOOSING_POSITION_FOR_RESOURCES CHOOSING_POSITION_FOR_RESOURCES} game turn action is performed and
 * has to be processed to accomplish server-side client validation.
 */
public class MoveResourceEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.MoveResourceEvent implements Validable {

    /**
     * {@link GameModel} of the event's current game on which event validation has to be performed.
     */
    private transient GameModel gameModel;
    private transient PersonalBoard currentPlayerPersonalBoard;

    /**
     * Server-side initializer to setup common attributes among {@link State#MIDDLE_PHASE MIDDLE_PHASE}
     * events.
     * @param gameModel {@link GameModel} of the event's current game on which event validation has to be performed.
     */
    private void initializeMiddlePhaseEventValidation(GameModel gameModel){
        this.gameModel = gameModel;
        this.currentPlayerPersonalBoard = gameModel.getCurrentPlayer().getPersonalBoard();
    }

    @Override
    public boolean validate(GameModel model) {
        initializeMiddlePhaseEventValidation(model);
        return isGameStarted(model) && checkResourceAvailability() && validatePositions();
    }

    /*

    /**
     * Method to validate initial and final positions for {@link it.polimi.ingsw.server.model.NetworkResource Resources} taken from
     * {@link it.polimi.ingsw.server.model.market.MarketBoard MarketBoard} and placed in
     * {@link it.polimi.ingsw.server.model.player.board.WarehouseDepot WarehouseDepot}
     *
     * @param endPos  int value of the destination position for the {@link it.polimi.ingsw.server.model.NetworkResource NetworkResource} to move.
     *
     * @return true if starting position is among ones in the picked {@link it.polimi.ingsw.server.model.NetworkResource Resources} and
     * ending position is among available ones inside {@link it.polimi.ingsw.server.model.player.board.WarehouseDepot WarehouseDepot}
     *
    private boolean validateNewResourceToAdd(int endPos){
        return endPos >=0;

    }

    */

    /**
     * Method to validate initial and final position for {@link it.polimi.ingsw.server.model.Resource Resources} moved
     * from a spot to another available inside {@link it.polimi.ingsw.server.model.player.board.WarehouseDepot WarehouseDepot}.
     *
     * @param startPos int value position of the {@link it.polimi.ingsw.server.model.Resource NetworkResource} to move.
     * @param endPos  int value of the destination position for the {@link it.polimi.ingsw.server.model.Resource NetworkResource} to move.
     *
     * @return true if starting position is among ones in inside {@link it.polimi.ingsw.server.model.player.board.WarehouseDepot WarehouseDepot} and
     * ending position is among available ones inside {@link it.polimi.ingsw.server.model.player.board.WarehouseDepot WarehouseDepot}
     */
    private boolean validateResourceToMove(int startPos, int endPos){
        return endPos >= 0 && currentPlayerPersonalBoard.availableMovingPositionsForResourceAt(startPos)
                .anyMatch(position -> position == endPos);
    }

    /**
     * @return true if given initial and final positions are valid ones, otherwise false.
     */
    private boolean validatePositions(){
        return validateResourceToMove(startPos, endPos);
    }

    private boolean checkResourceAvailability(){
        return startPos >= -4 && startPos < 0 && gameModel.getCurrentPlayer().getPersonalBoard().getDiscardBox().getNumberOf(Resource.fromInt(startPos + 4)) > 0;
    }

}
