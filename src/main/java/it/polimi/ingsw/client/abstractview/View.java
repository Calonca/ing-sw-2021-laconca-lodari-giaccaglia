package it.polimi.ingsw.client.abstractview;


import it.polimi.ingsw.client.Client;

/**
 * Abstract representation of a command line interface view.
 */
public abstract class View implements Runnable
{
    private Client owner;
    private boolean stopInteraction;


    /**
     * Set the parent object of the view.
     * @param owner The parent object
     */
    public void setOwner(Client owner)
    {
        this.owner = owner;
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
