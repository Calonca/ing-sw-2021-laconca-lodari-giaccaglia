package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.simplemodel.PlayerCache;
import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.view.CLI.CLI;

import java.beans.PropertyChangeListener;


/**
 * Adds view elements to the view and contains references to useful classes
 */
public abstract class ViewBuilder implements Runnable, PropertyChangeListener
{
    private static Client client;
    private static CommonData commonData;
    private static SimpleModel simpleModel;
    private static CLI cli;

    public static CLI getCLIView() {
        return cli;
    }

    public static ViewBuilder getBuilder(boolean isCLI){return null;};

    public static PlayerCache getThisPlayerCache(){
        return getSimpleModel().getPlayerCache(getCommonData().getThisPlayerIndex());
    }

    public static void setCLIView(CLI cli) {
        ViewBuilder.cli = cli;
    }

    public static SimpleModel getSimpleModel() {
        return simpleModel;
    }

    public static void setSimpleModel(SimpleModel simpleModel) {
        ViewBuilder.simpleModel = simpleModel;
    }

    /**
     * Set the parent object of the view.
     * @param client The parent object
     */
    public static void setClient(Client client)
    {
        ViewBuilder.client = client;
    }

    public static CommonData getCommonData() {
        return commonData;
    }

    public static void setCommonData(CommonData commonData) {
        ViewBuilder.commonData = commonData;
    }

    /**
     * Returns The Client.
     */
    public static Client getClient()
    {
        return client;
    }

    /**
     * Adds view elements to the CLIView
     */
    abstract public void run();
}
