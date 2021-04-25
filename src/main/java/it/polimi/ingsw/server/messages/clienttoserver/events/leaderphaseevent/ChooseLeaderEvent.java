package it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.leaders.Leader;
import it.polimi.ingsw.server.model.player.leaders.LeaderState;

/**
 * Client side {@link it.polimi.ingsw.network.messages.clienttoserver.events.Event Event} created when {@link GameModel#currentPlayer currentPlayer}
 * wants to see his {@link it.polimi.ingsw.server.model.player.leaders.Leader Leaders}
 * during {@link it.polimi.ingsw.server.model.player.State#SHOWING_LEADERS_INITIAL SHOWING_LEADERS_INITIAL} or
 * {@link it.polimi.ingsw.server.model.player.State#SHOWING_LEADERS_FINAL SHOWING_LEADERS_FINAL} phase by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class ChooseLeaderEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.leaderphaseevent.ChooseLeaderEvent implements Validable, LeaderValidation {

    @Override
    public boolean validate(GameModel model) {
        initializeInitialOrFinalPhaseEvent(model);
        return  isGameStarted(gameModel) && validateLeaderNumber(model, leaderNumber)
                && validateLeaderAvailability(model, leaderNumber);
    }

}
