package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.ProductionViewCLI;
import it.polimi.ingsw.client.view.GUI.BoardView3D;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ToggleProductionAtPosition;
import it.polimi.ingsw.network.simplemodel.SimpleCardCells;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.client.simplemodel.State.*;

public abstract class ProductionViewBuilder extends ViewBuilder{

    public static ViewBuilder getBuilder(boolean isCLI) {
        if (isCLI) return new ProductionViewCLI();
        else return new BoardView3D();
    }
   public SimpleCardCells getSimpleCardCells(){
       return getSimpleModel().getElem(SimpleCardCells.class).orElseThrow();
   }

    public static void sendChosenProduction(int pos){
        getClient().getServerHandler().sendCommandMessage(new EventMessage(new ToggleProductionAtPosition(pos)));
    }


    //public void sendChosenResource(list<int> pos){
    //    getClient().getServerHandler().sendCommandMessage(new EventMessage(new ChooseResourcesForProductionEvent(pos)));
    //}

    /**
     * Called each time the player needs to select a resource for a production
     */
    public abstract void choosingResForProduction();

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
