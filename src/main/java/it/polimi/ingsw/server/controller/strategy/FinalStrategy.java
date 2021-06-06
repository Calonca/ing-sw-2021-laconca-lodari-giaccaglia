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
            return new Pair<>(State.FINAL_PHASE, elementsToUpdate);
        }
        else{
            elementsToUpdate.add(Element.SimpleCardShop);
            return new Pair<>(State.IDLE, elementsToUpdate);
        }
    }

    private static boolean playerTriggeredEnd(GameModel gameModel) {
        return gameModel.getMacroGamePhase().equals(GameModel.MacroGamePhase.LastTurn) &&
                gameModel.getPlayersEndingTheGame().containsKey(gameModel.getPlayerIndex(gameModel.getCurrentPlayer()));
    }

}
