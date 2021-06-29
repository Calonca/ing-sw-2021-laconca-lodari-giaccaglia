package it.polimi.ingsw.server.controller.strategy.production;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.ChooseResourcesForProductionEvent;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This implementation allows the user to sequentially select the input and output optional choices. The inputs are
 * chosen first, and it has already been calculated that there will be enough basic resources to complete the choice.
 * The user can reset mid-choice, allowing them to perform it from the start again.
 */
public class ChoosingResourceForProduction implements GameStrategy {

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event){

            List<Element> elementsToUpdate = new ArrayList<>();
            PersonalBoard personalBoard = gamemodel.getCurrentPlayer().getPersonalBoard();

            ChooseResourcesForProductionEvent chooseResourcesForProductionEvent = (ChooseResourcesForProductionEvent) event;
            Set<Pair<Integer, Integer>> chosenInputResourcesPositions = chooseResourcesForProductionEvent.getSelectedResourcesPairSet();
            List<Integer> chosenOutputResources = chooseResourcesForProductionEvent.getOutputResourcesPairList();

            chosenInputResourcesPositions.forEach(
                    pair -> {

                        int resPos = pair.getKey();

                        int numOfResToSelect = pair.getValue();

                        if(resPos>=-8 && resPos<-4) {
                            for(int i=0; i<numOfResToSelect; i++)

                                personalBoard.getStrongBox().selectResourceAt(resPos);

                        }
                        else personalBoard.getWarehouseLeadersDepots().selectResourceAt(resPos);
                    }

            );

            chosenOutputResources.forEach(resourceNumber -> personalBoard.performChoiceOnOutput(Resource.fromIntFixed(resourceNumber)));

            elementsToUpdate.add(Element.SimpleWareHouseLeadersDepot);
            elementsToUpdate.add(Element.SimpleFaithTrack);
            elementsToUpdate.add(Element.SimpleProductions);
            elementsToUpdate.add(Element.SimpleCardCells);
            elementsToUpdate.add(Element.SelectablePositions);


            return new Pair<>(State.CHOOSING_PRODUCTION, elementsToUpdate);

    }


}
