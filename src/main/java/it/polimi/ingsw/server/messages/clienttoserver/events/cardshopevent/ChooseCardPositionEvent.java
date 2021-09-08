package it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;

/**
 * Client side Event created when {@link GameModel#currentPlayer currentPlayer} has to place a {@link it.polimi.ingsw.server.model.cards.DevelopmentCard NetworkDevelopmentCard}
 * in his {@link it.polimi.ingsw.server.model.player.board.PersonalBoard PersonalBoard} during
 * {@link State#CHOOSING_POSITION_FOR_DEVCARD CHOOSING_POSITION_FOR_DEVCARD} phase by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class ChooseCardPositionEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseCardPositionEvent implements Validable{

    private transient DevelopmentCard chosenCard;

    private transient PersonalBoard currentPlayerPersonalBoard;

    /**
     * Server-side initializer to setup common attributes among {@link State#MIDDLE_PHASE MIDDLE_PHASE}
     * events.
     * @param gameModel {@link GameModel} of the event's current game on which event validation has to be performed.
     */
    private void initializeMiddlePhaseEventValidation(GameModel gameModel){
        this.currentPlayerPersonalBoard = gameModel.getCurrentPlayer().getPersonalBoard();
    }

    @Override
    public boolean validate(GameModel gameModel) {
        initializeMiddlePhaseEventValidation(gameModel);
        this.chosenCard = gameModel.getCardShop().getCopyOfPurchasedCard();
        return isGameStarted(gameModel) && checkCardPosition() && checkSpotAvailability();
    }

    /**
     * @return true if chosen {@link ChooseCardPositionEvent#position} is an existing one in
     * {@link it.polimi.ingsw.server.model.player.board.PersonalBoard PersonalBoard}, otherwise false.
     */
    private boolean checkCardPosition(){
        return currentPlayerPersonalBoard.getCardCells().size() >= position;
    }

    /**
     * @return true if chosen {@link ChooseCardPositionEvent#position} is compatible with {@link it.polimi.ingsw.server.model.cards.CardShop#purchasedCard purchasedCard}
     * in terms of space and card colour, otherwise false.
     */
    private boolean checkSpotAvailability(){
        return currentPlayerPersonalBoard.getCardCells().get(position).isSpotAvailable(chosenCard);
    }

    public int getCardPositionInPersonalBoard(){
        return position;
    }
}
