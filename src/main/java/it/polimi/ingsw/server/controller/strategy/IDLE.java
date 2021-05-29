package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * On turn start, if there is the possibility to play a Leader, the user will be able to decide wether to play
 * or a normal action
 */
public class Initial implements GameStrategy {

    List<Element> elementsToUpdate = new ArrayList<>();

    public Pair<State, List<Element>> execute(GameModel gamemodel, Event event) throws EventValidationFailedException
    {
        if(!gamemodel.getCurrentPlayer().anyLeaderPlayable()) {
            gamemodel.getCurrentPlayer().setCurrentState(State.MIDDLE_PHASE);
            return new Pair<>(State.MIDDLE_PHASE, elementsToUpdate);
        }
        else {
            gamemodel.getCurrentPlayer().setCurrentState(State.INITIAL_PHASE);
            return new Pair<>(State.INITIAL_PHASE, elementsToUpdate);
        }
    }

}
