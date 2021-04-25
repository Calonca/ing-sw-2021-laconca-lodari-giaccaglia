package it.polimi.ingsw.network.messages.clienttoserver.events;


import it.polimi.ingsw.server.model.GameModel;

/**
 * Client side {@link Event} created when a {@link it.polimi.ingsw.server.model.player.State#INITIAL_PHASE INITIAL_PHASE}
 * or {@link it.polimi.ingsw.server.model.player.State#FINAL_PHASE FINAL_PHASE} game turn action is performed and has
 * to be processed to accomplish server-side client validation. Both game phases events are handled by the same
 * validation handler since most of phases are in common, as seen in {@link it.polimi.ingsw.server.model.player.State States}.
 */
public class InitialOrFinalPhaseEvent extends Event {

    /**
     * {@link GameModel} of the event's current game on which event validation has to be performed.
     */
    protected GameModel gameModel;

    /**
     * Server-side initializer to setup common attributes among {@link it.polimi.ingsw.server.model.player.State#INITIAL_PHASE INITIAL_PHASE}
     * or {@link it.polimi.ingsw.server.model.player.State#FINAL_PHASE FINAL_PHASE} events.
     * @param gameModel {@link GameModel} of the event's current game on which event validation has to be performed.
     */
    protected void initializeInitialOrFinalPhaseEvent(GameModel gameModel) {
        this.gameModel = gameModel;
    }


}
