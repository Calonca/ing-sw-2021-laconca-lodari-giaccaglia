package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * On turn end, if there is the possibility to play a Leader, the user will be able to decide wether to play
 * or end the turn
 */
public class Final implements GameStrategy {

    List<Element> elementsToUpdate = new ArrayList<>();

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event)
    {
        //MESSAGE IS SKIPLEADER OR CHOOSELEADER (0 or 1)
        //gamemodel.getCurrentPlayer().setMacroState(State.INITIAL_PHASE);
        if(!gamemodel.getCurrentPlayer().anyLeaderPlayable())

            return new Pair<>(State.IDLE, elementsToUpdate);

        else

            return new Pair<>(State.FINAL_PHASE, elementsToUpdate);
    }
}
