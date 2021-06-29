package it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.states.State;

import java.util.List;


/**
 * Client side {@link it.polimi.ingsw.network.messages.clienttoserver.events.Event Event} created when {@link GameModel#currentPlayer currentPlayer} has to pick {@link it.polimi.ingsw.server.model.Resource Resources}
 * from his {@link it.polimi.ingsw.server.model.player.board.PersonalBoard PersonalBoard} deposits during
 * {@link State#CHOOSING_RESOURCES_FOR_DEVCARD CHOOSING_RESOURCES_FOR_DEVCARD} phase by performing a
 * game turn action processed to accomplish server-side client validation.
 */
public class ChooseResourceForCardShopEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseResourceForCardShopEvent implements Validable {

    private transient PersonalBoard currentPlayerPersonalBoard;
    private int[] chosenResourcesAsArray;

    /**
     * Server-side initializer to setup common attributes among {@link State#MIDDLE_PHASE MIDDLE_PHASE}
     * events.
     * @param gameModel {@link GameModel} of the event's current game on which event validation has to be performed.
     */
    private void initializeMiddlePhaseEventValidation(GameModel gameModel){
        this.currentPlayerPersonalBoard = gameModel.getCurrentPlayer().getPersonalBoard();
    }

    /**
     * Method to verify if current player's {@link it.polimi.ingsw.server.model.cards.CardShop#purchasedCard devCard}
     * from {@link it.polimi.ingsw.server.model.cards.CardShop CardShop} in
     * {@link State#CHOOSING_RESOURCES_FOR_DEVCARD CHOOSING_RESOURCES_FOR_DEVCARD} phase has
     * resources and level requirements matching current player's {@link it.polimi.ingsw.server.model.player.board.PersonalBoard PersonalBoard}
     * availability.
     * @return true if resources and level requirements are satisfied, otherwise false
     */
    private boolean validateDevCardRequirements(DevelopmentCard card){
        return currentPlayerPersonalBoard.areDevCardRequirementsSatisfied(card);
    }

    @Override
    public boolean validate(GameModel gameModel) {



        initializeMiddlePhaseEventValidation(gameModel);
        buildResourcesArray();
        applyMarketBonusToRequiredResources(gameModel);



        return isGameStarted(gameModel)
                && checkResourcesPositionIndexes()
                && checkIndexes ()
                && checkResourceRequirements()
                && validateDevCardRequirements(gameModel.getCardShop().getCopyOfPurchasedCard());
    }

    private void applyMarketBonusToRequiredResources(GameModel gameModel){

        int [] discounts = gameModel.getCurrentPlayer().getPersonalBoard().getDiscounts();
        for(int i=0; i<4; i++)
            chosenResourcesAsArray[i] += discounts[i];

    }

    /**
     * @return true if chosen {@link it.polimi.ingsw.server.model.Resource resources} actually match
     * {@link GameModel#currentPlayer currentPlayer} {@link it.polimi.ingsw.server.model.player.board.PersonalBoard PersonalBoard}
     * deposits availability, otherwise false.
     */
    private boolean checkResourceRequirements(){
        return currentPlayerPersonalBoard.hasResources(chosenResourcesAsArray);
    }


    private boolean checkResourcesPositionIndexes(){
        return chosenResources.stream().noneMatch(i -> ( i<-8 || (i>-5 && i<0) ));
    }

    private boolean checkIndexes (){

        return chosenResources
                .stream()
                .allMatch(position -> {

                            int indexOccurrences = (int) chosenResources.stream()
                                    .filter(positionToFind -> positionToFind.equals(position))
                                    .count();

                            if(indexOccurrences == 1)
                                return true;

                            else if (position >= -8 && position <-4) {
                                Resource resource = currentPlayerPersonalBoard.getStrongBox().getResourceAt(position);
                                return indexOccurrences <= currentPlayerPersonalBoard.getStrongBox().getNumberOf(resource);
                            }

                            else return false;

                }
                );

    }

    private void buildResourcesArray(){

       chosenResourcesAsArray = new int[]{0,0,0,0};
        int resourceNumber;

        for (Integer resourceGlobalPosition : chosenResources)
        {
        resourceNumber  =  currentPlayerPersonalBoard.getResourceAtPosition(resourceGlobalPosition).getResourceNumber();
        chosenResourcesAsArray[resourceNumber] =  chosenResourcesAsArray[resourceNumber] + 1;
        }

    }

    public List<Integer> getChosenResources(){
        return chosenResources;
    }



}