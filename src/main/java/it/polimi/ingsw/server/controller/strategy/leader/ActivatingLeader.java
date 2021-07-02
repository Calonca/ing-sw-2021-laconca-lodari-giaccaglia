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

    List<Element> elementsToUpdate = new ArrayList<>();

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event)
    {


        State currentState = gamemodel.getGamePhase();
        State nextPossibleState = currentState.equals(State.INITIAL_PHASE) ? State.MIDDLE_PHASE : State.IDLE;

        gamemodel.getCurrentPlayer().getLeader(((InitialOrFinalPhaseEvent) event).getLeaderId()).get().activate(gamemodel);

        elementsToUpdate.add(Element.SimplePlayerLeaders);
        elementsToUpdate.add(Element.SimpleProductions);
        elementsToUpdate.add(Element.SimpleWareHouseLeadersDepot);
        elementsToUpdate.add(Element.SimpleCardCells);
        elementsToUpdate.add(Element.ActiveLeaderInfo);

        return gamemodel.getCurrentPlayer().anyLeaderPlayable()
                ? new Pair<>(currentState, elementsToUpdate)
                : new Pair<>(nextPossibleState, elementsToUpdate);

    }

}
