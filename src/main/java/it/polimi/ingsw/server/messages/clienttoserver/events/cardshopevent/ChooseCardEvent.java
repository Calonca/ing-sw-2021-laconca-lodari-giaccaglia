package it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent;

import it.polimi.ingsw.server.model.states.State;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.network.assets.devcards.DevelopmentCardColor;


/**
 * Client side {@link it.polimi.ingsw.network.messages.clienttoserver.events.Event Event} created when {@link GameModel#currentPlayer currentPlayer} wants to purchase a {@link it.polimi.ingsw.server.model.cards.DevelopmentCard DevelopmentCard}
 * during {@link State#CHOOSING_DEVELOPMENT_CARD CHOOSING_DEVELOPMENT_CARD} by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class ChooseCardEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseCardEvent implements Validable {

    public ChooseCardEvent() {}

    /**
     * @return true if {@link ChooseCardEvent#cardLevel} belongs to {@link it.polimi.ingsw.server.model.cards.CardShop CardShop}
     * levels range values, otherwise false.
     */
    private boolean validateLevel(){
        return cardLevel <= gameModel.getMaxDevCardLevelInCardShop() && cardLevel>=0;
    }

    /**
     * @return true if {@link ChooseCardEvent#cardColorNumber} belongs to {@link DevelopmentCardColor}
     * int range values, otherwise false.
     */
    private boolean validateColor(){
        return cardColorNumber >= 0 && cardColorNumber < DevelopmentCardColor.values().length;
    }

    /**
     * @return true if chosen card is still available in {@link it.polimi.ingsw.server.model.cards.CardShop CardShop}, otherwise
     * action shouldn't have been performed, meaning client has been modified, thus event has to be invalidated by returning false.
     */
    private boolean isCardAvailable(){
        return gameModel.isDevCardInShopAvailable(cardColorNumber, cardLevel);
    }

    @Override
    public boolean validate(GameModel gameModel) {
        initializeMiddlePhaseEventValidation(gameModel);
        DevelopmentCard cardToPurchase = gameModel.getCardShop().getCardCopy(DevelopmentCardColor.fromInt(cardColorNumber), cardLevel);
        return isGameStarted(gameModel) && validateLevel() && validateColor() && isCardAvailable() && validateDevCardRequirements(cardToPurchase);
    }

}
