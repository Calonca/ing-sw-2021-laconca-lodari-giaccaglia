package it.polimi.ingsw.client.view.cli.CLIelem;

import com.google.gson.Gson;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.cli.CLI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * An element of a CLIView, for example an option or a Text
 */
public abstract class CLIelem implements PropertyChangeListener {
    /**
     * The updater updates the properties for the toString to work
     */
    private Runnable updater;
    private Runnable performer;
    protected static CLI cli;

    protected CLIelem(){}

    public static void setCli(CLI cli) {
        CLIelem.cli = cli;
    }

    public void perform(){
        if (performer!=null && cli !=null)
            performer.run();
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (updater!=null) {
            updater.run();
        }
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }

    public void addToListeners(Client client){
        client.addToListeners(this);
    }

    public void removeFromListeners(Client client){
        client.removeFromListeners(this);
    }

}
