package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.abstractview.InitialPhaseViewBuilder;

import java.beans.PropertyChangeEvent;

public class InitialPhase extends InitialPhaseViewBuilder implements CLIBuilder {

    @Override
    public void run() {
        getCLIView().setTitle(new Title("Initial Phase"));
        getCLIView().displayWithScroll();
    }


}
