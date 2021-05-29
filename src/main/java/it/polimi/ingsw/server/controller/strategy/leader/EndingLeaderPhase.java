package it.polimi.ingsw.server.controller.strategy.leader;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 *  This implementation uses a flag variable Past, to decide wether or not MIDDLE PHASE has already been played.
 */
public class EndingLeaderPhase implements GameStrategy {

    List<Element> elementsToUpdate = new ArrayList<>();

    public Pair<State, List<Element>> execute(GameModel gamemodel, Event event)
    {

        if (gamemodel.getCurrentPlayer().anyLeaderPlayable())
            return new Pair<>(gamemodel.getCurrentPlayer().getCurrentState() , elementsToUpdate);

        else if (gamemodel.getCurrentPlayer().getCurrentState()==State.INITIAL_PHASE)
            return new Pair<>(State.MIDDLE_PHASE, elementsToUpdate);

        else return new Pair<>(State.IDLE, elementsToUpdate);


    }}