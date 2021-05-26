package it.polimi.ingsw.server.controller.strategy.leader;

import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent.DiscardLeaderEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent.PlayLeaderEvent;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;

/**
 *  If the chosen Leader is playable, it is discarded. If not, nothing happens.
 */
public class DiscardingLeader implements GameStrategy {
    public State execute(GameModel gamemodel, Validable event) throws EventValidationFailedException
    {
        //ON EVENT DISCARDLEADEREVENT
        //MESSAGE IS INT 2
       // if(gamemodel.getCurrentPlayer().getLeaders().get(2).getState()== NetworkLeaderState.INACTIVE)

        gamemodel.getCurrentPlayer().getLeader(((DiscardLeaderEvent) event).getLeaderId()).get().activate(gamemodel);

        return new EndingLeaderPhase().execute(gamemodel, event);
    }
}
