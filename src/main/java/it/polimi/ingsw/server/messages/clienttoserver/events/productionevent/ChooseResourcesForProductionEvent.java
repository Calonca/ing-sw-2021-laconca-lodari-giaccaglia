package it.polimi.ingsw.server.messages.clienttoserver.events.productionevent;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChooseResourcesForProductionEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ChooseResourcesForProductionEvent implements Validable {

    /**
     * {@link GameModel} of the event's current game on which event validation has to be performed.
     */
    private transient PersonalBoard currentPlayerPersonalBoard;
    List<Integer> resourcePositions;
    private final int[] resourcesToDiscardArray = new int[4];
    private int cardPos;

    /**
     * Server-side initializer to setup common attributes among {@link State#MIDDLE_PHASE MIDDLE_PHASE}
     * events.
     * @param gameModel {@link GameModel} of the event's current game on which event validation has to be performed.
     */
    private void initializeMiddlePhaseEventValidation(GameModel gameModel){
        this.currentPlayerPersonalBoard = gameModel.getCurrentPlayer().getPersonalBoard();
    }

    @Override
    public boolean validate(GameModel gameModel) {
        initializeMiddlePhaseEventValidation(gameModel);
        buildResourcesArray();
        resourcePositions = new ArrayList<>(resourcesToDiscard.keySet());


        return isGameStarted(gameModel)
                && checkChosenResourceValidity()
                && checkTypeOfResourcesToConvert()
                && checkProductionRequirements()
                && checkResourcesPositionIndexes()
                && checkResourcePositionUniqueness()
                && checkAvailabilityOfResourcesToConvert()
                && checkPositionOfResourcesToConvert();
    }

    private boolean checkChosenResourceValidity(){
        return !isBasicProduction || !(Resource.fromIntFixed(chosenResource).equals(Resource.EMPTY) && Resource.fromIntFixed(chosenResource).equals(Resource.FAITH));
    }

    /**
     * @return true if chosen {@link it.polimi.ingsw.server.model.Resource resources} actually match
     * {@link GameModel#currentPlayer currentPlayer} {@link it.polimi.ingsw.server.model.player.board.PersonalBoard PersonalBoard}
     * deposits availability, otherwise false.
     */
    private boolean checkAvailabilityOfResourcesToConvert(){
        return currentPlayerPersonalBoard.hasResources(resourcesToDiscardArray);
    }

    private boolean checkPositionOfResourcesToConvert(){
        return resourcesToDiscard.keySet().stream()
                .anyMatch(position ->
                        !currentPlayerPersonalBoard.getResourceAtPosition(position).equals(Resource.fromIntFixed(resourcesToDiscard.get(position))));
    }

    private boolean checkTypeOfResourcesToConvert(){
        return resourcesToDiscard.values().stream().noneMatch(resource -> Resource.fromIntFixed(resource).equals(Resource.EMPTY));
    }

    private boolean checkResourcesPositionIndexes(){
        return resourcePositions.stream().anyMatch(i -> ( i<-8 || (i>-5 && i<0) ));
    }

    private boolean checkResourcePositionUniqueness(){
        return resourcePositions.stream().distinct()
                .count() == resourcePositions.size();
    }

    private boolean checkProductionRequirements(){
        return !isBasicProduction()
                && currentPlayerPersonalBoard.getProductionFromCardPosition(cardPos).isPresent()
                && Arrays.equals(currentPlayerPersonalBoard.getProductionFromCardPosition(cardPos).get().getInputs(), resourcesToDiscardArray);
    }

    private void buildResourcesArray(){
        Map<Integer, List<Integer>> resourcesMap = resourcesToDiscard.values().stream()
                .collect(Collectors.groupingBy(value -> value,
                        Collectors.mapping(Function.identity(), Collectors.toList())));

        List<Pair<Integer, Integer>> chosenResourcesList = resourcesMap
                .keySet().stream()
                .map(key ->
                        new Pair<>(key, resourcesMap.get(key).stream().reduce(0, Integer::sum)))
                .collect(Collectors.toList());


        for (Pair<Integer, Integer> resourceIntegerPair : chosenResourcesList)
            resourcesToDiscardArray[resourceIntegerPair.getKey()] += resourceIntegerPair.getValue();

    }

    public boolean isBasicProduction(){
        return isBasicProduction;
    }

    public int getChosenResourceForProduction(){
        return chosenResource;
    }

    public Map<Integer, Integer> getChosenResources(){ return resourcesToDiscard;}



}
