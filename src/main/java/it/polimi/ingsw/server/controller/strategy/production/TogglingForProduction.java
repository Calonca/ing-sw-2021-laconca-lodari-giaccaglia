package it.polimi.ingsw.server.controller.strategy.production;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.ToggleProductionAtPosition;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * This implementation allows the user to toggle selections on the available productions. When a choice is needed, the
 * used needs to perform it to completion before being able to toggle another production.
 */
public class TogglingForProduction implements GameStrategy {

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event) {

        List<Element> elementsToUpdate = new ArrayList<>();
        PersonalBoard playerBoard;

        ToggleProductionAtPosition toggleProductionAtPosition = (ToggleProductionAtPosition) event;
        playerBoard = gamemodel.getCurrentPlayer().getPersonalBoard();
        int productionPosition = toggleProductionAtPosition.getProductionPosition();
        playerBoard.toggleSelectProductionAt(productionPosition);


        elementsToUpdate.add(Element.SimpleWareHouseLeadersDepot);
        elementsToUpdate.add(Element.SimpleStrongBox);
        elementsToUpdate.add(Element.SimpleProductions);


        int lastSelectedProduction = playerBoard.getLastSelectedProductionPosition();

        if (lastSelectedProduction == productionPosition) {         // player enabled a production
            elementsToUpdate.add(Element.SelectablePositions);
            elementsToUpdate.add(Element.SimpleCardCells);
            return new Pair<>(State.CHOOSING_RESOURCE_FOR_PRODUCTION, elementsToUpdate);
         }


        return new Pair<>(State.CHOOSING_PRODUCTION, elementsToUpdate);

    }

}
