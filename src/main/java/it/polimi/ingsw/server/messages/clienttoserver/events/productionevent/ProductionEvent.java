package it.polimi.ingsw.server.messages.clienttoserver.events.productionevent;

import it.polimi.ingsw.server.messages.clienttoserver.events.MiddlePhaseEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;

public class ProductionEvent extends MiddlePhaseEvent {

    @Override
    public boolean validate(GameModel gameModel) {
        return isGameStarted(gameModel);
    }
}
