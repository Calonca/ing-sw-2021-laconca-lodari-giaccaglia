package it.polimi.ingsw.server.messages.clienttoserver.events.productionevent;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;

public class ChooseProductionAtPositionEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ChooseProductionAtPositionEvent implements Validable {

    @Override
    public boolean validate(GameModel gameModel) {
        initializeMiddlePhaseEventValidation(gameModel);
        return isGameStarted(gameModel) && validateProductionPosition() && validateProductionRequirements();
    }

    private boolean validateProductionPosition(){
            return (currentPlayerPersonalBoard.getAvailableProductions().length > productionPosition && productionPosition>=0);
    }

    private boolean validateProductionRequirements(){
            return currentPlayerPersonalBoard.getAvailableProductions()[productionPosition];
    }

}
