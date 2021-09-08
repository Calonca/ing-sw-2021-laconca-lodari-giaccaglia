package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.List;

/**
 * This implementation manages the end of turn. It sets the correct next state, wether it is turn end, final phase or end game
 */
public class FinalStrategy {

    private FinalStrategy(){}

    public static Pair<State, List<Element>> handleCommonEndGameStrategy(List<Element> elementsToUpdate,GameModel gameModel){
            elementsToUpdate.add(Element.SIMPLE_CARD_SHOP);
            State nextState;
                if (gameModel.getCurrentPlayer().anyLeaderPlayable())
                    nextState = State.FINAL_PHASE;
                else
                    nextState = State.IDLE;

            return new Pair<>(nextState, elementsToUpdate);
    }



    public static Pair<State, List<Element>> handleSinglePlayerEndGameStrategy(List<Element> elementsToUpdate,GameModel gameModel, String endGameReason){
        gameModel.getThisMatch().setReasonOfGameEnd(endGameReason);
        elementsToUpdate.add(Element.END_GAME_INFO);
        return new Pair<>(State.END_PHASE, elementsToUpdate);
    }

    public static void setLastTurnMacroGamePhase(GameModel gameModel, List<Element> elementsToUpdate){

            if (gameModel.getMacroGamePhase().equals(GameModel.MacroGamePhase.ACTIVE_GAME)) {
                elementsToUpdate.add(Element.END_GAME_INFO);
                gameModel.setMacroGamePhase(GameModel.MacroGamePhase.LAST_TURN);
            }
        }
    }
