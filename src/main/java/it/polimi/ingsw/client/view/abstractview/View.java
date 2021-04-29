package it.polimi.ingsw.client.view.abstractview;


import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.CLIBuilder;

/**
 * Abstract representation of a command view.
 * Todo make all the view in abstract view abstract classed
 */
public abstract class View implements Runnable
{
    private Client owner;
    private CommonData commonData;
    private boolean stopInteraction;
    private CLIBuilder cliBuilder;

    public CLIBuilder getCliBuilder() {
        return cliBuilder;
    }

    public void setCliBuilder(CLIBuilder cliBuilder) {
        this.cliBuilder = cliBuilder;
    }

    /**
     * Set the parent object of the view.
     * @param owner The parent object
     */
    public void setOwner(Client owner)
    {
        this.owner = owner;
    }

    public CommonData getCommonData() {
        return commonData;
    }

    public void setCommonData(CommonData commonData) {
        this.commonData = commonData;
    }

    /**
     * The parent object of the view.
     * @return The parent object.
     */
    public Client getClient()
    {
        return owner;
    }


    /**
     * The main method of the view. Handles user interaction. User interaction
     * is considered ended when this method exits.
     * @implNote This method shall exit as soon as possible after stopInteraction()
     * is called (from another thread).
     */
    abstract public void run();


    /**
     * Checks if stopInteraction() has been called at least once.
     * @return If the interaction shall be stopped.
     */
    synchronized protected boolean shouldStopInteraction()
    {
        return stopInteraction;
    }


    /**
     * Sets a flag that informs the run() method to terminate as soon
     * as possible.
     */
    public synchronized void stopInteraction()
    {
        stopInteraction = true;
        notifyAll();
    }
}
