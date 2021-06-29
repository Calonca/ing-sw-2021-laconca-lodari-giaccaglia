package it.polimi.ingsw.server.messages.clienttoserver.events.productionevent;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.production.Production;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ChooseResourcesForProductionEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ChooseResourcesForProductionEvent implements Validable {

    /**
     * {@link GameModel} of the event's current game on which event validation has to be performed.
     */
    private transient PersonalBoard currentPlayerPersonalBoard;
    private final int[] resourcesToConvertArray = new int[7];
    private Set<Pair<Integer, Integer>> selectedResourcesPairSet;  // Pair : key -> resource position ; value -> number of selected resources
    int lastSelectedProductionPosition;

    /**
     * Server-side initializer to setup common attributes among {@link State#MIDDLE_PHASE MIDDLE_PHASE}
     * events.
     * @param gameModel {@link GameModel} of the event's current game on which event validation has to be performed.
     */
    private void initializeMiddlePhaseEventValidation(GameModel gameModel){
        currentPlayerPersonalBoard = gameModel.getCurrentPlayer().getPersonalBoard();
        lastSelectedProductionPosition = currentPlayerPersonalBoard.getLastSelectedProductionPosition();
        this.currentPlayerPersonalBoard = gameModel.getCurrentPlayer().getPersonalBoard();
    }

    @Override
    public boolean validate(GameModel gameModel) {

        initializeMiddlePhaseEventValidation(gameModel);
        buildResourcesPairSet();
        buildResourcesArray();



        return isGameStarted(gameModel)
                && checkTypeOfResourcesToConvert()
                && checkProductionRequirements()
                && checkResourcesPositionIndexes()
                && checkPositionsValidity()
                && checkAvailabilityOfResourcesToConvert()
               // && checkPositionOfResourcesToConvert()
                && checkThatResourcesAreNotSelected();

    }

    private boolean checkThatResourcesAreNotSelected(){

        return selectedResourcesPairSet.stream().allMatch(
                pair -> {
                    if(pair.getKey()>=-8 && pair.getKey()<=-5){
                        int localPos = pair.getKey() + 8;
                        Resource resource = Resource.fromIntFixed(localPos);
                     //   int alreadySelectedResources = currentPlayerPersonalBoard.getStrongBox().getNSelected(resource);
                        int availableResources = currentPlayerPersonalBoard.getStrongBox().getNumberOfNotSelected(resource);

                        return availableResources >= pair.getValue();

                    }
                    else {
                        return !currentPlayerPersonalBoard.getWarehouseLeadersDepots().getSelected(pair.getKey());
                    }

                }
        );

    }

    /**
     * @return true if chosen {@link it.polimi.ingsw.server.model.Resource resources} actually match
     * {@link GameModel#currentPlayer currentPlayer} {@link it.polimi.ingsw.server.model.player.board.PersonalBoard PersonalBoard}
     * deposits availability, otherwise false.
     */
    private boolean checkAvailabilityOfResourcesToConvert(){
        return currentPlayerPersonalBoard.hasResources(resourcesToConvertArray);
    }

    private boolean checkPositionOfResourcesToConvert(){

        return IntStream.range(0, inputPositionsToChoose.size())
                .boxed()
                .allMatch(
                        index -> {

                            int globalPos = inputPositionsToChoose.get(index);
                            int localPos = globalPos>=0 ? globalPos : globalPos + 8;
                            return currentPlayerPersonalBoard.getResourceAtPosition(inputPositionsToChoose.get(index))
                                .equals
                                        (Resource.fromIntFixed(localPos));
                        });

    }

    private boolean checkTypeOfResourcesToConvert(){
        return inputPositionsToChoose.stream().noneMatch(resourcePos -> currentPlayerPersonalBoard.getResourceAtPosition(resourcePos).equals(Resource.EMPTY));
    }

    private boolean checkResourcesPositionIndexes(){
        return inputPositionsToChoose.stream().noneMatch(i -> ( i<-8 || (i>-5 && i<0) ));
    }

    private boolean checkPositionsValidity(){

        return inputPositionsToChoose.stream().allMatch(
                position -> {

                    int positionOccurrences = inputPositionsToChoose.stream().filter(positionToFind -> positionToFind.equals(position)).mapToInt(positionToFind -> 1).sum();

                    if(position>=-8 && position<-4) {
                            Resource resourceAtPos = currentPlayerPersonalBoard.getResourceAtPosition(position);
                            return currentPlayerPersonalBoard.getStrongBox().getNumberOfNotSelected(resourceAtPos) >= positionOccurrences;
                        }
                    else if(positionOccurrences>1)
                        return false;

                    return true;
                });
    }

    private boolean checkProductionRequirements() {

        Optional<Production> optOfSelectedProduction = currentPlayerPersonalBoard.getProductionFromPosition(lastSelectedProductionPosition);

        if (optOfSelectedProduction.isPresent()) {

            Production selectedProduction = optOfSelectedProduction.get();

            if(selectedProduction.choiceCanBeMade())
                return checkProductionWithChoicesRequirements(selectedProduction);

            return Arrays.equals(selectedProduction.getInputs(), resourcesToConvertArray);

        }

        return false;
    }

    private boolean checkChosenResourceValidity(){

        return outputResourcesToChoose.stream().noneMatch(resourceInt -> Resource.fromIntFixed(resourceInt).equals(Resource.EMPTY));

    }

    private boolean checkProductionWithChoicesRequirements(Production production) {

        boolean isValidationOk = false;

        if (production.choiceCanBeMadeOnInput()) {
            isValidationOk = (inputPositionsToChoose.size() == production.getNumOfResInInput());
        }

        if (production.choiceCanBeMadeOnOutput()) {

            isValidationOk = (outputResourcesToChoose.size() == production.getNumOfResInOutput()) && checkChosenResourceValidity();

        }

        return isValidationOk;
    }

    private void buildResourcesPairSet(){

        selectedResourcesPairSet = inputPositionsToChoose.stream().map(
                position -> {
                    int positionOccurrences = inputPositionsToChoose.stream().filter(positionToFind -> positionToFind.equals(position)).mapToInt(positionToFind -> 1).sum();
                    return new Pair<>(position, positionOccurrences);
                }

        ).collect(Collectors.toSet());


    }

    private void buildResourcesArray(){

             //   resource, numOf
        Set<Pair<Integer, Integer>> pairSetWithLocalPositions = inputPositionsToChoose.stream().map(
                position -> {
                    int positionOccurrences = inputPositionsToChoose.stream().filter(positionToFind -> positionToFind.equals(position)).mapToInt(positionToFind -> 1).sum();
                    int resourceNumber = currentPlayerPersonalBoard.getResourceAtPosition(position).getResourceNumber();
                    return new Pair<>(resourceNumber, positionOccurrences);
                }

        ).collect(Collectors.toSet());


        for (Pair<Integer, Integer> resourceIntegerPair : pairSetWithLocalPositions) {

            resourcesToConvertArray[resourceIntegerPair.getKey()] += resourceIntegerPair.getValue();
        }

    }

    public Set<Pair<Integer, Integer>> getSelectedResourcesPairSet() {

        return inputPositionsToChoose.stream().map(
                position -> {

                    int positionOccurrences = inputPositionsToChoose.stream().filter(positionToFind -> positionToFind.equals(position)).mapToInt(positionToFind -> 1).sum();
                    return new Pair<>(position, positionOccurrences);
                }

        ).collect(Collectors.toSet());
    }

    public List<Integer> getOutputResourcesPairList(){
        return outputResourcesToChoose;
    }






}
