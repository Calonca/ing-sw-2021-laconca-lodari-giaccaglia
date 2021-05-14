package it.polimi.ingsw.server.controller.strategy.leader;

import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;

/**
 *  If the chosen Leader is playable, its effect is activated. If not, nothing happens.
 */
public class ActivatingLeader implements GameStrategy {
    public State execute(GameModel gamemodel, Validable event) throws EventValidationFailedException
    {
        //ON EVENT PLAYLEADEREVENT
        //MESSAGE IS INT 2
    //    if(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(gamemodel.getCurrentPlayer().getLeaders().get(2))&&
    //        gamemodel.getCurrentPlayer().getLeaders().get(2).getState()== NetworkLeaderState.INACTIVE)
      //  gamemodel.getCurrentPlayer().getLeader().get(2).activate(gamemodel);

        return new EndingLeaderPhase().execute(gamemodel, event);


    }
}
