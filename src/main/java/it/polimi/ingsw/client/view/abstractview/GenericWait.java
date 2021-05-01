package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.PlayerCache;
import it.polimi.ingsw.client.view.CLI.SetupPhaseView;

import java.beans.PropertyChangeEvent;

/**
 * Runs the runnable when the the value connected to {@link #runWhenReceiving} is updated.<br>
 * While running displays the {@link #message}
 */
public abstract class GenericWait extends View{

    public static GenericWait matchToStart(Client client){
        return new it.polimi.ingsw.client.view.CLI.GenericWait("Match to start",
                PlayerCache.SETUP_PHASE,
                ()->client.transitionToView(new SetupPhaseView()));

    }

    protected final Runnable r;
    protected final String message;
    protected final String runWhenReceiving;


    public GenericWait(String message, String runWhenReceiving, Runnable r) {
        this.r = r;
        this.message = message;
        this.runWhenReceiving = runWhenReceiving;
    }

    /**
     * Waiting indefinitely
     */
    public GenericWait(String message) {
        this.r = ()->{};
        this.message = message;
        this.runWhenReceiving = "";
    }

    /**
     * The main method of the view. Handles user interaction. User interaction
     * is considered ended when this method exits.
     *
     * @implNote This method shall exit as soon as possible after stopInteraction()
     * is called (from another thread).
     */
    @Override
    public void run() {

    }

    public void perform() {
        r.run();
    }


    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(runWhenReceiving))
            perform();
    }
}
