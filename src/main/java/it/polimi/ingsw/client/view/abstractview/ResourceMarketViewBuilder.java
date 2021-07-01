package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.middle.MiddleResourceMarketCLI;
import it.polimi.ingsw.client.view.GUI.ResourceMarketGUI;
import it.polimi.ingsw.network.assets.marbles.MarbleAsset;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.ChooseLineEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.ChooseWhiteMarbleConversionEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.DiscardResourcesEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.MoveResourceEvent;
import it.polimi.ingsw.network.simplemodel.SimpleMarketBoard;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.client.simplemodel.State.CHOOSING_POSITION_FOR_RESOURCES;
import static it.polimi.ingsw.client.simplemodel.State.CHOOSING_WHITEMARBLE_CONVERSION;

/**
 * Used to take the resources from the market and place them in the personalBoard
 */
public abstract class ResourceMarketViewBuilder extends ViewBuilder{

    public static ViewBuilder getBuilder(boolean isCLI) {
        if (isCLI) return new MiddleResourceMarketCLI();
        else return new ResourceMarketGUI();
    }

    /**
     * @param line 0 to 2 for rows and 3 to 6 for columns
     */
    public void sendLine(int line){
        getClient().getServerHandler().sendCommandMessage(new EventMessage(new ChooseLineEvent(line)));
    }


    /**
     * Method used during CHOOSING_WHITEMARBLE_CONVERSION
     * @param resourceNumber is an active white marble conversion bonus
     */
    public static void sendWhiteMarbleConversion(int resourceNumber){
        getClient().getServerHandler().sendCommandMessage(new EventMessage(new ChooseWhiteMarbleConversionEvent(resourceNumber)));
    }

    /**
     * Method used after selecting a line to place the taken resources
     */
    public static void sendMove(int startPos, int endPos){
        getClient().getServerHandler().sendCommandMessage(new EventMessage(new MoveResourceEvent(startPos, endPos)));
    }


    /**
     * Method used after finishing to place the taken resources
     */
    public static void sendDiscard(){
        getClient().getServerHandler().sendCommandMessage(new EventMessage(new DiscardResourcesEvent()));
    }

    /**
     * Called every time a resource is moved in the personalBoard or when the discardBox is received
     */
    public abstract void choosePositions();

    public MarbleAsset[][] getMarketMatrix(){
        return getSimpleMarketBoard().getMarbleMatrix();
    }
    public SimpleMarketBoard getSimpleMarketBoard(){
        return getSimpleModel().getElem(SimpleMarketBoard.class).orElseThrow();
    }


    /**
     * This listener centralizes the interaction with ResourceMarket
     * @param evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (CHOOSING_POSITION_FOR_RESOURCES.name().equals(propertyName)) {
            choosePositions();
        } else  if (CHOOSING_WHITEMARBLE_CONVERSION.name().equals(propertyName)) {
            chooseMarbleConversion();
        } else MiddlePhaseViewBuilder.middlePhaseCommonTransition(evt);
    }


    /**
     * Method used when more than one market leader is active and white marbles are picked in the Resource Market
     */
    public abstract void chooseMarbleConversion();
}
