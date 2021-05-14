package it.polimi.ingsw.network.messages.clienttoserver.events;


import it.polimi.ingsw.server.model.states.State;

/**
 * Client side {@link Event} created when a {@link State#INITIAL_PHASE INITIAL_PHASE}
 * or {@link State#FINAL_PHASE FINAL_PHASE} game turn action is performed and has
 * to be processed to accomplish server-side client validation. Both game phases events are handled by the same
 * validation handler since most of phases are in common, as seen in {@link State States}.
 */
public class InitialOrFinalPhaseEvent extends Event {

}
