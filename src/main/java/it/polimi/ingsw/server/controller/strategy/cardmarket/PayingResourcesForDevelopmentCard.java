package it.polimi.ingsw.server.controller.strategy.cardmarket;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseResourceForCardShopEvent;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * This implementation allows the user to choose one resource at a time. As soon as the requirements are met,
 * the resources are taken from their Depots and Strongbox, transitioning to the next game phase.
 */
public class PayingResourcesForDevelopmentCard implements GameStrategy {

   private  final List<Element> elementsToUpdate = new ArrayList<>();

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event)
    {
        PersonalBoard playerBoard = gamemodel.getCurrentPlayer().getPersonalBoard();

        List<Integer>  resourcesToRemove = ((ChooseResourceForCardShopEvent) event).getChosenResources();

        resourcesToRemove.forEach(playerBoard::selectResourceAt);

        playerBoard.removeSelectedResources();

        elementsToUpdate.add(Element.SIMPLE_STRONG_BOX);
        elementsToUpdate.add(Element.SIMPLE_WARE_HOUSE_LEADERS_DEPOT);
        elementsToUpdate.add(Element.SIMPLE_CARD_CELLS);

        return new Pair<>(State.CHOOSING_POSITION_FOR_DEVCARD, elementsToUpdate);

    }
}