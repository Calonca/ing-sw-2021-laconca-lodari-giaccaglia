package it.polimi.ingsw.network.messages.clienttoserver.events.setupphaseevent;
import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.model.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Client side {@link Event} created during {@link State#SETUP_PHASE SETUP_PHASE} when player initialization is
 * performed and has to be processed to accomplish server-side client validation.
 */
public class SetupPhaseEvent extends Event {

    protected Pair<Integer, Integer> [] resources; //key = position, value = resource number
    protected List<Integer> chosenLeaders;
    protected List<Integer> discardedLeaders;
    protected int initialResources;
    protected int initialChosenLeaders;
    protected int initialDiscardedLeaders;
    protected int actualSize = 0;
    protected int playerNumber;

    /**
     * Client side {@link Event} constructor invoked when {@link State#SETUP_PHASE SETUP_PHASE}
     * action is performed.
     *
     * @param initialResources int value representing initial amount of resources player can choose, according to his <em>playerNumber</em>
     * @param initialChosenLeaders int value representing initial number of <em>leaderCards</em> each player can have.
     * @param initialDiscardedLeaders int value representing amount of <em>leaderCards</em> each player has to diascard after setup phase.
     * @param playerNumber int value ranging from 1 to 4 representing the number of the player performing setup phase.
     */
    public SetupPhaseEvent(int initialResources, int initialChosenLeaders, int initialDiscardedLeaders, int playerNumber){
        this.initialResources = initialResources;
        this.initialChosenLeaders = initialChosenLeaders;
        this.initialDiscardedLeaders = initialDiscardedLeaders;
        this.chosenLeaders = new ArrayList<>(initialChosenLeaders);
        this.discardedLeaders = new ArrayList<>(initialDiscardedLeaders);
        this.playerNumber = playerNumber;
        resources = new Pair[initialResources];
    }

    /**
     * Default constructor for handling {@link it.polimi.ingsw.server.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent #SERVERSetupPhaseEvent}
     * server side equivalent inherited Event handler
     */
    public SetupPhaseEvent(){}

    /**
     * Method to select initial player resources during setup phase.
     * @param resource {@link Pair} whose key represents resource position inside <em>Warehouse</em>, whereas the value
     * represent resource int corresponding value, according to {@link it.polimi.ingsw.server.model.Resource Resources}
     * encoding.
     */
    public void addResource(Pair<Integer, Integer> resource) {
        resources[actualSize++] = new Pair<>(resource.getKey(), resource.getValue());
    }

    /**
     * Method to select initial player <em>leaderCards</em> during setup phase.
     * @param leaderNumber int value representing <em>leaderCard</em> number.
     */
    public void addChosenLeader(int leaderNumber){
        chosenLeaders.add(leaderNumber);
        }

    /**
     * Method to select <em>leaderCards</em> to discard during setup phase.
     * @param leaderNumber int value representing <em>leaderCard</em> number.
     */
    public void addDiscardedLeader(int leaderNumber){
        discardedLeaders.add(leaderNumber);
    }

}
