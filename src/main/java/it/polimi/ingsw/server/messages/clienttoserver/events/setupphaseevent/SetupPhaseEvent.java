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
import java.util.stream.IntStream;

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
     * @return true if chosen and discarded <em>leaderCards</em> are available among ones in {@link GameModel}, otherwise false.
     */
    private boolean validateLeaders() {
        boolean validationOk = false;
        for (Integer leaderNumber : chosenLeaders) {
            validationOk = gamemodel.isLeaderAvailable(leaderNumber);
        }

        for (Integer leaderNumber : discardedLeaders) {
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
        return (playerNumber == 3 || playerNumber == 4) ? resourcesFromPlayerNumber - 1 == resourcesAmount :
                resourcesFromPlayerNumber == resourcesAmount;
    }

    private boolean validateResources() {
        return validateResourceNumber() && validateResourcesAmount() && validatePositionsUniqueness() && validateResourcePositions();
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
     * @return true if chosen resource positions int values are all unique, otherwise false.
     */
    private boolean validatePositionsUniqueness() {
        List<Integer> resourcePositions = Arrays.stream(resources).map(Pair::getKey).collect(Collectors.toList());
        return resourcePositions.stream().distinct()
                .count() == resourcePositions.size();
    }
    /**
     * @return true if chosen resources positions are available among ones in the <em>Warehouse</em>, otherwise false.
     */
    private boolean validateResourcePositions() {
        List<List<Integer>> availablePositionsAsList = IntStream.range(0,3)
                .boxed().map(i ->  new int[i+1])
                .collect(Collectors.toList())
                .stream()
                .map(array -> IntStream.range(0, array.length)
                        .boxed()
                        .mapToInt(i -> -1)
                        .toArray())
                .collect(Collectors.toList())
                .stream()
                .map(i -> Arrays.stream(i)
                        .boxed()
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());


            return  Arrays.stream(resources).noneMatch(resource -> {
                boolean value = true;
                if (resource.getKey() < 1) {
                    availablePositionsAsList.get(resource.getKey()).set(resource.getKey(), resource.getValue());
                    value = false;
                } else if (resource.getKey() < 3) {
                    {
                        if (availablePositionsAsList.get(1).stream().noneMatch(i -> (i != -1 && !i.equals(resource.getValue())))) {
                            availablePositionsAsList.get(1).set(resource.getKey() - 1, resource.getValue());
                            value = false;
                        } else
                            value = true;
                    }
                } else if (resource.getKey() < 6) {
                    if (availablePositionsAsList.get(2).stream().noneMatch(i -> (i != -1 && !i.equals(resource.getValue())))) {
                        availablePositionsAsList.get(2).set(resource.getKey() - 3, resource.getValue());
                        value = false;
                    } else
                        value = true;
                }
                return value;
            });
    }
}
