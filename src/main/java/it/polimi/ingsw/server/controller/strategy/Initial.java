package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Initial implements GameStrategy {

    List<Element> elementsToUpdate = new ArrayList<>();

    @Override
    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event) {

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
