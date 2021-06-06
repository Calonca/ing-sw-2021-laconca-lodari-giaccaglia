package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.controller.strategy.leader.ActivatingLeader;
import it.polimi.ingsw.server.controller.strategy.leader.DiscardingLeader;
import it.polimi.ingsw.server.messages.clienttoserver.events.InitialOrFinalPhaseEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ExecuteLeaderAction implements GameStrategy {

    List<Element> elementsToUpdate = new ArrayList<>();

    @Override
    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event) {

        int choice = ( (InitialOrFinalPhaseEvent) event ).getChoice();

        if(choice == 0){  //discard

            return new DiscardingLeader().execute(gamemodel, event);

        }

        else if(choice == 1){ //activate

            return new ActivatingLeader().execute(gamemodel, event);

        }

        else { //choice == 3 -> skip
            State nextState = gamemodel.getCurrentPlayer().getCurrentState().equals(State.INITIAL_PHASE) ? State.MIDDLE_PHASE : State.IDLE;

            elementsToUpdate.add(Element.SimpleAvailableMovingPositions); //needed for idle phase
            elementsToUpdate.add(Element.SimpleCardShop);
            return new Pair<>(nextState, elementsToUpdate);

        }

    }

}

