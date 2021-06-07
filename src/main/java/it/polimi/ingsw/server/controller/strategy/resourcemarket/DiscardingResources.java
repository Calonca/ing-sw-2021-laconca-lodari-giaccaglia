package it.polimi.ingsw.server.controller.strategy.resourcemarket;

import it.polimi.ingsw.server.controller.strategy.FinalStrategy;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
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

    private final List<Element> elementsToUpdate = new ArrayList<>();
    private transient GameModel gameModel;

    public Pair<State, List<Element>> execute(GameModel gameModel, Validable event)
    {

        int positionsToAdd;
        elementsToUpdate.add(Element.SimpleDiscardBox);
        elementsToUpdate.add(Element.SimpleFaithTrack);
        elementsToUpdate.add(Element.SimplePlayerLeaders);

        this.gameModel = gameModel;
        PersonalBoard currentBoard = gameModel.getCurrentPlayer().getPersonalBoard();

        currentBoard.discardResources();

        positionsToAdd = currentBoard.getBadFaithToAdd();

        for(int i=0; i<positionsToAdd; i++)
        {

            gameModel.addFaithPointToOtherPlayers();
            gameModel.handleVaticanReport();

            if(gameModel.checkTrackStatus()) {

                handleCommonEndGameStrategy();

                return new Pair<>(State.IDLE, elementsToUpdate);

            }
        }

        currentBoard.setBadFaithToZero();


        positionsToAdd = currentBoard.getFaithToAdd();
        for(int i=0; i<positionsToAdd; i++)
        {
            gameModel.getCurrentPlayer().moveOnePosition();
            gameModel.handleVaticanReport();

            if(gameModel.checkTrackStatus()) {

                handleCommonEndGameStrategy();

                return new Pair<>(State.IDLE, elementsToUpdate);
            }

        }

        currentBoard.setFaithToZero();


        return FinalStrategy.handleCommonEndGameStrategy(elementsToUpdate,gameModel);

    }

    private void handleCommonEndGameStrategy(){

        if (gameModel.getMacroGamePhase().equals(GameModel.MacroGamePhase.ActiveGame))
            gameModel.setMacroGamePhase(GameModel.MacroGamePhase.LastTurn);

        else if (gameModel.getPlayerIndex(gameModel.getCurrentPlayer()) == (gameModel.getOnlinePlayers().size() - 1))
            gameModel.setMacroGamePhase(GameModel.MacroGamePhase.GameEnded);

    }
}
