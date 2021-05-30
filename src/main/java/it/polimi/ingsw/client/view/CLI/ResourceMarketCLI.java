package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;

import java.beans.PropertyChangeEvent;

public class ResourceMarketCLI extends ResourceMarketViewBuilder implements CLIBuilder {
    @Override
    public void run() {
        getCLIView().setTitle("Take you pick");
        getCLIView().show();
    }

    @Override
    public void choosePositions() {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

}
