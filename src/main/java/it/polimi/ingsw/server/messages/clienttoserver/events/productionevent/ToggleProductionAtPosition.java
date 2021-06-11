package it.polimi.ingsw.server.messages.clienttoserver.events.productionevent;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.production.Production;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;

import java.util.List;
import java.util.Optional;

public class ToggleProductionAtPosition extends it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ToggleProductionAtPosition implements Validable {

    private transient PersonalBoard currentPlayerPersonalBoard;
    private transient Production selectedProduction;

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

        return isGameStarted(gameModel)
                && checkProductionType()
                && validateProductionRequirements();

    }


    private boolean checkProductionType(){
        Optional<Production> optOfSelectedProduction = currentPlayerPersonalBoard.getProductionFromPosition(productionPosition);
        if (optOfSelectedProduction.isPresent()) {
            selectedProduction = optOfSelectedProduction.get();
            return true;
        }
        return false;
    }


    private boolean validateProductionRequirements(){
    return currentPlayerPersonalBoard.getAvailableProductions()[productionPosition];
    }

    public int getProductionPosition(){
        return productionPosition;
    }



}
