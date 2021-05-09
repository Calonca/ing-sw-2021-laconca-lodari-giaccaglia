package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.CLI;

import java.beans.PropertyChangeListener;


/**
 * Adds view elements to the view and contains references to useful classes
 */
public abstract class ViewBuilder implements Runnable, PropertyChangeListener
{
    private Client client;
    private CommonData commonData;
    private CLI cli;

    public CLI getCLIView() {
        return cli;
    }

    public static ViewBuilder getBuilder(boolean isCLI){return null;};

    public void setCLIView(CLI cli) {
        this.cli = cli;
    }

    /**
     * Set the parent object of the view.
     * @param client The parent object
     */
    public void setClient(Client client)
    {
        this.client = client;
    }

    public CommonData getCommonData() {
        return commonData;
    }

    public void setCommonData(CommonData commonData) {
        this.commonData = commonData;
    }

    /**
     * Returns The Client.
     */
    public Client getClient()
    {
        return client;
    }

    /**
     * Adds view elements to the CLIView
     */
    abstract public void run();
}
