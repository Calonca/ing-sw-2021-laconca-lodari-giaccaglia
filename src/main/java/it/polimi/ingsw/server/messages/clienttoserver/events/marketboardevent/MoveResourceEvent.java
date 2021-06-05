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
        return isGameStarted(model) && checkResourceAvailability() && validateResourceToMove(startPos, endPos);
    }

    /*

    /**
     * Method to validate initial and final positions for {@Resource Resources} taken from
     * {@link it.polimi.ingsw.server.model.market.MarketBoard MarketBoard} and placed in
     * {@link it.polimi.ingsw.server.model.player.board.WarehouseDepot WarehouseDepot}
     *
     * @param endPos  int value of the destination position for the {@link Resource Resource} to move.
     *
     * @return true if starting position is among ones in the picked {@link Resource Resources} and
     * ending position is among available ones inside {@link it.polimi.ingsw.server.model.player.board.WarehouseDepot WarehouseDepot}
     *
    private boolean validateNewResourceToAdd(int endPos){
        return endPos >=0;

    }

    */

    /**
     * Method to validate initial and final position for {@link Resource Resources} moved
     * from a spot to another available inside {@link it.polimi.ingsw.server.model.player.board.WarehouseDepot WarehouseDepot}.
     *
     * @param startPos int value position of the {@link Resource Resource} to move.
     * @param endPos  int value of the destination position for the {@link Resource Resource} to move.
     *
     * @return true if starting position is among ones in inside {@link it.polimi.ingsw.server.model.player.board.WarehouseDepot WarehouseDepot} and
     * ending position is among available ones inside {@link it.polimi.ingsw.server.model.player.board.WarehouseDepot WarehouseDepot}
     */
    private boolean validateResourceToMove(int startPos, int endPos){
        return endPos >= 0
                && endPos < currentPlayerPersonalBoard.getWarehouseLeadersDepots().getNumOfCellsInAllDepots()
                && currentPlayerPersonalBoard.availableMovingPositionsForResourceAt(startPos)
                .anyMatch(position -> position == endPos);
    }
    private boolean checkResourceAvailability(){

        if(startPos<0)
            return startPos >= -4 && gameModel.getCurrentPlayer().getPersonalBoard().getDiscardBox().getNumberOf(Resource.fromIntFixed(startPos + 4)) > 0;

        else if(startPos < currentPlayerPersonalBoard.getWarehouseLeadersDepots().getNumOfCellsInAllDepots())
            return !gameModel.getCurrentPlayer().getPersonalBoard().getResourceAtPosition(startPos).equals(Resource.EMPTY);

        else return false;
    }

    public int getStartPos(){
        return startPos;
    }

    public int getEndPos(){
        return endPos;
    }

}
