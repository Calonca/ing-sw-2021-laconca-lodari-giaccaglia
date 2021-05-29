package it.polimi.ingsw.server.controller.strategy.cardmarket;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardPositionEvent;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.production.ProductionCardCell;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * This implementation allows the user to place the selected card in an available space. Upon calling this is
 * precalculated that at least one correct option exists
 */
public class ChoosingSpaceForDevelopmentCard implements GameStrategy {

    List<Element> elementsToUpdate = new ArrayList<>();

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event)
    {

        ProductionCardCell chosenCell=gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(((ChooseCardPositionEvent) event).getCardPositionInPersonalBoard());
        chosenCell.addToTop(gamemodel.getPurchasedCard());

        elementsToUpdate.add(Element.SimpleCardCells);
        elementsToUpdate.add(Element.SimpleCardShop);

        return new Pair<>(State.FINAL_PHASE, elementsToUpdate);

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