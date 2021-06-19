package it.polimi.ingsw.server.controller.strategy.cardmarket;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardEvent;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * The chosen card gets acquired. If it's not possible, the user returns to the market view.
 * Received message is int, cards are mapped as follows INT%3 IS THE LEVEL, INT/3 IS THE COLOR
 * If the color is not out of stock, the method proceeds to acquire the card, and if it should not have
 * been possible, it will get back to the previous state
 */
public class AcquiringDevelopmentCard implements GameStrategy {

    List<Element> elementsToUpdate = new ArrayList<>(0);

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event)

    {
        int level = ((ChooseCardEvent) event).getCardLevel();
        int color = ((ChooseCardEvent) event).getCardColorNumber();

        gamemodel.purchaseCardFromCardShop(DevelopmentCardColor.fromInt(color),level);

        elementsToUpdate.add(Element.SimpleCardShop);
        elementsToUpdate.add(Element.SimpleCardCells);
        elementsToUpdate.add(Element.SelectablePositions);


        return new Pair<>(State.CHOOSING_RESOURCES_FOR_DEVCARD, elementsToUpdate);
    }
}

