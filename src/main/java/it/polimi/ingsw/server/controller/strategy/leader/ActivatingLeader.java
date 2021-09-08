package it.polimi.ingsw.server.controller.strategy.leader;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.InitialOrFinalPhaseEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 *  If the chosen Leader is playable, its effect is activated. If not, nothing happens.
 */
public class ActivatingLeader implements GameStrategy {

    final List<Element> elementsToUpdate = new ArrayList<>();

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event)
    {


        State currentState = gamemodel.getGamePhase();
        State nextPossibleState = currentState.equals(State.INITIAL_PHASE) ? State.MIDDLE_PHASE : State.IDLE;

        if(gamemodel.getCurrentPlayer().getLeader(((InitialOrFinalPhaseEvent) event).getLeaderId()).isPresent())
            gamemodel.getCurrentPlayer().getLeader(((InitialOrFinalPhaseEvent) event).getLeaderId()).get().activate(gamemodel);

        elementsToUpdate.add(Element.SIMPLE_PLAYER_LEADERS);
        elementsToUpdate.add(Element.SIMPLE_PRODUCTIONS);
        elementsToUpdate.add(Element.SIMPLE_WARE_HOUSE_LEADERS_DEPOT);
        elementsToUpdate.add(Element.SIMPLE_CARD_CELLS);
        elementsToUpdate.add(Element.ACTIVE_LEADER_INFO);

        return gamemodel.getCurrentPlayer().anyLeaderPlayable()
                ? new Pair<>(currentState, elementsToUpdate)
                : new Pair<>(nextPossibleState, elementsToUpdate);

    }

}
