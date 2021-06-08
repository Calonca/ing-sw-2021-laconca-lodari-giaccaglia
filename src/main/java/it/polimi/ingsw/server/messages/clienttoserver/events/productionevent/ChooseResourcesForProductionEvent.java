package it.polimi.ingsw.server.messages.clienttoserver.events.productionevent;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class ChooseResourcesForProductionEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ChooseResourcesForProductionEvent implements Validable {

    /**
     * {@link GameModel} of the event's current game on which event validation has to be performed.
     */
    private transient PersonalBoard currentPlayerPersonalBoard;
    private final int[] resourcesToConvertArray = new int[4];
    List<Pair<Integer, Integer>> chosenResourcesPairList;
    private int productionPos;

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
        Integer resNUm = 0;
        Integer pos = 0;

        AtomicBoolean result = new AtomicBoolean(false);
        currentPlayerPersonalBoard.firstProductionSelectedWithChoice().ifPresentOrElse((p)->{
                if (p.choiceCanBeMade()&&p.choiceCanBeMadeOnInput()&&!pos.equals(null)&&resNUm.equals(null)){
                    result.set(!currentPlayerPersonalBoard.getResourceAtPosition(pos).equals(Resource.EMPTY));
                } else if(p.choiceCanBeMadeOnOutput()&&pos.equals(null)&&!resNUm.equals(null)){
                    Resource resIfChoosingOutput = Resource.fromIntFixed(resNUm);
                    result.set(!resIfChoosingOutput.equals(Resource.EMPTY));
                }else result.set(false);
                },
            ()-> {
                Resource r = currentPlayerPersonalBoard.getResourceAtPosition(pos);
                //Todo check if the resource is in the production non selected input
                result.set(currentPlayerPersonalBoard.remainingToSelectForProduction() > 0 &&
                                !r.equals(Resource.EMPTY)
                    );
            }
        );



        initializeMiddlePhaseEventValidation(gameModel);
        buildResourcesArray();

        return isGameStarted(gameModel)
                && checkChosenResourceValidity()
                && checkTypeOfResourcesToConvert()
                && checkProductionRequirements()
                && checkResourcesPositionIndexes()
                && checkPositionsValidity()
                && checkAvailabilityOfResourcesToConvert()
                && checkPositionOfResourcesToConvert();
                //TODO CHECK RESOURCES NOT SELECTED
    }

    private boolean checkChosenResourceValidity(){
        return productionPos>0 || !(Resource.fromIntFixed(chosenResource).equals(Resource.EMPTY) && Resource.fromIntFixed(chosenResource).equals(Resource.FAITH));
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
        return positionsOfResourcesToConvert.stream()
                .anyMatch(position ->
                        !currentPlayerPersonalBoard.getResourceAtPosition(position).equals(Resource.fromIntFixed(positionsOfResourcesToConvert.get(position))));
    }

    private boolean checkTypeOfResourcesToConvert(){
        return positionsOfResourcesToConvert.stream().noneMatch(resourcePos -> currentPlayerPersonalBoard.getResourceAtPosition(resourcePos).equals(Resource.EMPTY));
    }

    private boolean checkResourcesPositionIndexes(){
        return positionsOfResourcesToConvert.stream().anyMatch(i -> ( i<-8 || (i>-5 && i<0) ));
    }

    private boolean checkPositionsValidity(){

        return positionsOfResourcesToConvert.stream().allMatch(
                position -> {

                    int positionOccurrences = positionsOfResourcesToConvert.stream().filter(positionToFind -> positionToFind.equals(position)).mapToInt(positionToFind -> 1).sum();

                    if(position>=-8 && position<-4) {
                            Resource resourceAtPos = currentPlayerPersonalBoard.getResourceAtPosition(position);
                            return currentPlayerPersonalBoard.getStrongBox().getNumberOf(resourceAtPos) == positionOccurrences;
                        }
                    else if(positionOccurrences>1)
                        return false;

                    return true;
                });
    }

    private boolean checkProductionRequirements(){
        return  productionPos>0
                && currentPlayerPersonalBoard.getProductionFromPosition(productionPosition).isPresent()
                && Arrays.equals(currentPlayerPersonalBoard.getProductionFromPosition(productionPos).get().getInputs(), resourcesToConvertArray);
    }

    private void buildResourcesPairList(){

        chosenResourcesPairList = positionsOfResourcesToConvert.stream().map(
                position -> {

                    int positionOccurrences = positionsOfResourcesToConvert.stream().filter(positionToFind -> positionToFind.equals(position)).mapToInt(positionToFind -> 1).sum();

                    return new Pair<>(position, positionOccurrences);
                }

        ).collect(Collectors.toList());

    }

    private void buildResourcesArray(){
        for (Pair<Integer, Integer> resourceIntegerPair : chosenResourcesPairList)
            resourcesToConvertArray[resourceIntegerPair.getKey()] += resourceIntegerPair.getValue();
    }


    public boolean isBasicProduction(){
        return productionPos==0;
    }

    public int getChosenResourceForBasicProduction(){
        return chosenResource;
    }

    public List<Integer>getPositionsOfResourcesToConvert(){ return positionsOfResourcesToConvert;}

    public List<Pair<Integer, Integer>> getChosenResourcesPairList(){ return chosenResourcesPairList;}



}
