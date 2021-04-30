package it.polimi.ingsw.server.messages.clienttoserver.events.setupphaseevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.model.State;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Client side {@link Event} created during {@link State#SETUP_PHASE SETUP_PHASE} when player initialization is
 * performed and has to be processed to accomplish server-side client validation.
 */
public class SetupPhaseEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent implements Validable {

    /**
     * {@link GameModel} of the event's current game on which event validation has to be performed.
     */
    private GameModel gamemodel;

    @Override
    public boolean validate(GameModel gameModel) {
        this.gamemodel = gameModel;
        return isGameStarted(gameModel) && validatePlayerNumber() && validateLeaders() && validateResources();
    }

    /**
     * @return true if {@link SetupPhaseEvent#playerNumber} associated with this event matches the {@link GameModel#currentPlayer}, otherwise
     * false.
     */
    private boolean validatePlayerNumber() {
        return playerNumber == gamemodel.getPlayerIndex(gamemodel.getCurrentPlayer());
    }

    /**
     * @return true if chosen <em>leaderCards</em> are available among ones in {@link GameModel}, otherwise false.
     */
    private boolean validateLeaders() {
        boolean validationOk = false;
        for (Integer leaderNumber : leaders) {
            validationOk = gamemodel.isLeaderAvailable(leaderNumber);
        }
        return validationOk;
    }

    /**
     * @return true if number of chosen resources matches the ones expected according to the
     * {@link SetupPhaseEvent#playerNumber}, otherwise false.
     */
    private boolean validateResourcesAmount() {

        int playerNumber = gamemodel.getPlayerIndex(gamemodel.getCurrentPlayer());
        int resourcesFromPlayerNumber = (11 % playerNumber);
        int resourcesAmount = resources.length;
        return (playerNumber == 3) ? resourcesFromPlayerNumber - 1 == resourcesAmount :
                resourcesFromPlayerNumber == resourcesAmount;
    }

    private boolean validateResources() {
        return validateResourceNumber() && validateResourcesAmount() && validateResourcePositions();
    }

    /**
     * @return true if chosen resources int values corresponds to existing {@link Resource Resources}, otherwise false
     */
    private boolean validateResourceNumber() {
        List<Integer> resourceNumbers = Arrays.stream(resources).map(Pair::getValue).collect(Collectors.toList());
        return resourceNumbers.stream()
                .noneMatch(number -> Resource.fromInt(number)
                        .equals(Resource.EMPTY));
    }

    /**
     * @return true if chosen resources positions are available among ones in the <em>Warehouse</em>, otherwise false.
     */
    private boolean validateResourcePositions() {
        List<Pair<Integer, Integer>>resourcesList = Arrays.stream(resources).collect(Collectors.toList());

        return resourcesList
                .stream()
                .allMatch(resource ->
                        gamemodel
                                .getCurrentPlayer()
                                .getPersonalBoard()
                                .availableMovingPositionsForResource(Resource.fromInt(resource.getValue()))
                                .anyMatch(position ->position == resource.getValue()));
    }
}
