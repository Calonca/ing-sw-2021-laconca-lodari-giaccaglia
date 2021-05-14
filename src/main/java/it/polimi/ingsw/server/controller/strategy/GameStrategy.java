package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.controller.states.State;

public interface GameStrategy
{

    State execute(GameModel gamemodel, Validable event) throws EventValidationFailedException;
}
