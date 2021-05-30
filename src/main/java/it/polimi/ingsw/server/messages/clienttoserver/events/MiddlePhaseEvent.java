package it.polimi.ingsw.server.messages.clienttoserver.events;

import it.polimi.ingsw.server.model.states.State;
import it.polimi.ingsw.server.model.GameModel;

/**
 * Client side {@link it.polimi.ingsw.network.messages.clienttoserver.events.Event Event} created when a {@link State#MIDDLE_PHASE MIDDLE_PHASE}
 * game turn action is performed and has to be processed to accomplish server-side client validation.
 */
public class MiddlePhaseEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.MiddlePhaseEvent implements Validable {

    public int getChoice() {
        return choice;
    }

    public MiddlePhaseEvent(int choice) {
        super(choice);
    }

    /**
     * @param gameModel {@link GameModel} of the event's current game on which event validation has to be performed.
     * @return true if current match associated with this {@link GameModel} has started, otherwise false.
     */
    @Override
    public boolean validate(GameModel gameModel) {
        return isGameStarted(gameModel);
    }

}
