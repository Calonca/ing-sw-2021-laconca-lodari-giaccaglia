package it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.model.states.State;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;

/**
 * Client side {@link Event} created when {@link GameModel#currentPlayer currentPlayer} wants to discard a
 * {@link it.polimi.ingsw.server.model.player.leaders.Leader Leader}
 * during {@link State#SHOWING_LEADERS_INITIAL SHOWING_LEADERS_INITIAL} or
 * {@link State#SHOWING_LEADERS_FINAL SHOWING_LEADERS_FINAL} by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class DiscardLeaderEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.leaderphaseevent.DiscardLeaderEvent implements Validable, LeaderValidation {


    @Override
    public boolean validate(GameModel model) {
        return  isGameStarted(model)  && validateLeaderNumber(model, leaderNumber)
                && validateLeaderAvailability(model, leaderNumber);

    }
}
