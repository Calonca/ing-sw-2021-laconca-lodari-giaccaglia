package it.polimi.ingsw.server.messages.clienttoserver.events.productionevent;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;

import java.util.Arrays;

public class FinalProductionPhaseEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.FinalProductionPhaseEvent implements Validable {

    private transient PersonalBoard currentPlayerPersonalBoard;

    @Override
    public boolean validate(GameModel gameModel) {

        currentPlayerPersonalBoard = gameModel.getCurrentPlayer().getPersonalBoard();

        return isGameStarted(gameModel)
                && validateActionToPerform();
    }

    private boolean validateActionToPerform(){

        if(actionToPerform == 0)
            return Arrays.stream(currentPlayerPersonalBoard.getSelectedProduction()).anyMatch(isActive -> isActive);

        else return true;

    }

    public int getActionToPerform(){
        return actionToPerform;
    }


}
