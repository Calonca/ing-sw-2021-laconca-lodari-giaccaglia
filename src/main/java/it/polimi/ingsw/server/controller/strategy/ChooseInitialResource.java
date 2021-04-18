package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

/**
 * On game start, each player will choose a quantity of resources based on their turn order
 */
public class ChooseInitialResource extends GameStrategy {
    public State execute(GameModel gamemodel)
    {
        //MSG IS LIST OF INT? RESOURCES?
        int[] msg=new int[]  {1,3};
        int[] toadd=new int[]  {0,0,0,0};

        for (int j : msg) toadd[j]+=1;

        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(toadd);

        return State.IDLE;

    }
}
