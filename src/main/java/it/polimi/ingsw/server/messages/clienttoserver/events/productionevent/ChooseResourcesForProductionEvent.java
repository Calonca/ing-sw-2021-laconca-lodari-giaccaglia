package it.polimi.ingsw.server.messages.clienttoserver.events.productionevent;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.List;

public class ChooseResourcesForProductionEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ChooseResourcesForProductionEvent implements Validable {

    /**
     * {@link GameModel} of the event's current game on which event validation has to be performed.
     */
    private transient PersonalBoard currentPlayerPersonalBoard;

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
        return checkChosenResourceAvailability() && checkResourcesToConvert();
    }

    private boolean checkChosenResourceAvailability(){
        int[] resource = new int[1];
        resource[0] = chosenResource;
        return currentPlayerPersonalBoard.hasResources(resource);
    }

    private boolean checkAvailabilityOfResourcesToConvert(){
        return currentPlayerPersonalBoard.hasResources(resourcesToDiscard.stream().mapToInt(Pair::getValue).toArray());
    }

    private boolean checkPositionOfResourcesToDiscard(){
      return resourcesToDiscard.stream()
              .allMatch(
                      pair -> currentPlayerPersonalBoard.getResourceAtPosition(pair.getKey()).equals(Resource.fromInt(pair.getValue())));
    }

    private boolean checkResourcesType(){
        return resourcesToDiscard.stream().anyMatch(resource -> Resource.fromInt(resource.getValue()).equals(Resource.EMPTY));
    }

    private boolean checkResourcesToConvert(){
        return checkResourcesType() && checkAvailabilityOfResourcesToConvert() && checkPositionOfResourcesToDiscard();
    }

    public int getChosenResourceForProduction(){
        return chosenResource;
    }

    public List<Pair<Integer, Integer>> getResourcesToDiscard(){
        return resourcesToDiscard;
    }

}
