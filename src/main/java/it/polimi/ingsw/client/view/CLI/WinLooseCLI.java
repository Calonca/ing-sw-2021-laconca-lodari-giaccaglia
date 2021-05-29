package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.abstractview.WinLooseBuilder;

import java.beans.PropertyChangeEvent;

public class WinLooseCLI extends WinLooseBuilder implements CLIBuilder {
    @Override
    public void run() {
        getCLIView().setTitle("You won or lost");
        getCLIView().show();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
