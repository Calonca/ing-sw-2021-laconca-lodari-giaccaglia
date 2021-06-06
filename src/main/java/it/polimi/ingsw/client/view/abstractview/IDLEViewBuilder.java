package it.polimi.ingsw.client.view.abstractview;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.client.simplemodel.State.*;

public abstract class IDLEViewBuilder extends ViewBuilder{
    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new it.polimi.ingsw.client.view.CLI.IDLEViewBuilder();
        else return new it.polimi.ingsw.client.view.GUI.IDLEViewBuilder();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (INITIAL_PHASE.name().equals(propertyName)) {
            getClient().changeViewBuilder(InitialOrFinalPhaseViewBuilder.getBuilder(getClient().isCLI(),true));
        }else if (MIDDLE_PHASE.name().equals(propertyName)) {
            getClient().changeViewBuilder(MiddlePhaseViewBuilder.getBuilder(getClient().isCLI()));
        }
        else ViewBuilder.printWrongStateReceived(evt);
    }
}
