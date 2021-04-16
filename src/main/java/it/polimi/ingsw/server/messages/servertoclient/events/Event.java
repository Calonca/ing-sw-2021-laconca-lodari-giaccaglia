package it.polimi.ingsw.server.messages.servertoclient.events;

import it.polimi.ingsw.server.model.GameModel;

public interface Event {
    boolean validate(GameModel gameModel);
}
