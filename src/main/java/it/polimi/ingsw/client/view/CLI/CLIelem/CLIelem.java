package it.polimi.ingsw.client.view.CLI.CLIelem;

import com.google.gson.Gson;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

/**
 * An element of a CLIView, for example an option or a Text
 */
public abstract class CLIelem implements PropertyChangeListener {
    /**
     * The updater updates the properties for the toString to work
     */
    private Runnable updater,performer;
    protected CLI cli;
    private PropertyChangeEvent evt;

    public Runnable getUpdater() {
        return updater;
    }

    public Runnable getPerformer() {
        return performer;
    }

    public CLIelem(){
    }

    public void setUpdater(Runnable updater) {
        this.updater = updater;
    }

    public void performWhenReceiving(String key) {
        this.updater = ()->{
            if (evt.getPropertyName().equals(key))
                perform();
        };
    }

    public void switchToStateWhenReceiving(String key, ViewBuilder view, Client client) {
        this.updater = ()->{
            if (evt.getPropertyName().equals(key))
                client.changeViewBuilder(view);
        };
    }

    public void setPerformer(Runnable performer) {
        this.performer = performer;
    }

    public PropertyChangeEvent getEvt() {
        return evt;
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
        System.out.println(evt.getPropertyName());
        this.evt=evt;
        if (updater!=null) {
            updater.run();
        }
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }

    public abstract int horizontalSize();

    public void setCLIAndUpdateSubscriptions(CLI cli, Client client){
        this.cli = cli;
        addToPublishers(client);
    }

    private void addToPublishers(Client client){
        client.addToPublishers(this);
    }

    public void removeFromPublishers(Client client){
        client.removeFromPublishers(this);
    }

}
