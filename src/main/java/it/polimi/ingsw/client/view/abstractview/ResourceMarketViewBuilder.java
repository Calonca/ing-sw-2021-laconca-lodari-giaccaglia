package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.ResourceMarketCLI;
import it.polimi.ingsw.client.view.GUI.ResourceMarketGUI;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.ChooseLineEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.DiscardResourcesEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.MoveResourceEvent;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.client.simplemodel.State.*;

/**
 * Used to take the resources from the market and place them in the personalBoard
 */
public abstract class ResourceMarketViewBuilder extends ViewBuilder{

    public static ViewBuilder getBuilder(boolean isCLI) {
        if (isCLI) return new ResourceMarketCLI();
        else return new ResourceMarketGUI();
    }

    protected void sendLine(int line){
        getClient().getServerHandler().sendCommandMessage(new EventMessage(new ChooseLineEvent(line)));
    }

    protected void sendWhiteMarbleConversion(int resourceNumber){
        getClient().getServerHandler().sendCommandMessage(new EventMessage(new ChooseLineEvent(resourceNumber)));
    }

    protected void sendMove(int startPos, int endPos){
        getClient().getServerHandler().sendCommandMessage(new EventMessage(new MoveResourceEvent(startPos, endPos)));
    }

    protected void sendDiscard(){
        getClient().getServerHandler().sendCommandMessage(new EventMessage(new DiscardResourcesEvent()));
    }

    /**
     * Called every time a resource is moved in the personalBoard or when the discardBox is received
     */
    public abstract void choosePositions();



    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (CHOOSING_RESOURCE_FOR_PRODUCTION.name().equals(propertyName)) {
            choosePositions();
        } else MiddlePhaseViewBuilder.middlePhaseCommonTransition(evt);
    }
}
