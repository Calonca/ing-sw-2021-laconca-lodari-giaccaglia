package it.polimi.ingsw.network.messages.clienttoserver.events.leaderphaseevent;


import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.clienttoserver.events.InitialOrFinalPhaseEvent;
import it.polimi.ingsw.server.model.states.State;
import it.polimi.ingsw.server.model.GameModel;

import java.util.UUID;

/**
 * Client side {@link it.polimi.ingsw.network.messages.clienttoserver.events.Event Event} created when {@link GameModel#currentPlayer currentPlayer}
 * wants to activate his {@link it.polimi.ingsw.server.model.player.leaders.Leader Leaders}
 * during {@link State#SHOWING_LEADERS_INITIAL SHOWING_LEADERS_INITIAL} or
 * {@link State#SHOWING_LEADERS_FINAL SHOWING_LEADERS_FINAL} phase by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class PlayLeaderEvent extends InitialOrFinalPhaseEvent {

    /**
     * Default constructor for handling {@link it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent.PlayLeaderEvent} #SERVERPlayerLeaderEvent server side equivalent inherited Event handler
     */
    public PlayLeaderEvent(){}
}
