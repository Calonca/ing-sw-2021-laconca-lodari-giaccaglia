package it.polimi.ingsw.server.controller.strategy.resourcemarket;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.controller.strategy.VaticanReportStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * This implementation calculates the movements on the Faith Track based on the resources left in the
 * discardbox.
 */
public class DiscardingResources implements GameStrategy {

    public Pair<State, List<Element>> execute(GameModel gameModel, Validable event) {

        List<Element> elementsToUpdate = new ArrayList<>();

        elementsToUpdate.add(Element.SIMPLE_FAITH_TRACK);
        elementsToUpdate.add(Element.SIMPLE_DISCARD_BOX);
        elementsToUpdate.add(Element.SIMPLE_PLAYER_LEADERS);

        PersonalBoard currentBoard = gameModel.getCurrentPlayer().getPersonalBoard();

        currentBoard.discardResources();

        return VaticanReportStrategy.addFaithPointsStrategy(gameModel, elementsToUpdate);
    }


}
