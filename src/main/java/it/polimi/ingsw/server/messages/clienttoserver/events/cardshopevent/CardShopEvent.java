package it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent;

import it.polimi.ingsw.server.messages.clienttoserver.events.MiddlePhaseEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.CardShop;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.cards.DevelopmentCardDeck;

/**
 * Client side {@link it.polimi.ingsw.network.messages.clienttoserver.events.Event} created when {@link GameModel#currentPlayer currentPlayer} wants to view the {@link CardShop}
 * during {@link it.polimi.ingsw.server.model.player.State#MIDDLE_PHASE MIDDLE_PHASE} by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class CardShopEvent extends MiddlePhaseEvent {


    @Override
    public boolean validate(GameModel gameModel) {
        initializeMiddlePhaseEventValidation(gameModel);
        return isGameStarted(gameModel) && checkCardShopStatus();
    }


    /**
     * <p>Checks if one type of {@link DevelopmentCardColor} in {@link CardShop} is no longer available in
     * {@link CardShop#devDecks devDecks}, meaning that a color list inside {@link CardShop#devDecks devDecks}
     * has only empty {@link DevelopmentCardDeck Decks}. This validation is needed to check if game has to end, meaning
     * that current Event shouldn't have taken place and has to be refused.
     * @return true if a list of {@link DevelopmentCardDeck DevelopmentCardDecks} has only empty Decks, otherwise false.
     */
    protected boolean checkCardShopStatus(){
        return gameModel.isSomeDevCardColourOutOfStock();
    }

}
