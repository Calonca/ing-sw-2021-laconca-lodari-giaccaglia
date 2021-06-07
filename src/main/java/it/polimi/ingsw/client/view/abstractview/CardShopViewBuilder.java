package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.CardShopCLI;
import it.polimi.ingsw.client.view.GUI.CardShopGUI;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseCardEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseCardPositionEvent;
import it.polimi.ingsw.network.simplemodel.SimpleCardShop;

import java.beans.PropertyChangeEvent;
import java.util.List;

import static it.polimi.ingsw.client.simplemodel.State.*;

public abstract class CardShopViewBuilder extends ViewBuilder{

    public static boolean viewing;

    public CardShopViewBuilder(boolean viewing) {
        CardShopViewBuilder.viewing = viewing;
    }

    public CardShopViewBuilder() { CardShopViewBuilder.viewing =true;
    }

    public static ViewBuilder getBuilder(boolean isCLI, boolean viewing) {
        if (isCLI) return new CardShopCLI(viewing);
        else return new CardShopGUI(viewing);
    }


    protected void sendResourcesToBuy(List<Integer> positions){
        //Todo uncomment when ChooseResourceForCardShopEvent interface has changed
        //getClient().getServerHandler().sendCommandMessage(new EventMessage(new ChooseResourceForCardShopEvent(positions)));
    }

    protected void sendChosenCard(int cardColor, int cardLevel){
        getClient().getServerHandler().sendCommandMessage(new EventMessage(new ChooseCardEvent(cardColor,cardLevel)));
    }

    protected void sendCardPlacementPosition(int position) {
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

    public SimpleCardShop getSimpleCardShop(){
        return getSimpleModel().getElem(SimpleCardShop.class).orElseThrow();
    }


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
