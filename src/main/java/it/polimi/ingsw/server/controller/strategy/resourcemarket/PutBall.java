package it.polimi.ingsw.server.controller.strategy.resourcemarket;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.market.MarketLine;
import it.polimi.ingsw.server.model.player.State;

/**
 * This implementation allows the user to choose a line, sliding in the remaining marble. If the player has
 * an active discount, the state progresses accordingly. At first, FAITH gets filtered out and added to
 * the user's resources. If there are any white marbles and active Market Leaders, the user will choose
 * which resources to convert, if not they will choose the warehouse position.
 */
public class PutBall extends ResourceMarketStrategy
{

    public State execute(GameModel gamemodel)
    {
        int i;
        //ON EVENT CHOOSEROWEVENT
        //MESSAGE IS MarketLine.FIRST_COLUMN

        gamemodel.chooseLineFromMarketBoard(MarketLine.FIRST_COLUMN);
        gamemodel.updateMatrixAfterTakingResources();

        int faithnum=gamemodel.getBoxResourcesFromMarketBoard().getNumberOf(Resource.FAITH);


        for(i=0; i<faithnum; i++)
            gamemodel.getCurrentPlayer().moveOnePosition();


        // gamemodel.getBoxResourcesFromMarketBoard().removeResources(new int[]{0,0,0,0,faithnum,0,0});
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
