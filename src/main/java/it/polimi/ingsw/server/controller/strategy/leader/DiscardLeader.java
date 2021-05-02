package it.polimi.ingsw.server.controller.strategy.leader;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.State;

/**
 *  If the chosen Leader is playable, it is discarded. If not, nothing happens.
 */
public class DiscardLeader implements GameStrategy {
    public State execute(GameModel gamemodel, Validable event)
    {
        //ON EVENT DISCARDLEADEREVENT
        //MESSAGE IS INT 2
       // if(gamemodel.getCurrentPlayer().getLeaders().get(2).getState()== LeaderState.INACTIVE)

        gamemodel.getCurrentPlayer().getLeaders().get(2).discard(gamemodel);

        return new EndLeaders().execute(gamemodel, );
    }
}
