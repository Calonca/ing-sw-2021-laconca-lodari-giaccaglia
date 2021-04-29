package it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent;

import it.polimi.ingsw.network.messages.servertoclient.State;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;


/**
 * Client side {@link it.polimi.ingsw.network.messages.clienttoserver.events.Event Event} created when {@link GameModel#currentPlayer currentPlayer} has to pick {@link it.polimi.ingsw.server.model.Resource Resources}
 * from his {@link it.polimi.ingsw.server.model.player.board.PersonalBoard PersonalBoard} deposits during
 * {@link State#CHOOSING_RESOURCES_FOR_DEVCARD CHOOSING_RESOURCES_FOR_DEVCARD} phase by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class ChooseResourceForCardShopEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseResourceForCardShopEvent implements Validable {

    @Override
    public boolean validate(GameModel gameModel) {
        initializeMiddlePhaseEventValidation(gameModel);
        return isGameStarted(gameModel) && checkResourceRequirements() && validateDevCardRequirements();
    }

    /**
     * @return true if chosen {@link it.polimi.ingsw.server.model.Resource resources} actually match
     * {@link GameModel#currentPlayer currentPlayer} {@link it.polimi.ingsw.server.model.player.board.PersonalBoard PersonalBoard}
     * deposits availability, otherwise false.
     */
    private boolean checkResourceRequirements(){
        return currentPlayerPersonalBoard.hasResources(chosenResources);
    }

}
