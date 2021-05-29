package it.polimi.ingsw.server.messages.clienttoserver.events;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.model.GameModel;

public class TestEvent extends Event implements Validable {
    @Override
    public boolean validate(GameModel gameModel) {
        return false;
    }
}
