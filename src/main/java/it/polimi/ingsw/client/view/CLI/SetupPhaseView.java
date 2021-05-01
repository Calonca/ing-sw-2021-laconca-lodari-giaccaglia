package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.PlayerCache;

import java.beans.PropertyChangeEvent;

public class SetupPhaseView extends it.polimi.ingsw.client.view.abstractview.SetupPhaseView implements CLIView {

    /**
     * The main method of the view. Handles user interaction. User interaction
     * is considered ended when this method exits.
     *
     * @implNote This method shall exit as soon as possible after stopInteraction()
     * is called (from another thread).
     */
    @Override
    public void run() {
        System.out.println(getClient().getFromStateAndKey(PlayerCache.SETUP_PHASE,"leaders").orElse("no leaders"));
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
