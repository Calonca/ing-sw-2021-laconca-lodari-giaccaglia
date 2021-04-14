package it.polimi.ingsw.controller.strategy.resourcemarket;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.market.MarketLine;
import it.polimi.ingsw.server.model.player.State;

public abstract class AddResources extends ResourceMarketStrategy
{
    /**
     *
     * @param gamemodel!=NULL
     * @return the correct state based on the available choiches
     */
    public abstract State execute(GameModel gamemodel);
}
