package it.polimi.ingsw.server.controller.strategy.resourcemarket;

import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.market.MarketLine;
import it.polimi.ingsw.server.model.State;

/**
 * This implementation allows the user to choose a line, sliding in the remaining marble. If the player has
 * an active discount, the state progresses accordingly. At first, FAITH gets filtered out and added to
 * the user's resources. If there are any white marbles and active Market Leaders, the user will choose
 * which resources to convert, if not they will choose the warehouse position.
 */
public class PuttingBallOnLine implements GameStrategy {

    public State execute(GameModel gamemodel, Validable event) throws EventValidationFailedException
    {
        //ON EVENT CHOOSEROWEVENT
        //MESSAGE IS MarketLine.FIRST_COLUMN

        gamemodel.chooseLineFromMarketBoard(MarketLine.FIRST_COLUMN);
        gamemodel.updateMatrixAfterTakingResources();

        gamemodel.getCurrentPlayer().getPersonalBoard().setMarketBox(gamemodel.getBoxResourcesFromMarketBoard());


        // gamemodel.getBoxResourcesFromMarketBoard().removeResources(new int[]{0,0,0,0,faithnum,0,0});
        //gamemodel.getBoxResourcesFromMarketBoard().selectN(faithnum,Resource.FAITH);
        //gamemodel.getBoxResourcesFromMarketBoard().removeSelected();


        //if (gamemodel.getBoxResourcesFromMarketBoard().getNumberOf(Resource.TOCHOOSE)>0)
        if (gamemodel.areThereWhiteMarblesInPickedLine())
           if(!gamemodel.getCurrentPlayer().moreThanOneMarketBonus())
               for(int i=0;i<gamemodel.getNumberOfWhiteMarblesInPickedLine();i++)
               {
                   gamemodel.convertWhiteMarbleInPickedLine(Resource.fromInt(gamemodel.getCurrentPlayer().getSingleMarketBonus()));
               }
           else return State.CHOOSING_WHITEMARBLE_CONVERSION;
        return State.CHOOSING_POSITION_FOR_RESOURCES;
    }
}