package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.network.util.Util;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * On game start, each player will choose a quantity of resources based on their turn order
 */
public class Setup implements GameStrategy {

    List<Element> elementsToUpdate = new ArrayList<>();
    int currentPlayerNumber;

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event)
    {
        int[] toAdd = new int[4];

        SetupPhaseEvent clientEvent = ((SetupPhaseEvent) event);

        List<UUID> chosenLeaders = clientEvent.getChosenLeaders();
        currentPlayerNumber = gamemodel.getPlayerIndex(gamemodel.getCurrentPlayer());

        Arrays.fill((toAdd),0);

        for (Pair<Integer, Integer> resourceIntegerPair : clientEvent.getChosenResources())
            toAdd[resourceIntegerPair.getKey()] += resourceIntegerPair.getValue();

        gamemodel.discardLeadersOnSetupPhase(chosenLeaders);
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(toAdd);
        gamemodel.getCurrentPlayer().getPersonalBoard().resetDiscardBox();

        IntStream.range(0, Util.initialFaithPoints(currentPlayerNumber)).forEach(i -> gamemodel.getCurrentPlayer().moveOnePosition());

        buildElementsList();

        return new Pair<>(State.IDLE, elementsToUpdate);

    }

    private void buildElementsList(){

        elementsToUpdate.add(Element.SimpleWareHouseLeadersDepot);
        elementsToUpdate.add(Element.SimplePlayerLeaders);

        if(Util.initialFaithPoints(currentPlayerNumber)!=0)
            elementsToUpdate.add(Element.SimpleFaithTrack);
    }

}
