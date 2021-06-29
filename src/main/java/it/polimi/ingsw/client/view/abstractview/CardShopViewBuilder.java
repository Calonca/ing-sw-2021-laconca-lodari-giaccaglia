package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.CardShopCLI;
import it.polimi.ingsw.client.view.GUI.CardShopGUI;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseCardEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseCardPositionEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseResourceForCardShopEvent;
import it.polimi.ingsw.network.simplemodel.SimpleCardShop;

import java.beans.PropertyChangeEvent;
import java.util.List;

import static it.polimi.ingsw.client.simplemodel.State.CHOOSING_POSITION_FOR_DEVCARD;
import static it.polimi.ingsw.client.simplemodel.State.CHOOSING_RESOURCES_FOR_DEVCARD;

public abstract class CardShopViewBuilder extends ViewBuilder{

    public static boolean viewing;

    public static boolean isIdlePhase;

    /**
     * Method to initialize the viewBuilder
     * @param viewing represents the card shop state
     */
    public CardShopViewBuilder(boolean viewing) {
        CardShopViewBuilder.viewing = viewing;
        isIdlePhase = false;
    }


    /**
     *
     * @param viewing represents the card shop state
     * @param isIdle represents the player state
     */
    public CardShopViewBuilder(boolean viewing, boolean isIdle){
        CardShopViewBuilder.viewing = viewing;
        isIdlePhase = isIdle;
    }

    public CardShopViewBuilder() { CardShopViewBuilder.viewing =true;
    }

    public static ViewBuilder getBuilder(boolean isCLI, boolean viewing) {
        if (isCLI) return new CardShopCLI(viewing);
        else return new CardShopGUI(viewing);
    }


    /**
     * Method used in CHOOSING_RESOURCE_FOR_DEVCARD
     * @param positions are board resources' position
     */
    public static void sendResourcesToBuy(List<Integer> positions){
        getClient().getServerHandler().sendCommandMessage(new EventMessage(new ChooseResourceForCardShopEvent(positions)));
    }

    /**CHOOSING_POSITION_FOR_DEVCARD
     * Method used in CHOOSING_DEVELOPMENT_CARD
     * @param cardColor represents a valid development card color
     * @param cardLevel is a valid development card level
     */
    protected void sendChosenCard(int cardColor, int cardLevel){
        getClient().getServerHandler().sendCommandMessage(new EventMessage(new ChooseCardEvent(cardColor,cardLevel)));
    }


    /**
     * Method used inCHOOSING_POSITION_FOR_DEVCARD
     * @param position represents a SimpleCardCells position
     */
    public static void sendCardPlacementPosition(int position) {
        getClient().getServerHandler().sendCommandMessage(new EventMessage(new ChooseCardPositionEvent(position)));
    }


    /**
     * Get called when choosing resources to buy the card
     */
    public abstract void selectResources();

    /**
     * Gets called when the player needs to place the development card on his personal board
     */
    public abstract void choosePositionForCard();

    public static SimpleCardShop getSimpleCardShop(){
        return getSimpleModel().getElem(SimpleCardShop.class).orElseThrow();
    }


    /**
     * This method centralizes all the actions regarding the interaction with the CardShop
     * @param evt can't be null
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (CHOOSING_RESOURCES_FOR_DEVCARD.name().equals(propertyName)) {
            selectResources();
        } else if (CHOOSING_POSITION_FOR_DEVCARD.name().equals(propertyName)) {
            choosePositionForCard();
        } else MiddlePhaseViewBuilder.middlePhaseCommonTransition(evt);
    }

}
