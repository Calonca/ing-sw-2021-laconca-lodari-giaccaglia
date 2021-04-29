package it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent;

import it.polimi.ingsw.network.messages.servertoclient.State;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;

/**
 * Client side {@link it.polimi.ingsw.network.messages.clienttoserver.events.Event Event} created when {@link GameModel#currentPlayer currentPlayer}
 * wants to activate his {@link it.polimi.ingsw.server.model.player.leaders.Leader Leaders}
 * during {@link State#SHOWING_LEADERS_INITIAL SHOWING_LEADERS_INITIAL} or
 * {@link State#SHOWING_LEADERS_FINAL SHOWING_LEADERS_FINAL} phase by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class PlayLeaderEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.leaderphaseevent.PlayLeaderEvent implements Validable, LeaderValidation{

    @Override
    public boolean validate(GameModel model) {
        initializeInitialOrFinalPhaseEvent(model);
        return  isGameStarted(gameModel) && validateLeaderAvailability(model, leaderNumber)
                && validateLeaderNumber(model, leaderNumber) && validateLeaderRequirements(model, leaderNumber);
    }

}
