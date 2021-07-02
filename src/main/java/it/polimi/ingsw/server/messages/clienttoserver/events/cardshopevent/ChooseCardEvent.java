package it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;


/**
 * Client side {@link it.polimi.ingsw.network.messages.clienttoserver.events.Event Event} created when {@link GameModel#currentPlayer currentPlayer} wants to purchase a {@link it.polimi.ingsw.server.model.cards.DevelopmentCard NetworkDevelopmentCard}
 * during {@link State#CHOOSING_DEVELOPMENT_CARD CHOOSING_DEVELOPMENT_CARD} by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class ChooseCardEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseCardEvent implements Validable {

    public ChooseCardEvent() {}


    /**
     * {@link GameModel} of the event's current game on which event validation has to be performed.
     */
    private transient GameModel gameModel;
    private transient PersonalBoard currentPlayerPersonalBoard;

    /**
     * Server-side initializer to setup common attributes among {@link State#MIDDLE_PHASE MIDDLE_PHASE}
     * events.
     * @param gameModel {@link GameModel} of the event's current game on which event validation has to be performed.
     */
    private void initializeMiddlePhaseEventValidation(GameModel gameModel){
        this.gameModel = gameModel;
        this.currentPlayerPersonalBoard = gameModel.getCurrentPlayer().getPersonalBoard();
    }

    /**
     * Method to verify if current player's {@link it.polimi.ingsw.server.model.cards.CardShop#purchasedCard devCard}
     * from {@link it.polimi.ingsw.server.model.cards.CardShop CardShop} in
     * {@link State#CHOOSING_DEVELOPMENT_CARD CHOOSING_DEVELOPMENT_CARD} phase has
     * resources and level requirements matching current player's {@link it.polimi.ingsw.server.model.player.board.PersonalBoard PersonalBoard}
     * availability.
     * @return true if resources and level requirements are satisfied, otherwise false
     */
    private boolean validateDevCardRequirements(DevelopmentCard card){
        return currentPlayerPersonalBoard.areDevCardRequirementsSatisfied(card);
    }

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
        if(gameModel.getCardShop().isDeckEmpty(DevelopmentCardColor.fromInt(cardColorNumber), cardLevel))
            return false;

        DevelopmentCard cardToPurchase = gameModel.getCardShop().getCopyOfCardOnTop(DevelopmentCardColor.fromInt(cardColorNumber), cardLevel);
        return isGameStarted(gameModel) && validateLevel() && validateColor() && isCardAvailable() && validateDevCardRequirements(cardToPurchase);
    }

    public int getCardColorNumber(){
        return cardColorNumber;
    }

    public int getCardLevel(){
        return cardLevel;
    }



}
