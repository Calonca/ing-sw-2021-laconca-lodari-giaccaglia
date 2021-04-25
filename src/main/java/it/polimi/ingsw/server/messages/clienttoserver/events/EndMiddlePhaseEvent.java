package it.polimi.ingsw.server.messages.clienttoserver.events;

import it.polimi.ingsw.server.model.GameModel;

public class EndMiddlePhaseEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.EndMiddlePhaseEvent implements Validable{

    @Override
    public boolean validate(GameModel gameModel) {
        return isGameStarted(gameModel);
    }
    
}
