package it.polimi.ingsw.server.controller.strategy.cardmarket;

import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.controller.strategy.Final;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.production.ProductionCardCell;
import it.polimi.ingsw.server.model.State;

/**
 * This implementation allows the user to place the selected card in an available space. Upon calling this is
 * precalculated that at least one correct option exists
 */
public class ChoosingSpaceForDevelopmentCard implements GameStrategy {
    public State execute(GameModel gamemodel, Validable event) throws EventValidationFailedException
    {
        //ON EVENT CHOOSECARDPOSITIONEVENT
        //MESSAGE IS 2
        ProductionCardCell chosencell=gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(2);
        if(chosencell.isSpotAvailable(gamemodel.getPurchasedCard()))
            chosencell.addToTop(gamemodel.getPurchasedCard());
        else
            return State.CHOOSING_POSITION_FOR_DEVCARD;

        return new Final().execute(gamemodel, event);
    }
}
 //   /**
 //    * Useful for when a player or the server disconnects to send the needed information to the other players
 //    */
 //   RESET {
 //       @Override
 //       public String serialize(GameModel gameModel) {
 //           String[] toSerialize = Arrays.stream(State.values())
 //                   .filter((state -> !state.equals(State.RESET)))
 //                   .map(state -> state.serialize(gameModel))
 //                   .toArray(String[]::new);
 //           String allStates = new GsonBuilder().create().toJson(toSerialize);
 //           return gameModel.getCurrentPlayer().getCurrentState().toString().concat(allStates);
 //       }
 //   };