package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.controller.states.State;

/**
 * On game start, each player will choose a quantity of resources based on their turn order
 */
public class ChooseInitialResource implements GameStrategy {
    public State execute(GameModel gamemodel, Validable event) throws EventValidationFailedException
    {
        //MSG IS LIST OF INT? RESOURCES?
        int[] msg=new int[]  {1,3};
        int[] toadd=new int[]  {0,0,0,0};

        for (int j : msg) toadd[j]+=1;

        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(toadd);

        return State.IDLE;

    }
}
