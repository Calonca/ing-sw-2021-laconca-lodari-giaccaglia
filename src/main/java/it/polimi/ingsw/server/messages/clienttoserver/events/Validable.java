package it.polimi.ingsw.server.messages.clienttoserver.events;

import it.polimi.ingsw.server.model.GameModel;

public interface Validable {

    boolean validate(GameModel gameModel);

    default boolean isGameStarted(GameModel gameModel){
        return gameModel.isGameStarted();
    }

}
