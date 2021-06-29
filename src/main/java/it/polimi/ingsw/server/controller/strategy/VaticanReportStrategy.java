package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.controller.EndGameReason;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.List;

public class VaticanReportStrategy {

    public static Pair<State, List<Element>> addFaithPointsStrategy(GameModel gameModel, List<Element> elementsToUpdate) {

        String endGameReason;

        PersonalBoard currentBoard = gameModel.getCurrentPlayer().getPersonalBoard();

        int positionsToAdd = currentBoard.getBadFaithToAdd();

        for (int i = 0; i < positionsToAdd; i++) {

            gameModel.addFaithPointToOtherPlayers();
            if (gameModel.handleVaticanReport())
                elementsToUpdate.add(Element.VaticanReportInfo);

            if (gameModel.checkTrackStatus() && !gameModel.getMacroGamePhase().equals(GameModel.MacroGamePhase.LastTurn)) { //lorenzo or a different player from current reached track end
                FinalStrategy.setLastTurnMacroGamePhase(gameModel, elementsToUpdate);

                if(gameModel.isSinglePlayer()) {
                    endGameReason = EndGameReason.LORENZO_REACHED_END.getEndGameReason();
                    gameModel.getThisMatch().setReasonOfGameEnd(endGameReason);
                    gameModel.getSinglePlayer().setMatchOutcome(false);
                    return FinalStrategy.handleSinglePlayerEndGameStrategy(elementsToUpdate, gameModel, endGameReason);
                }
                else {

                    if(gameModel.getPlayersAtTheEndOfTheFaithTrack().size()>1)
                        endGameReason = EndGameReason.MULTIPLE_TRACK_END.getEndGameReason();
                    else
                        endGameReason = EndGameReason.TRACK_END.getEndGameReason();

                    gameModel.getThisMatch().setReasonOfGameEnd(endGameReason);

                    FinalStrategy.setLastTurnMacroGamePhase(gameModel, elementsToUpdate);
                }


            }
        }

        currentBoard.setBadFaithToZero();

        positionsToAdd = currentBoard.getFaithToAdd();

        for (int i = 0; i < positionsToAdd; i++)
        {
            gameModel.getCurrentPlayer().moveOnePosition();
            if (gameModel.handleVaticanReport())
                elementsToUpdate.add(Element.VaticanReportInfo);

            if (gameModel.checkTrackStatus() && !gameModel.getMacroGamePhase().equals(GameModel.MacroGamePhase.LastTurn)) {
                FinalStrategy.setLastTurnMacroGamePhase(gameModel, elementsToUpdate);// player reached end

                if (gameModel.isSinglePlayer() && gameModel.getSinglePlayer().getPlayerPosition() == 24) {

                    endGameReason = EndGameReason.TRACK_END_SOLO.getEndGameReason();
                    gameModel.getThisMatch().setReasonOfGameEnd(endGameReason);
                    gameModel.getSinglePlayer().setMatchOutcome(true);

                    return FinalStrategy.handleSinglePlayerEndGameStrategy(elementsToUpdate, gameModel, endGameReason);
                }

                else {

                    if(gameModel.getPlayersAtTheEndOfTheFaithTrack().size()>1)
                        endGameReason = EndGameReason.MULTIPLE_TRACK_END.getEndGameReason();
                    else
                        endGameReason = EndGameReason.TRACK_END.getEndGameReason();

                    gameModel.getThisMatch().setReasonOfGameEnd(endGameReason);

                    FinalStrategy.setLastTurnMacroGamePhase(gameModel, elementsToUpdate);
                }

            }
        }

        currentBoard.setFaithToZero();

        return FinalStrategy.handleCommonEndGameStrategy(elementsToUpdate, gameModel);
    }

}
