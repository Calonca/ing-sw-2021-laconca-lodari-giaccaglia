package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.IDLE.IDLEViewBuilderCLI;
import it.polimi.ingsw.client.view.GUI.IDLEViewBuilderGUI;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.client.simplemodel.State.*;

public abstract class IDLEViewBuilder extends ViewBuilder{
    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new IDLEViewBuilderCLI();
        else return new IDLEViewBuilderGUI();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (INITIAL_PHASE.name().equals(propertyName)) {
            getClient().changeViewBuilder(InitialOrFinalPhaseViewBuilder.getBuilder(getClient().isCLI(),true));
        }else if (MIDDLE_PHASE.name().equals(propertyName)) {
            getClient().changeViewBuilder(MiddlePhaseViewBuilder.getBuilder(getClient().isCLI()));
        }
        else if(IDLE.name().equals(propertyName)){
            getClient().changeViewBuilder(IDLEViewBuilder.getBuilder(getClient().isCLI()));
        }
        else ViewBuilder.printWrongStateReceived(evt);
    }
}
