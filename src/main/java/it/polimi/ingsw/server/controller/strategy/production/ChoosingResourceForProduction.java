package it.polimi.ingsw.server.controller.strategy.production;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.ChooseResourcesForProductionEvent;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * This implementation allows the user to sequentially select the input and output optional choices. The inputs are
 * chosen first, and it has already been calculated that there will be enough basic resources to complete the choice.
 * The user can reset mid-choice, allowing them to perform it from the start again.
 */
public class ChoosingResourceForProduction implements GameStrategy {

    private transient List<Element> elementsToUpdate = new ArrayList<>();

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event){

            PersonalBoard personalBoard = gamemodel.getCurrentPlayer().getPersonalBoard();

            ChooseResourcesForProductionEvent chooseResourcesForProductionEvent = (ChooseResourcesForProductionEvent) event;
            List<Pair<Integer, Integer>> chosenResources = chooseResourcesForProductionEvent.getSelectedResourcesPairList();

            chosenResources.forEach(
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

            personalBoard.decreaseRemainingNumOfSelectedProductionsWithoutChoices();

            int remainingNumOfSelectedProductionsWithoutChoices = personalBoard.getRemainingNumOfSelectedProductionsWithoutChoices();

            elementsToUpdate.add(Element.SimpleWareHouseLeadersDepot);
            elementsToUpdate.add(Element.SimpleFaithTrack);

            if(remainingNumOfSelectedProductionsWithoutChoices == 0){

                return new Pair<>(State.FINAL_PHASE, elementsToUpdate);
            }


            return new Pair<>(State.CHOOSING_RESOURCE_FOR_PRODUCTION, elementsToUpdate);

    }


}
