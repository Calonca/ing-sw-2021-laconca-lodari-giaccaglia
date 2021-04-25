package it.polimi.ingsw.network.messages.clienttoserver.events.leaderphaseevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.InitialOrFinalPhaseEvent;
import it.polimi.ingsw.server.model.GameModel;

/**
 * Client side {@link it.polimi.ingsw.network.messages.clienttoserver.events.Event Event} created when {@link GameModel#currentPlayer currentPlayer}
 * wants to skip {@link it.polimi.ingsw.server.model.player.leaders.Leader Leaders} selection during {@link it.polimi.ingsw.server.model.player.State#SHOWING_LEADERS_INITIAL SHOWING_LEADERS_INITIAL} or
 * {@link it.polimi.ingsw.server.model.player.State#SHOWING_LEADERS_FINAL SHOWING_LEADERS_FINAL} phase by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class SkipLeaderEvent extends InitialOrFinalPhaseEvent {

}
