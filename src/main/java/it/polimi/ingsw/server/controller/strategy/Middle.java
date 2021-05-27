package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.controller.strategy.cardmarket.ShowingDevelopmentCardsMarket;
import it.polimi.ingsw.server.controller.strategy.production.ShowingProductionCards;
import it.polimi.ingsw.server.controller.strategy.resourcemarket.ShowingResourceMarket;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Before the initial choice, Past is set to true, so the Leader logic will acquire that information.
 */

public class Middle implements GameStrategy {

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event) throws EventValidationFailedException
    {
        //MESSAGE IS MARKET, SHOP, PRODUCTION (0,1,2)
        int msg=0;
        if(msg==0)
            return new ShowingResourceMarket().execute(gamemodel, event);
        else if(msg==1)
            return new ShowingDevelopmentCardsMarket().execute(gamemodel, event);
        else
            return new ShowingProductionCards().execute(gamemodel, event);
    }


}
