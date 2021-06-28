package it.polimi.ingsw.server.controller.strategy.leader;

import it.polimi.ingsw.server.controller.EndGameReason;
import it.polimi.ingsw.server.controller.strategy.FinalStrategy;
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
 *  If the chosen Leader is playable, it is discarded. If not, nothing happens.
 */
public class DiscardingLeader implements GameStrategy {

    List<Element> elementsToUpdate = new ArrayList<>();

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event) {

        State currentState = gamemodel.getCurrentPlayer().getCurrentState();

        State nextPossibleState = currentState.equals(State.INITIAL_PHASE) ? State.MIDDLE_PHASE : State.IDLE;
        elementsToUpdate.add(Element.SimpleFaithTrack);
        elementsToUpdate.add(Element.SimplePlayerLeaders);

        gamemodel.getCurrentPlayer().discardLeader(((InitialOrFinalPhaseEvent) event).getLeaderId());
        gamemodel.getCurrentPlayer().moveOnePosition();

        if(gamemodel.handleVaticanReport())
            elementsToUpdate.add(Element.VaticanReportInfo);

        if (gamemodel.checkTrackStatus()) {

            String endGameReason;

            if (gamemodel.isSinglePlayer()) {

                endGameReason = EndGameReason.TRACK_END_SOLO.getEndGameReason();
                gamemodel.getThisMatch().setReasonOfGameEnd(endGameReason);
                gamemodel.getSinglePlayer().setMatchOutcome(true);
                elementsToUpdate.add(Element.EndGameInfo);
                return FinalStrategy.handleSinglePlayerEndGameStrategy(elementsToUpdate, gamemodel, endGameReason);

            }



            endGameReason = EndGameReason.TRACK_END.getEndGameReason();

            gamemodel.getThisMatch().setReasonOfGameEnd(endGameReason);

            FinalStrategy.setLastTurnMacroGamePhase(gamemodel, elementsToUpdate);

            return new Pair<>(State.IDLE, elementsToUpdate);

        }

        else return gamemodel.getCurrentPlayer().anyLeaderPlayable()
                ? new Pair<>(currentState, elementsToUpdate)
                : new Pair<>(nextPossibleState, elementsToUpdate);

    }

}
