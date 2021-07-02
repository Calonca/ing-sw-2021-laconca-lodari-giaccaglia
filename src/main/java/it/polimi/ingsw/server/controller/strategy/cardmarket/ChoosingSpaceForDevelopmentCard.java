package it.polimi.ingsw.server.controller.strategy.cardmarket;

import it.polimi.ingsw.server.controller.EndGameReason;
import it.polimi.ingsw.server.controller.strategy.FinalStrategy;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardPositionEvent;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This implementation allows the user to place the selected card in an available space. Upon calling this is
 * precalculated that at least one correct option exists
 */
public class ChoosingSpaceForDevelopmentCard implements GameStrategy {

    List<Element> elementsToUpdate = new ArrayList<>();

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event)
    {

        int chosenPosition = ((ChooseCardPositionEvent) event).getCardPositionInPersonalBoard();

        PersonalBoard currentBoard = gamemodel.getCurrentPlayer().getPersonalBoard();
        DevelopmentCard purchaseCard = gamemodel.takePurchasedCardFromShop();

        currentBoard.addDevelopmentCardToCell(purchaseCard, chosenPosition);

        elementsToUpdate.add(Element.SimpleCardCells);
        elementsToUpdate.add(Element.SimpleCardShop);
        elementsToUpdate.add(Element.SimplePlayerLeaders);
        elementsToUpdate.add(Element.SimpleProductions);

        if(gamemodel.isSinglePlayer() && (gamemodel.isSomeDevCardColourOutOfStock() || currentBoard.playerHasSevenCards())) {
           return MarketCardsCheck.checkMarketCards(gamemodel, elementsToUpdate);
        }


        //todo try to mock test
        else if (currentBoard.playerHasSevenCards() && !gamemodel.isSinglePlayer())

        {

                int currentPlayerIndex = gamemodel.getPlayerPosition(gamemodel.getCurrentPlayer());


                Map<Integer, Player> playersEndingTheGame = new HashMap<>();
                playersEndingTheGame.put(currentPlayerIndex, gamemodel.getPlayer(currentPlayerIndex).get());
                gamemodel.setPlayersEndingTheGame(playersEndingTheGame);

                String reasonOfEnd = EndGameReason.SEVENTH_CARD_PURCHASED.getEndGameReason();
                gamemodel.getThisMatch().setReasonOfGameEnd(reasonOfEnd);
                FinalStrategy.setLastTurnMacroGamePhase(gamemodel, elementsToUpdate);

        }

        return FinalStrategy.handleCommonEndGameStrategy(elementsToUpdate,gamemodel);


    }
}