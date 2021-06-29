package it.polimi.ingsw.server.controller.strategy.production;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.controller.strategy.VaticanReportStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.FinalProductionPhaseEvent;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class FinalProductionStrategy implements GameStrategy {

    @Override
    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event) {

        List<Element> elementsToUpdate = new ArrayList<>();

        elementsToUpdate.add(Element.SimpleProductions);
        elementsToUpdate.add(Element.SimpleCardCells);
        elementsToUpdate.add(Element.SimpleWareHouseLeadersDepot);
        elementsToUpdate.add(Element.SimpleStrongBox);
        elementsToUpdate.add(Element.SimpleFaithTrack);


        PersonalBoard personalBoard = gamemodel.getCurrentPlayer().getPersonalBoard();

        FinalProductionPhaseEvent finalProductionPhaseEvent = (FinalProductionPhaseEvent) event;
        int actionToPerform = finalProductionPhaseEvent.getActionToPerform();

        if(actionToPerform==0){    // 0 to produce
            personalBoard.produce();
            return VaticanReportStrategy.addFaithPointsStrategy(gamemodel, elementsToUpdate);
        }

        else // 1 revert all and go back to middle phase

                personalBoard.resetAllSelectedProductions();
                return new Pair<>(State.MIDDLE_PHASE, elementsToUpdate);

        }

}
