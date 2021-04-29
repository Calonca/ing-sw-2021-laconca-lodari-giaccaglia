package it.polimi.ingsw.client.view.CLI;

import java.beans.PropertyChangeEvent;

public class InitialPhaseView extends it.polimi.ingsw.client.view.abstractview.InitialPhaseView implements CLIView {
    public InitialPhaseView(String leaderAndChooseNumber) {
        super(leaderAndChooseNumber);
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
        System.out.println(leaderAndChooseNumber);
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
