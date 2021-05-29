package it.polimi.ingsw.network.messages.clienttoserver.events.leaderphaseevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.clienttoserver.events.InitialOrFinalPhaseEvent;
import it.polimi.ingsw.server.model.states.State;
import it.polimi.ingsw.server.model.GameModel;

import java.util.UUID;

/**
 * Client side {@link it.polimi.ingsw.network.messages.clienttoserver.events.Event Event} created when {@link GameModel#currentPlayer currentPlayer}
 * wants to see his {@link it.polimi.ingsw.server.model.player.leaders.Leader Leaders}
 * during {@link State#SHOWING_LEADERS_INITIAL SHOWING_LEADERS_INITIAL} or
 * {@link State#SHOWING_LEADERS_FINAL SHOWING_LEADERS_FINAL} phase by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class ChooseLeaderEvent extends InitialOrFinalPhaseEvent {


    /**
     * Represents the chosen {@link it.polimi.ingsw.server.model.player.leaders.Leader Leader} position in
     * {@link GameModel#currentPlayer currentPlayer}'s {@link it.polimi.ingsw.server.model.player.Player#leaders Leaders} List
     */
    protected UUID leaderId;

    /**
     * Client side {@link Event} constructor invoked when {@link State#SHOWING_LEADERS_INITIAL SHOWING_LEADERS_INITIAL} or
     * {@link State#SHOWING_LEADERS_FINAL SHOWING_LEADERS_FINAL} phase action is performed.
     * @param leaderNumber chosen {@link it.polimi.ingsw.server.model.player.leaders.Leader Leader} position in
     *{@link GameModel#currentPlayer currentPlayer}'s {@link it.polimi.ingsw.server.model.player.Player#leaders Leaders} List
     */
    public ChooseLeaderEvent(UUID leaderNumber){
        this.leaderId = leaderNumber;
    }

    /**
     * Default constructor for handling {@link it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent.ChooseLeaderEvent} #SERVERChooseLeaderEvent server side equivalent inherited Event handler
     */
    public ChooseLeaderEvent(){}

}
