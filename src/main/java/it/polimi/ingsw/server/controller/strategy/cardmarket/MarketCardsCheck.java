package it.polimi.ingsw.server.controller.strategy.cardmarket;

import it.polimi.ingsw.server.controller.EndGameReason;
import it.polimi.ingsw.server.controller.strategy.FinalStrategy;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.List;

public class MarketCardsCheck {

    private MarketCardsCheck(){}

    public static Pair<State, List<Element>> checkMarketCards(GameModel gamemodel, List<Element> elementsToUpdate){

        String endGameReason = null;
        elementsToUpdate.add(Element.END_GAME_INFO);
        PersonalBoard currentBoard = gamemodel.getCurrentPlayer().getPersonalBoard();


        if (currentBoard.playerHasSevenCards()) {
            endGameReason = EndGameReason.SEVENTH_CARD_PURCHASED_SOLO.getEndGameReason();
            gamemodel.getSinglePlayer().setMatchOutcome(true);
        }

        else if (gamemodel.isSomeDevCardColourOutOfStock()) {
            String colorUpperCaseString = gamemodel.getCardShop().getColorOutOfStock().toString();
            String color = colorUpperCaseString.charAt(0) + colorUpperCaseString.substring(1);
            endGameReason = color + "Development card  " + EndGameReason.NO_MORE_DEVCARD_TYPE.getEndGameReason();
            gamemodel.getSinglePlayer().setMatchOutcome(false);
        }
        gamemodel.getThisMatch().setReasonOfGameEnd(endGameReason);

        return FinalStrategy.handleSinglePlayerEndGameStrategy(elementsToUpdate, gamemodel, endGameReason);
    }

}
