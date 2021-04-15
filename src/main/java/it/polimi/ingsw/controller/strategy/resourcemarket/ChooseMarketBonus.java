package it.polimi.ingsw.controller.strategy.resourcemarket;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.market.MarketLine;
import it.polimi.ingsw.server.model.player.State;
import javafx.util.Pair;

/**
 * This method allows the player's choice to be validated. They will pick a resource, and if available
 * in the Market Bonus effect, it will apply and convert one white marble. The return state will be
 * the one to choose the bonus, as long as there are white marbles remaining
 */
public class ChooseMarketBonus extends ResourceMarketStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT CHOOSEWHITEMARBLECONVERSIONEVENT
        //MESSAGE IS Resource.SHIELD
        Resource msg=Resource.SHIELD;
        //gamemodel.getBoxResourcesFromMarketBoard().selectN(1,Resource.TOCHOOSE);
        //gamemodel.getBoxResourcesFromMarketBoard().removeSelected();
        //gamemodel.getBoxResourcesFromMarketBoard().addResource(new Pair<>(1, Resource.SHIELD));

        if(gamemodel.getCurrentPlayer().getMarketBonus()[msg.getResourceNumber()])
            gamemodel.convertWhiteMarbleInPickedLine(msg);

        //if(gamemodel.getBoxResourcesFromMarketBoard().getNumberOf(Resource.TOCHOOSE)>0)
        if(gamemodel.areThereWhiteMarblesInPickedLine())
            return State.CHOOSING_WHITEMARBLE_CONVERSION;


        return State.CHOOSING_POSITION_FOR_RESOURCES;
    }
}
