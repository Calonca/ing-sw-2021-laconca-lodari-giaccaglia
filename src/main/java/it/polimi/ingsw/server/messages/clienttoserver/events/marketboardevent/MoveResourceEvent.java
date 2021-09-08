package it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;

/**
 * Client side {@link Event} created when player wants move {@link it.polimi.ingsw.server.model.Resource Resources} picked
 * from {@link it.polimi.ingsw.server.model.market.MarketBoard} and move the ones inside his Warehouse deposits
 * when {@link State#CHOOSING_POSITION_FOR_RESOURCES CHOOSING_POSITION_FOR_RESOURCES} game turn action is performed and
 * has to be processed to accomplish server-side client validation.
 */
public class MoveResourceEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.MoveResourceEvent implements Validable {

    private transient PersonalBoard playerPersonalBoard;

    /**
     * Server-side initializer to setup common attributes among {@link State#MIDDLE_PHASE MIDDLE_PHASE}
     * events.
     * @param gameModel {@link GameModel} of the event's current game on which event validation has to be performed.
     */
    private boolean initializeMiddlePhaseEventValidation(GameModel gameModel){

        if(gameModel.getPlayer(playerNickname).isEmpty())
            return false;

        this.playerPersonalBoard = gameModel.getPlayer(playerNickname).get().getPersonalBoard();
        return true;
    }

    @Override
    public boolean validate(GameModel model) {

        return initializeMiddlePhaseEventValidation(model) &&
                isGameStarted(model) &&
                checkResourceAvailability() &&
                validateResourceToMove(startPos, endPos);
    }

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

        if(startPos == endPos)
            return true;

        Resource resourceToMove = playerPersonalBoard.getResourceAtPosition(startPos);

        return endPos >= 0
                && endPos < playerPersonalBoard.getWarehouseLeadersDepots().getNumOfCellsInAllDepots()
                && playerPersonalBoard.getWarehouseLeadersDepots().availableMovingPositionsForResource(resourceToMove)
                .anyMatch(position -> position == endPos);


    }
    private boolean checkResourceAvailability(){



        if(startPos<0) {

            if(startPos == endPos)
                return true;

            return startPos >= -4 && playerPersonalBoard.getDiscardBox().getNumberOf(Resource.fromIntFixed(startPos + 4)) > 0;
        }

        else if(startPos < playerPersonalBoard.getWarehouseLeadersDepots().getNumOfCellsInAllDepots()) {

            if(startPos==endPos)
                return true;

            return !playerPersonalBoard.getResourceAtPosition(startPos).equals(Resource.EMPTY);
        }

        else return false;
    }

    public int getStartPos(){
        return startPos;
    }

    public int getEndPos(){
        return endPos;
    }

}
