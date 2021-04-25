package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;
/**
 * Before the initial choice, Past is set to true, so the Leader logic will acquire that information.
 */

public class Middle implements GameStrategy {
    public State execute(GameModel gamemodel)
    {
        //MESSAGE IS MARKET, SHOP, PRODUCTION (0,1,2)
        int msg=0;
        gamemodel.getCurrentPlayer().setMacroState(State.MIDDLE_PHASE);
        if(msg==0)
            return State.SHOWING_MARKET_RESOURCES;
        else if(msg==1)
            return State.SHOWING_CARD_SHOP;
        else
            return State.CHOOSING_CARD_FOR_PRODUCTION;
    }
}
