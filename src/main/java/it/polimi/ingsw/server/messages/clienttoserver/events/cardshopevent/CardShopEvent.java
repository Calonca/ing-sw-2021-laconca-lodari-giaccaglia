package it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.State;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.CardShop;
import it.polimi.ingsw.network.assets.devcards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.cards.DevelopmentCardDeck;

/**
 * Client side {@link it.polimi.ingsw.network.messages.clienttoserver.events.Event Event} created when {@link GameModel#currentPlayer currentPlayer} wants to view the {@link CardShop}
 * during {@link State#MIDDLE_PHASE MIDDLE_PHASE} by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class CardShopEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.CardShopEvent implements Validable {

    @Override
    public boolean validate(GameModel gameModel) {
        initializeMiddlePhaseEventValidation(gameModel);
        return isGameStarted(gameModel) && checkCardShopStatus();
    }


    /**
     * <p>Checks if one type of {@link DevelopmentCardColor} in {@link CardShop} is no longer available in
     * {@link CardShop#devDecks devDecks}, meaning that a color list inside {@link CardShop#devDecks devDecks}
     * has only empty {@link DevelopmentCardDeck Decks}. This validation is needed to check if a <em>Single Player</em>
     * game has to end, meaning that current Event shouldn't have taken place and has to be refused.
     * @return false if a list of {@link DevelopmentCardDeck DevelopmentCardDecks} has only empty Decks, otherwise true.
     */
    protected boolean checkCardShopStatus(){
        return gameModel.getSinglePlayer() == null || !gameModel.isSomeDevCardColourOutOfStock();
    }

}
