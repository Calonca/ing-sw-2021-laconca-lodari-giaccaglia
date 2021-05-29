package it.polimi.ingsw.server.messages.clienttoserver.events.productionevent;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;

public class ChooseProductionAtPositionEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ChooseProductionAtPositionEvent implements Validable {


    private transient PersonalBoard currentPlayerPersonalBoard;

    /**
     * Server-side initializer to setup common attributes among {@link State#MIDDLE_PHASE MIDDLE_PHASE}
     * events.
     * @param gameModel {@link GameModel} of the event's current game on which event validation has to be performed.
     */
    private void initializeMiddlePhaseEventValidation(GameModel gameModel){
        this.currentPlayerPersonalBoard = gameModel.getCurrentPlayer().getPersonalBoard();
    }


    @Override
    public boolean validate(GameModel gameModel) {
        initializeMiddlePhaseEventValidation(gameModel);
        return isGameStarted(gameModel) && validateProductionPosition() && validateProductionRequirements();
    }

    private boolean validateProductionPosition(){
            return (currentPlayerPersonalBoard.getAvailableProductions().length > cardPosition && cardPosition>=0 && cardPosition <= 3);
    }

    private boolean validateProductionRequirements(){
            return currentPlayerPersonalBoard.getAvailableProductions()[cardPosition+1];
    }

    public int getProductionPosition(){
        return cardPosition;
    }

}
