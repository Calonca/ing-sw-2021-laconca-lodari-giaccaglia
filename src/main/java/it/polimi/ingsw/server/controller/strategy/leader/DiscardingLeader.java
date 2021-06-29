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

    public Pair<State, List<Element>> execute(GameModel gameModel, Validable event) {

        State currentState = gameModel.getCurrentPlayer().getCurrentState();

        State nextPossibleState = currentState.equals(State.INITIAL_PHASE) ? State.MIDDLE_PHASE : State.IDLE;
        elementsToUpdate.add(Element.SimpleFaithTrack);
        elementsToUpdate.add(Element.SimplePlayerLeaders);

        gameModel.getCurrentPlayer().discardLeader(((InitialOrFinalPhaseEvent) event).getLeaderId());
        gameModel.getCurrentPlayer().moveOnePosition();

        if(gameModel.handleVaticanReport())
            elementsToUpdate.add(Element.VaticanReportInfo);

        if (gameModel.checkTrackStatus() && !gameModel.getMacroGamePhase().equals(GameModel.MacroGamePhase.LastTurn)) {

            String endGameReason;

            if (gameModel.isSinglePlayer()) {

                endGameReason = EndGameReason.TRACK_END_SOLO.getEndGameReason();
                gameModel.getThisMatch().setReasonOfGameEnd(endGameReason);
                gameModel.getSinglePlayer().setMatchOutcome(true);
                elementsToUpdate.add(Element.EndGameInfo);
                return FinalStrategy.handleSinglePlayerEndGameStrategy(elementsToUpdate, gameModel, endGameReason);

            }

            endGameReason = EndGameReason.TRACK_END.getEndGameReason();

            gameModel.getThisMatch().setReasonOfGameEnd(endGameReason);

            FinalStrategy.setLastTurnMacroGamePhase(gameModel, elementsToUpdate);

            return new Pair<>(State.IDLE, elementsToUpdate);

        }

        else return gameModel.getCurrentPlayer().anyLeaderPlayable()
                ? new Pair<>(currentState, elementsToUpdate)
                : new Pair<>(nextPossibleState, elementsToUpdate);

    }

}
