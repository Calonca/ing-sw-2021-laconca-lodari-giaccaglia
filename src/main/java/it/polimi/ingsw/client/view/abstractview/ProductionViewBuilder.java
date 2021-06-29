package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.middle.MiddleProductionViewCLI;
import it.polimi.ingsw.client.view.GUI.ProductionViewGUI;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ChooseResourcesForProductionEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.FinalProductionPhaseEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ToggleProductionAtPosition;
import it.polimi.ingsw.network.simplemodel.SimpleCardCells;

import java.beans.PropertyChangeEvent;
import java.util.List;

import static it.polimi.ingsw.client.simplemodel.State.CHOOSING_PRODUCTION;
import static it.polimi.ingsw.client.simplemodel.State.CHOOSING_RESOURCE_FOR_PRODUCTION;

public abstract class ProductionViewBuilder extends ViewBuilder{

    public static ViewBuilder getBuilder(boolean isCLI) {
        if (isCLI) return new MiddleProductionViewCLI();
        else return new ProductionViewGUI();
    }
   public SimpleCardCells getSimpleCardCells(){
       return getSimpleModel().getElem(SimpleCardCells.class).orElseThrow();
   }


    /**
     * Method called during CHOOSING_PRODUCTION
     * @param pos
     */
    public static void sendChosenProduction(int pos){
        getClient().getServerHandler().sendCommandMessage(new EventMessage(new ToggleProductionAtPosition(pos)));
    }

    /**
     * Method called upon executing production
     */
    public static void sendProduce(){
        getClient().getServerHandler().sendCommandMessage(new EventMessage(new FinalProductionPhaseEvent(0)));
    }

    /**
     * Method called during CHOOSING_RESOURCE_FOR_PRODUCTION
     * @param chosenInputPos are available chosen resources
     * @param chosenOutputRes are chosen output resources
     */
    public static void sendChosenResources(List<Integer> chosenInputPos,List<Integer> chosenOutputRes){
        getClient().getServerHandler().sendCommandMessage(new EventMessage(new ChooseResourcesForProductionEvent(chosenInputPos,chosenOutputRes)));
    }

    /**
     * Called each time the player needs to select a resource for a production
     */
    public abstract void choosingResForProduction();

    /**
     * This method centralizes the interaction with Productions
     * @param evt is not null
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (CHOOSING_RESOURCE_FOR_PRODUCTION.name().equals(propertyName)) {
            choosingResForProduction();
        } else if (CHOOSING_PRODUCTION.name().equals(propertyName)) {
            getClient().changeViewBuilder(ProductionViewBuilder.getBuilder(getClient().isCLI()));
        } else MiddlePhaseViewBuilder.middlePhaseCommonTransition(evt);
    }

}
