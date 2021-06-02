package it.polimi.ingsw.server.controller.strategy.resourcemarket;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.ChooseLineEvent;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.market.MarketLine;
import it.polimi.ingsw.server.model.player.board.Box;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * This implementation allows the user to choose a line, sliding in the remaining marble. If the player has
 * an active discount, the state progresses accordingly. At first, FAITH gets filtered out and added to
 * the user's resources. If there are any white marbles and active Market Leaders, the user will choose
 * which resources to convert, if not they will choose the warehouse position.
 */
public class PuttingBallOnLine implements GameStrategy {

    List<Element> elementsToUpdate = new ArrayList<>();

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event) {

        gamemodel.chooseLineFromMarketBoard(MarketLine.fromInt(((ChooseLineEvent) event).getChosenRow()));
        gamemodel.getCurrentPlayer().getPersonalBoard().setMarketBox(gamemodel.getBoxOfResourcesFromMarketBoard());
        gamemodel.updateMatrixAfterTakingResources();
        gamemodel.getCurrentPlayer().getPersonalBoard().getFaithToAdd();

        elementsToUpdate.add(Element.SimpleMarketBoard);

        if (gamemodel.areThereWhiteMarblesInPickedLine()){
            if (!gamemodel.getCurrentPlayer().moreThanOneMarketBonus() && (gamemodel.getCurrentPlayer().getSingleMarketBonus()>-1)) {
                for (int i = 0; i < gamemodel.getNumberOfWhiteMarblesInPickedLine(); i++) {
                    gamemodel.convertWhiteMarbleInPickedLine(Resource.fromInt(gamemodel.getCurrentPlayer().getSingleMarketBonus()));
                }
            }
            else if (gamemodel.getCurrentPlayer().moreThanOneMarketBonus())
                return new Pair<>(State.CHOOSING_WHITEMARBLE_CONVERSION, elementsToUpdate);
        }

        Box marketBox = gamemodel.getBoxOfResourcesFromMarketBoard();
        gamemodel.getCurrentPlayer().getPersonalBoard().setMarketBox(marketBox);

        elementsToUpdate.add(Element.SimpleDiscardBox);

        return new Pair<>(State.CHOOSING_POSITION_FOR_RESOURCES, elementsToUpdate);

    }
}
