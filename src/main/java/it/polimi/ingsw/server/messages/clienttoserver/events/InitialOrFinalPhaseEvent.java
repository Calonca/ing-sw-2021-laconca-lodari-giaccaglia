package it.polimi.ingsw.server.messages.clienttoserver.events;

import it.polimi.ingsw.server.model.GameModel;

/**
 * Client side {@link it.polimi.ingsw.network.messages.clienttoserver.events.Event Event} created when a {@link it.polimi.ingsw.server.model.player.State#INITIAL_PHASE INITIAL_PHASE}
 * or {@link it.polimi.ingsw.server.model.player.State#FINAL_PHASE FINAL_PHASE} game turn action is performed and has
 * to be processed to accomplish server-side client validation. Both game phases events are handled by the same
 * validation handler since most of phases are in common, as seen in {@link it.polimi.ingsw.server.model.player.State States}.
 */
public class InitialOrFinalPhaseEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.InitialOrFinalPhaseEvent implements Validable {

    @Override
    public boolean validate(GameModel gameModel) {
        return isGameStarted(gameModel);
    }

}
