package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.PlayerCache;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder;

import java.beans.PropertyChangeEvent;

public class SetupPhase extends SetupPhaseViewBuilder implements CLIBuilder {

    /**
     * The main method of the view. Handles user interaction. User interaction
     * is considered ended when this method exits.
     *
     * @implNote This method shall exit as soon as possible after stopInteraction()
     * is called (from another thread).
     */
    @Override
    public void run() {

        String title = "Should display the leader";
        getCLIView().setTitle(new Title(title));
        getCLIView().displayWithScroll();
    }

}
