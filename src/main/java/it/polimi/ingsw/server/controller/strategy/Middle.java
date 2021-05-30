package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.messages.clienttoserver.events.MiddlePhaseEvent;
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

    List<Element> elementsToUpdate = new ArrayList<>(0);

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event)
    {

        //MESSAGE IS MARKET, SHOP, PRODUCTION (0,1,2)
        MiddlePhaseEvent middlePhaseEvent = (MiddlePhaseEvent) event;
        int msg=middlePhaseEvent.getChoice();

        if(msg==0)
            return new Pair<>(State.CHOOSING_MARKET_LINE, elementsToUpdate);

        else if(msg==1)
            return new Pair<>(State.CHOOSING_DEVELOPMENT_CARD, elementsToUpdate);

        else
            return new Pair<>(State.CHOOSING_PRODUCTION, elementsToUpdate);
    }

}
