package it.polimi.ingsw.server.messages.clienttoserver.events.productionevent;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;

public class ProductionEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ProductionEvent implements Validable{

    @Override
    public boolean validate(GameModel gameModel) {
        return isGameStarted(gameModel);
    }
}
