package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.WaitingStartCLI;
import it.polimi.ingsw.client.view.GUI.WaitingStartGUI;

import java.beans.PropertyChangeEvent;

public abstract class WaitingStartViewBuilder extends ViewBuilder {

    public static ViewBuilder getBuilder(boolean isCLI) {
        if (isCLI) return new WaitingStartCLI();
        else return new WaitingStartGUI();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(CommonData.matchesDataString)||evt.getPropertyName().equals(CommonData.thisMatchData))
            getClient().changeViewBuilder(WaitingStartViewBuilder.getBuilder(getClient().isCLI()));
    }
}
