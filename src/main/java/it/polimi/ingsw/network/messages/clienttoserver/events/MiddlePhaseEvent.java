package it.polimi.ingsw.network.messages.clienttoserver.events;

import it.polimi.ingsw.server.model.states.State;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;

/**
 * Client side {@link Event} created when a {@link State#MIDDLE_PHASE MIDDLE_PHASE}
 * game turn action is performed and has to be processed to accomplish server-side client validation.
 */
public class MiddlePhaseEvent extends Event {

    /**
     * {@link GameModel} of the event's current game on which event validation has to be performed.
     */
    protected GameModel gameModel;
    protected PersonalBoard currentPlayerPersonalBoard;

    /**
     * Server-side initializer to setup common attributes among {@link State#MIDDLE_PHASE MIDDLE_PHASE}
     * events.
     * @param gameModel {@link GameModel} of the event's current game on which event validation has to be performed.
     */
    protected void initializeMiddlePhaseEventValidation(GameModel gameModel){
        this.gameModel = gameModel;
        this.currentPlayerPersonalBoard = gameModel.getCurrentPlayer().getPersonalBoard();
    }

}
