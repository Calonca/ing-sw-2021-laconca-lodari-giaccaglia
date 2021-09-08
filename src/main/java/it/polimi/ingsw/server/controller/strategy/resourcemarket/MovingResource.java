package it.polimi.ingsw.server.controller.strategy.resourcemarket;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.MoveResourceEvent;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * This implementation allows the user to move one at a time the resources from one of the depot lines to an empty
 * space. Before being able to proceed, the user needs to place all of the resources from the market either in the
 * DiscardBox or in one of their depot lines. For each resource in the DiscardBox all the other players get a faith point
 */
public class MovingResource implements GameStrategy {

    private final List<Element> elementsToUpdate = new ArrayList<>();

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event)
    {

        MoveResourceEvent moveResourceEvent= (MoveResourceEvent) event;

        int startPos = moveResourceEvent.getStartPos();
        int endPos = moveResourceEvent.getEndPos();
        String playerNickname = moveResourceEvent.getPlayerNickname();
        Player playerSendingEvent = gamemodel.getPlayer(playerNickname).get();

        if(startPos!=endPos)
           playerSendingEvent.getPersonalBoard().move(startPos, endPos);


        elementsToUpdate.add(Element.SIMPLE_WARE_HOUSE_LEADERS_DEPOT);
        elementsToUpdate.add(Element.SIMPLE_DISCARD_BOX);
        elementsToUpdate.add(Element.SIMPLE_PRODUCTIONS);

        State currentState = playerSendingEvent.getCurrentState();

        return new Pair<>(currentState, elementsToUpdate);

    }

}
