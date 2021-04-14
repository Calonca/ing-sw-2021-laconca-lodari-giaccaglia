package it.polimi.ingsw.controller.strategy.resourcemarket;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

public class PutBall extends ResourceMarketStrategy
{
    /**
     *
     * @param gamemodel!=NULL
     * @return the correct state based on the available choiches
     */
    public State execute(GameModel gamemodel)
    {
        int i=0;
        //ON EVENT CHOOSEROWEVENT
        //RICEVO MESSAGGIO SELEZIONE

        return State.CHOOSING_POSITION_FOR_RESOURCES;
    }
}
