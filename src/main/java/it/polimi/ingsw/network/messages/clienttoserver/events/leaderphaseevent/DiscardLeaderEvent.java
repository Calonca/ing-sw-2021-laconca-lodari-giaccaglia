package it.polimi.ingsw.network.messages.clienttoserver.events.leaderphaseevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.servertoclient.State;
import it.polimi.ingsw.server.model.GameModel;

/**
 * Client side {@link Event} created when {@link GameModel#currentPlayer currentPlayer} wants to discard a
 * {@link it.polimi.ingsw.server.model.player.leaders.Leader Leader}
 * during {@link State#SHOWING_LEADERS_INITIAL SHOWING_LEADERS_INITIAL} or
 * {@link State#SHOWING_LEADERS_FINAL SHOWING_LEADERS_FINAL} by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class DiscardLeaderEvent extends ChooseLeaderEvent {

    /**
     * Client side {@link Event} constructor invoked when{@link State#SHOWING_LEADERS_INITIAL SHOWING_LEADERS_INITIAL} or
     * {@link State#SHOWING_LEADERS_FINAL SHOWING_LEADERS_FINAL} phase action is performed.
     *
     * @param leaderNumber chosen {@link it.polimi.ingsw.server.model.player.leaders.Leader Leader} position in
     *{@link GameModel#currentPlayer currentPlayer}'s {@link it.polimi.ingsw.server.model.player.Player#leaders Leaders} List
     */
    public DiscardLeaderEvent(int leaderNumber){
        this.leaderNumber = leaderNumber;
    }

    /**
     * Default constructor for handling {@link it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent.DiscardLeaderEvent #SERVERDiscardLeaderEvent}
     * server side equivalent inherited Event handler
     */
    public DiscardLeaderEvent(){}
}
