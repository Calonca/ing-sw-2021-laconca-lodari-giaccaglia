package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * On game start, each player will choose a quantity of resources based on their turn order
 */
public class ChooseInitialResource implements GameStrategy {
    public State execute(GameModel gamemodel, Validable event) throws EventValidationFailedException
    {
        //ON SETUPPHASEEVENT
        //MSG IS LIST OF INT? RESOURCES?

        event.validate(gamemodel);

        int[] toadd = new int[4];
        Arrays.fill((toadd),0);
        for (Pair<Integer, Integer> resourceIntegerPair : ((SetupPhaseEvent) event).getChosenResources())
            toadd[resourceIntegerPair.getKey()] += resourceIntegerPair.getValue();
        List<UUID> toDiscard= ((SetupPhaseEvent) event).getDiscardedLeaders();

        for(UUID toD : toDiscard)
            if(gamemodel.getCurrentPlayer().getLeader(toD).isPresent())
                gamemodel.getCurrentPlayer().getLeader(toD).get().discard(gamemodel);

        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(toadd);

        return State.IDLE;

    }
}
