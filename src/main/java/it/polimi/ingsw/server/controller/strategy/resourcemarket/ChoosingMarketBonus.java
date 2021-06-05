package it.polimi.ingsw.server.controller.strategy.resourcemarket;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.ChooseWhiteMarbleConversionEvent;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * This method allows the player's choice to be validated. They will pick a resource, and if available
 * in the Market Bonus effect, it will apply and convert one white marble. The return state will be
 * the one to choose the bonus, as long as there are white marbles remaining
 */
public class ChoosingMarketBonus implements GameStrategy {

    List<Element> elementsToUpdate = new ArrayList<>();

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event)
    {

       int resourceNumber =  ((ChooseWhiteMarbleConversionEvent) event).getResourceNumber();
       gamemodel.convertWhiteMarbleInPickedLine(resourceNumber);

        elementsToUpdate.add(Element.SimpleMarketBoard);

        if(gamemodel.areThereWhiteMarblesInPickedLine())
            return new Pair<>(State.CHOOSING_WHITEMARBLE_CONVERSION, elementsToUpdate);

        elementsToUpdate.add(Element.SimpleStrongBox);
        return new Pair<>(State.CHOOSING_POSITION_FOR_RESOURCES, elementsToUpdate);

    }

}
