package it.polimi.ingsw.client.view.CLI.CLIelem;

import it.polimi.ingsw.client.view.CLI.CLIView;

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
    protected CLIView cliView;
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
        if (performer!=null && cliView !=null)
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
        return "";
    }

    public void setCLIView(CLIView cliView){
        this.cliView = cliView;
    };
}
