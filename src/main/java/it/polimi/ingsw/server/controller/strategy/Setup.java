package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.network.util.Util;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * On game start, each player will choose a quantity of resources based on their turn order
 */
public class Setup implements GameStrategy {

    List<Element> elementsToUpdate = new ArrayList<>();
    int indexOfPlayerInSetupPhase;

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event)
    {

        SetupPhaseEvent clientEvent = ((SetupPhaseEvent) event);

        List<UUID> chosenLeaders = clientEvent.getChosenLeaders();
        indexOfPlayerInSetupPhase = clientEvent.getPlayerNumber();
        Player playerInSetupPhase = gamemodel.getPlayer(indexOfPlayerInSetupPhase).get();

        List<Pair<Integer, Resource>> resources = clientEvent.getChosenResources();


        for(Pair<Integer, Resource> resource : resources)
            playerInSetupPhase.getPersonalBoard().getWarehouseLeadersDepots().addResource(resource);


        gamemodel.discardLeadersOnSetupPhase(playerInSetupPhase, chosenLeaders);
        IntStream.range(0, Util.initialFaithPoints(indexOfPlayerInSetupPhase)).forEach(i -> playerInSetupPhase.moveOnePosition());

        buildElementsList();

        return new Pair<>(State.IDLE, elementsToUpdate);

    }

    private void buildElementsList(){

        elementsToUpdate.add(Element.SimpleWareHouseLeadersDepot);
        elementsToUpdate.add(Element.SimplePlayerLeaders);

        if(Util.initialFaithPoints(indexOfPlayerInSetupPhase)!=0)
            elementsToUpdate.add(Element.SimpleFaithTrack);
    }

}
