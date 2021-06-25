package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.List;

public class FinalStrategy {

    public static Pair<State, List<Element>> handleCommonEndGameStrategy(List<Element> elementsToUpdate,GameModel gameModel){

        if (gameModel.getCurrentPlayer().anyLeaderPlayable() && !playerTriggeredEnd(gameModel)) {
            elementsToUpdate.add(Element.SimpleCardShop);

            State nextState = State.FINAL_PHASE;
            if(gameModel.isSinglePlayer() && gameModel.getSinglePlayer().getCurrentState().equals(State.IDLE))
                nextState = gameModel.getCurrentPlayer().anyLeaderPlayable() ? State.INITIAL_PHASE : State.MIDDLE_PHASE;

            return new Pair<>(nextState, elementsToUpdate);
        }
        else{

            elementsToUpdate.add(Element.SimpleCardShop);

            State nextState = State.IDLE;
            if(gameModel.isSinglePlayer() && gameModel.getSinglePlayer().getCurrentState().equals(State.IDLE))
                nextState = gameModel.getCurrentPlayer().anyLeaderPlayable() ? State.INITIAL_PHASE : State.MIDDLE_PHASE;

            return new Pair<>(nextState, elementsToUpdate);
        }
    }

    private static boolean playerTriggeredEnd(GameModel gameModel) {
        return gameModel.getMacroGamePhase().equals(GameModel.MacroGamePhase.LastTurn) &&
                gameModel.getPlayersEndingTheGame().containsKey(gameModel.getPlayerIndex(gameModel.getCurrentPlayer()));
    }

    public static Pair<State, List<Element>> handleSinglePlayerEndGameStrategy(List<Element> elementsToUpdate,GameModel gameModel, String endGameReason){
        gameModel.getThisMatch().setReasonOfGameEnd(endGameReason);
        return new Pair<>(State.END_PHASE, elementsToUpdate);
    }

    public static void setMacroGamePhase(GameModel gameModel, List<Element> elementsToUpdate){

            if (gameModel.getMacroGamePhase().equals(GameModel.MacroGamePhase.ActiveGame)) {
                elementsToUpdate.add(Element.EndGameInfo);
                gameModel.setMacroGamePhase(GameModel.MacroGamePhase.LastTurn);
            }

            else if (gameModel.getPlayerIndex(gameModel.getCurrentPlayer()) == (gameModel.getOnlinePlayers().size() - 1))
                gameModel.setMacroGamePhase(GameModel.MacroGamePhase.GameEnded);

        }
    }
