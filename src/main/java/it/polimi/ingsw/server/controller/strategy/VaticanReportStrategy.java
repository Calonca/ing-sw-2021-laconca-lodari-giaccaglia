package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.controller.EndGameReason;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.List;

public class VaticanReportStrategy {

    public static Pair<State, List<Element>>  addFaithPointsToPlayer(GameModel gameModel, List<Element> elementsToUpdate){

        boolean notCurrentPlayerReachedEnd = false;
        boolean currentPlayerReachedEnd = false;
        String endGameReason = null;

        PersonalBoard currentBoard = gameModel.getCurrentPlayer().getPersonalBoard();

        int positionsToAdd = currentBoard.getBadFaithToAdd();

        for (int i = 0; i < positionsToAdd; i++) {

            gameModel.addFaithPointToOtherPlayers();
            if(gameModel.handleVaticanReport())
                elementsToUpdate.add(Element.VaticanReportInfo);

            if (gameModel.checkTrackStatus()) {
                handleCommonEndGameStrategy(gameModel);
                notCurrentPlayerReachedEnd = true;

            }
        }

        currentBoard.setBadFaithToZero();

        positionsToAdd = currentBoard.getFaithToAdd();

        for(int i = 0; i < positionsToAdd; i++) {
            gameModel.getCurrentPlayer().moveOnePosition();
            if(gameModel.handleVaticanReport())
                elementsToUpdate.add(Element.VaticanReportInfo);

            if (gameModel.checkTrackStatus()) { // player reached end

                if (gameModel.isSinglePlayer()) {

                    endGameReason = EndGameReason.TRACK_END_SOLO.getEndGameReason();
                    currentPlayerReachedEnd = true;
                }

                FinalStrategy.setMacroGamePhase(gameModel, elementsToUpdate);

            }
        }

        currentBoard.setFaithToZero();

        if(notCurrentPlayerReachedEnd)
            return new Pair<>(State.IDLE, elementsToUpdate);
        else if (currentPlayerReachedEnd)
            return FinalStrategy.handleSinglePlayerEndGameStrategy(elementsToUpdate, gameModel, endGameReason);
        else
            return FinalStrategy.handleCommonEndGameStrategy(elementsToUpdate, gameModel);

    }

    private static void handleCommonEndGameStrategy(GameModel gameModel){

        if (gameModel.getMacroGamePhase().equals(GameModel.MacroGamePhase.ActiveGame))
            gameModel.setMacroGamePhase(GameModel.MacroGamePhase.LastTurn);

        else if (gameModel.getPlayerIndex(gameModel.getCurrentPlayer()) == (gameModel.getOnlinePlayers().size() - 1))
            gameModel.setMacroGamePhase(GameModel.MacroGamePhase.GameEnded);

    }
}
