package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.controller.strategy.cardmarket.ShowCards;
import it.polimi.ingsw.server.controller.strategy.production.ShowProductionCards;
import it.polimi.ingsw.server.controller.strategy.resourcemarket.ShowResourceMarket;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.State;
/**
 * Before the initial choice, Past is set to true, so the Leader logic will acquire that information.
 */

public class Middle implements GameStrategy {
    public State execute(GameModel gamemodel)
    {
        //MESSAGE IS MARKET, SHOP, PRODUCTION (0,1,2)
        int msg=0;
        if(msg==0)
            return new ShowResourceMarket().execute(gamemodel);
        else if(msg==1)
            return new ShowCards().execute(gamemodel);
        else
            return new ShowProductionCards().execute(gamemodel);
    }
}
