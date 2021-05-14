package it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent;

import it.polimi.ingsw.server.controller.states.State;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;

/**
 * Client side {@link it.polimi.ingsw.network.messages.clienttoserver.events.Event Event} created when {@link GameModel#currentPlayer currentPlayer}
 * wants to skip {@link it.polimi.ingsw.server.model.player.leaders.Leader Leaders} selection during {@link State#SHOWING_LEADERS_INITIAL SHOWING_LEADERS_INITIAL} or
 * {@link State#SHOWING_LEADERS_FINAL SHOWING_LEADERS_FINAL} phase by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class SkipLeaderEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.leaderphaseevent.SkipLeaderEvent implements Validable {

    @Override
    public boolean validate(GameModel gameModel) {
        return isGameStarted(gameModel);
    }
}
