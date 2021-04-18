package it.polimi.ingsw.server.controller.strategy.cardmarket;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.production.ProductionCardCell;
import it.polimi.ingsw.server.model.player.State;

/**
 * This implementation allows the user to place the selected card in an available space. Upon calling this is
 * precalculated that at least one correct option exists
 */
public class ChoosingSpace extends CardMarketStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT CHOOSECARDPOSITIONEVENT
        //MESSAGE IS 2
        ProductionCardCell chosencell=gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(2);
        if(chosencell.isSpotAvailable(gamemodel.getPurchasedCard()))
            chosencell.addToTop(gamemodel.getPurchasedCard());
        else
            return State.CHOOSING_POSITION_FOR_DEVCARD;

        return State.FINAL_PHASE;
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