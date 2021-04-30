package it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent;

import it.polimi.ingsw.server.model.State;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;

/**
 * Client side Event created when {@link GameModel#currentPlayer currentPlayer} has to place a {@link it.polimi.ingsw.server.model.cards.DevelopmentCard DevelopmentCard}
 * in his {@link it.polimi.ingsw.server.model.player.board.PersonalBoard PersonalBoard} during
 * {@link State#CHOOSING_POSITION_FOR_DEVCARD CHOOSING_POSITION_FOR_DEVCARD} phase by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class ChooseCardPositionEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseCardPositionEvent implements Validable{

    @Override
    public boolean validate(GameModel gameModel) {
        initializeMiddlePhaseEventValidation(gameModel);
        return isGameStarted(gameModel) && checkCardPosition() && checkSpotAvailability();
    }

    /**
     * @return true if chosen {@link ChooseCardPositionEvent#position} is an existing one in
     * {@link it.polimi.ingsw.server.model.player.board.PersonalBoard PersonalBoard}, otherwise false.
     */
    private boolean checkCardPosition(){
        return !(currentPlayerPersonalBoard.getCardCells().size() < position);
    }

    /**
     * @return true if chosen {@link ChooseCardPositionEvent#position} is compatible with {@link it.polimi.ingsw.server.model.cards.CardShop#purchasedCard purchasedCard}
     * in terms of space and card colour, otherwise false.
     */
    private boolean checkSpotAvailability(){
        return currentPlayerPersonalBoard.getCardCells().get(position).isSpotAvailable(chosenCard);
    }
}
