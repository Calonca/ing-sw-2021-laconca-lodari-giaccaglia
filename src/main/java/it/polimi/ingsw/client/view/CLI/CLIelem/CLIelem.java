package it.polimi.ingsw.client.view.CLI.CLIelem;

import com.google.gson.Gson;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.CLI.CLI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
        this.evt=evt;
        if (updater!=null) {
            updater.run();
        }
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }

    public void setCLIView(CLI cli, Client client){
        this.cli = cli;
        addToPublishers(client);
    }

    public void addToPublishers(Client client){
        client.getCommonData().addPropertyChangeListener(this);
        client.currentPlayerCache().ifPresent(playerCache -> playerCache.addPropertyChangeListener(this));
    }

    public void removeFromPublishers(Client client){
        client.getCommonData().removePropertyChangeListener(this);
        client.currentPlayerCache().ifPresent(playerCache -> playerCache.removePropertyChangeListener(this));
    }

}
