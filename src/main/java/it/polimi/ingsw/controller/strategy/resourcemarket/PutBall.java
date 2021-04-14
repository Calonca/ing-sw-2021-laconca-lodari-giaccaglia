package it.polimi.ingsw.controller.strategy.resourcemarket;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.market.MarketLine;
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
        int i;
        //ON EVENT CHOOSEROWEVENT
        //RICEVO MESSAGGIO SELEZIONE MarketLine.FIRST_COLUMN

        gamemodel.chooseLineFromMarketBoard(MarketLine.FIRST_COLUMN);
        gamemodel.updateMatrixAfterTakingResources();

        int faithnum=gamemodel.getBoxResourcesFromMarketBoard().getNumberOf(Resource.FAITH);


        for(i=0; i<faithnum; i++)
            gamemodel.getCurrentPlayer().moveOnePosition();


        // che è meglio? gamemodel.getBoxResourcesFromMarketBoard().removeResources(new int[]{0,0,0,0,faithnum,0,0});
        gamemodel.getBoxResourcesFromMarketBoard().selectN(faithnum,Resource.FAITH);
        gamemodel.getBoxResourcesFromMarketBoard().removeSelected();


        //if (gamemodel.getBoxResourcesFromMarketBoard().getNumberOf(Resource.TOCHOOSE)>0)
        if (gamemodel.areThereWhiteMarblesInPickedLine())
            for(i=0; i<gamemodel.getCurrentPlayer().getMarketBonus().length; i++)
                if (gamemodel.getCurrentPlayer().getMarketBonus()[i])
                    return State.CHOOSING_WHITEMARBLE_CONVERSION;

        return State.CHOOSING_POSITION_FOR_RESOURCES;
    }
}
