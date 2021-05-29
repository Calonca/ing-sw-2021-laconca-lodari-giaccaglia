package it.polimi.ingsw.server.controller.strategy.leader;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent.DiscardLeaderEvent;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 *  If the chosen Leader is playable, it is discarded. If not, nothing happens.
 */
public class DiscardingLeader implements GameStrategy {

    List<Element> elementsToUpdate = new ArrayList<>();

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event)
    {
        //ON EVENT DISCARDLEADEREVENT
        //MESSAGE IS INT 2
       // if(gamemodel.getCurrentPlayer().getLeaders().get(2).getState()== NetworkLeaderState.INACTIVE)

        gamemodel.getCurrentPlayer().getLeader(((DiscardLeaderEvent) event).getLeaderId()).get().activate(gamemodel);

        elementsToUpdate.add(Element.SimpleFaithTrack);
        elementsToUpdate.add(Element.SimplePlayerLeaders);

        return new Pair<>(State.LEADER_END, elementsToUpdate);
    }
}
