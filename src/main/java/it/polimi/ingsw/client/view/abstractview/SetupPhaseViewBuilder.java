package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.CLI.SetupPhaseCLI;
import it.polimi.ingsw.client.view.GUI.SetupPhaseGUI;

import java.beans.PropertyChangeEvent;

public abstract class SetupPhaseViewBuilder extends ViewBuilder {

    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new SetupPhaseCLI();
        else return new SetupPhaseGUI();
    }

    /**
     * The setup phase just waits for the game to start
     * @param evt is not null
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(State.IDLE.name()))
            getClient().changeViewBuilder(IDLEViewBuilder.getBuilder(getClient().isCLI()));
    }

}
