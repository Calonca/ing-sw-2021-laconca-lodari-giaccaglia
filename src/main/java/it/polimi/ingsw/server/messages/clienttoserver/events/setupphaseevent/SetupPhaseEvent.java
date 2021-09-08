package it.polimi.ingsw.server.messages.clienttoserver.events.setupphaseevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.util.Util;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
    protected transient GameModel gamemodel;

    @Override
    public boolean validate(GameModel gameModel) {
        this.gamemodel = gameModel;
        return isGameStarted(gameModel) && validatePlayerNumber() && validateLeaders() && validateResources();
    }

    /**
     * @return true if {@link SetupPhaseEvent#playerNumber} associated with this event corresponds to an online player,
     * otherwise false
     */
    private boolean validatePlayerNumber() {
        return gamemodel.getOnlinePlayers().containsKey(playerNumber);
    }

    /**
     * @return true if discarded <em>leaderCards</em> are available among ones in {@link GameModel}, otherwise false.
     */
    private boolean validateLeaders() {


        if( gamemodel.getPlayer(playerNumber).isPresent() && chosenLeaders.size() == initialDiscardedLeaders && initialDiscardedLeaders == 2)
        {
            Player player = gamemodel.getPlayer(playerNumber).get();
            boolean validationOk;
            for (UUID leaderId: chosenLeaders) {
                validationOk = player.isLeaderAvailable(leaderId);
                if(!validationOk) return false;
            }

            return true;
        }
        else return false;
    }

    /**
     * @return true if number of chosen resources matches the ones expected according to the
     * {@link SetupPhaseEvent#playerNumber}, otherwise false.
     */
    private boolean validateResourcesAmount() {
        int resourcesFromPlayerNumber = Util.resourcesToChooseOnSetup(playerNumber);
        int resourcesAmount = resources.length;
        return resourcesFromPlayerNumber == resourcesAmount;
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
                .noneMatch(number -> Resource.fromIntFixed(number)
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
                    if (availablePositionsAsList.get(1).stream().noneMatch(i -> (i != -1 && !i.equals(resource.getValue())))) {
                        availablePositionsAsList.get(1).set(resource.getKey() - 1 , resource.getValue());
                        value = false;
                    } else
                        value = true;
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

    //key = position in warehouseLeadersDepot, value = resource number

    public List<UUID> getChosenLeaders(){
        return chosenLeaders;
    }

    public List<Pair<Integer, Resource>> getChosenResources(){
        return Arrays.stream(resources).map(pair -> new Pair<>(pair.getKey(), Resource.fromIntFixed(pair.getValue()))).collect(Collectors.toList());
    }

    public int getPlayerNumber(){
        return playerNumber;
    }

}
