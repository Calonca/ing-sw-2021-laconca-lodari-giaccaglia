package it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;

public class FinalDevCardPhase extends it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.FinalDevCardPhase implements Validable {

    @Override
    public boolean validate(GameModel model) {
        return isGameStarted(model);
    }

}
