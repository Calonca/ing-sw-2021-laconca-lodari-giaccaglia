package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.List;

public class FinalStrategy {

    public static Pair<State, List<Element>> handleCommonEndGameStrategy(List<Element> elementsToUpdate,GameModel gameModel){
            elementsToUpdate.add(Element.SimpleCardShop);
            State nextState;
                if (gameModel.getCurrentPlayer().anyLeaderPlayable())
                    nextState = State.FINAL_PHASE;
                else
                    nextState = State.IDLE;

            return new Pair<>(nextState, elementsToUpdate);
    }



    public static Pair<State, List<Element>> handleSinglePlayerEndGameStrategy(List<Element> elementsToUpdate,GameModel gameModel, String endGameReason){
        gameModel.getThisMatch().setReasonOfGameEnd(endGameReason);
        elementsToUpdate.add(Element.EndGameInfo);
        return new Pair<>(State.END_PHASE, elementsToUpdate);
    }

    public static void setLastTurnMacroGamePhase(GameModel gameModel, List<Element> elementsToUpdate){

            if (gameModel.getMacroGamePhase().equals(GameModel.MacroGamePhase.ActiveGame)) {
                elementsToUpdate.add(Element.EndGameInfo);
                gameModel.setMacroGamePhase(GameModel.MacroGamePhase.LastTurn);
            }
        }
    }
