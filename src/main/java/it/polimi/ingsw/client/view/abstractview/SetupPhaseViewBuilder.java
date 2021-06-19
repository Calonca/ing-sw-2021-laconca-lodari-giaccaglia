package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.CLI.SetupPhase;

import java.beans.PropertyChangeEvent;

public abstract class SetupPhaseViewBuilder extends ViewBuilder {

    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new SetupPhase();
        else return new it.polimi.ingsw.client.view.GUI.SetupPhase();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(State.IDLE.name()))
            getClient().changeViewBuilder(IDLEViewBuilder.getBuilder(getClient().isCLI()));
        else{
            printWrongStateReceived(evt);
        }
    }

}
