package it.polimi.ingsw.network.messages.clienttoserver.events;

import it.polimi.ingsw.server.model.states.State;

/**
 * Client side {@link Event} created when a {@link State#MIDDLE_PHASE MIDDLE_PHASE}
 * game turn action is performed and has to be processed to accomplish server-side client validation.
 */
public class MiddlePhaseEvent extends Event {

    protected int choice;

    public MiddlePhaseEvent() {
    }

    public MiddlePhaseEvent(int choice) {
        this.choice = choice;
    }
}
