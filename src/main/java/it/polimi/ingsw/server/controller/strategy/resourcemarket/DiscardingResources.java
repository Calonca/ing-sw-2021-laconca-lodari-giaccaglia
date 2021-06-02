package it.polimi.ingsw.server.controller.strategy.resourcemarket;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.controller.strategy.InitialOrFinalStrategy;
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

    List<Element> elementsToUpdate = new ArrayList<>();

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event)
    {
        int positionsToAdd;
        PersonalBoard currentBoard = gamemodel.getCurrentPlayer().getPersonalBoard();

        positionsToAdd = currentBoard.getFaithToAdd();

        for(int i=0;i<positionsToAdd;i++)
        {
            gamemodel.getCurrentPlayer().moveOnePosition();  //TODO HANDLE VATICAN REPORT
        }

        currentBoard.discardResources();
        positionsToAdd = currentBoard.getBadFaithToAdd();


        for(int i=0;i<positionsToAdd;i++)
        {
            gamemodel.addFaithPointToOtherPlayers();  //TODO HANDLE VATICAN REPORT
        }



        elementsToUpdate.add(Element.SimpleDiscardBox);

        return new Pair<>(State.FINAL_PHASE, elementsToUpdate);

    }


}
