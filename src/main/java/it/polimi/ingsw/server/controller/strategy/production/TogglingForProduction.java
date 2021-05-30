package it.polimi.ingsw.server.controller.strategy.production;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.ChooseProductionAtPositionEvent;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * This implementation allows the user to toggle selections on the available productions. When a choice is needed, the
 * used needs to perform it to completion before being able to toggle another production.
 */
public class TogglingForProduction implements GameStrategy {

    List<Element> elementsToUpdate = new ArrayList<>();

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event)
    {

        int msg=3;

        ChooseProductionAtPositionEvent eventCasted = (ChooseProductionAtPositionEvent) event;

        if (eventCasted.getProductionPosition()==-1)
        {
            gamemodel.getCurrentPlayer().getPersonalBoard().produce();
            return new Pair<>(State.FINAL_PHASE, elementsToUpdate);

        }
        gamemodel.getCurrentPlayer().getPersonalBoard().toggleSelectProductionAt(((ChooseProductionAtPositionEvent) event).getProductionPosition());

        if(gamemodel.getCurrentPlayer().getPersonalBoard().firstProductionSelectedWithChoice().isPresent())
                    return new Pair<>(State.CHOOSING_RESOURCE_FOR_PRODUCTION, elementsToUpdate);

        return new Pair<>(State.CHOOSING_PRODUCTION, elementsToUpdate);
    }

}
