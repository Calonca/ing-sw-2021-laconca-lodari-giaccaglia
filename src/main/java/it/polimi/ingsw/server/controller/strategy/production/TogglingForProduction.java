package it.polimi.ingsw.server.controller.strategy.production;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.ToggleProductionAtPosition;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.production.Production;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This implementation allows the user to toggle selections on the available productions. When a choice is needed, the
 * used needs to perform it to completion before being able to toggle another production.
 */
public class TogglingForProduction implements GameStrategy {

    private transient List<Element> elementsToUpdate = new ArrayList<>();
    private transient PersonalBoard playerBoard;

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event) {

        ToggleProductionAtPosition toggleProductionAtPosition = (ToggleProductionAtPosition) event;
        int actionToPerform = toggleProductionAtPosition.getActionToPerform();
        playerBoard = gamemodel.getCurrentPlayer().getPersonalBoard();


        if (actionToPerform == 2)    //end selection -> choose resources
            return new Pair<>(State.CHOOSING_RESOURCE_FOR_PRODUCTION, elementsToUpdate);

        int productionPosition = toggleProductionAtPosition.getProductionPosition();

        if (actionToPerform == 1) {  //select production

            playerBoard.toggleSelectProductionAt(productionPosition);

            Optional<Production> optOfProductionWithChoice = playerBoard.firstProductionSelectedWithChoice();

            if (optOfProductionWithChoice.isPresent()
                    && (optOfProductionWithChoice.get().equals(playerBoard.getProductionFromPosition(productionPosition).get()))  //verify if last selected production has choices

            ) {

                List<Integer> positionsOfResourcesToConvert = toggleProductionAtPosition.getPositionsOfResourcesToConvertForSpecialProduction();

                positionsOfResourcesToConvert.forEach(playerBoard::performChoiceOnInput);

                if (optOfProductionWithChoice.get().choiceCanBeMadeOnOutput()) {

                    int resToObtain = toggleProductionAtPosition.getChosenResourceForBasicProduction();

                    playerBoard.performChoiceOnOutput(Resource.fromInt(resToObtain));
                }


            }

        } else if (actionToPerform == 0) {  //deselect production


            Optional<Production> optOfProductionWithChoice = playerBoard.firstProductionSelectedWithChoice();

            //if it is a production with choices , deselect choices on input and remove choice on output

            if (optOfProductionWithChoice.isPresent() && optOfProductionWithChoice.get().equals(playerBoard.getProductionFromPosition(productionPosition).get())) {

                Production productionToRevert = optOfProductionWithChoice.get();

                List<Integer> positionsOfResourcesToDeselect = playerBoard.getPosOfChosenResourcesOnInputForProduction(productionPosition);

                positionsOfResourcesToDeselect.forEach(playerBoard::deselectResourceAt);

                playerBoard.resetHistoryOfProductionWithInputsToChoose(productionPosition);

                productionToRevert.resetChoice();

            }

            playerBoard.deselectProductionAt(productionPosition);

        }

        elementsToUpdate.add(Element.SimpleWareHouseLeadersDepot);
        elementsToUpdate.add(Element.SimpleStrongBox);
        elementsToUpdate.add(Element.SimpleProductions);

        if (playerBoard.remainingToSelectForProduction() == 0)
            return new Pair<>(State.CHOOSING_RESOURCE_FOR_PRODUCTION, elementsToUpdate);


        return new Pair<>(State.CHOOSING_PRODUCTION, elementsToUpdate);

    }

}
