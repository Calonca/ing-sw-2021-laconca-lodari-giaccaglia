package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.cli.idle.IDLEViewBuilderCLI;
import it.polimi.ingsw.client.view.gui.IDLEViewBuilderGUI;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.client.simplemodel.State.*;

public abstract class IDLEViewBuilder extends ViewBuilder{
    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new IDLEViewBuilderCLI();
        else return new IDLEViewBuilderGUI();
    }


    /**
     * This listener centralizes the actions regarding player's waiting for  and starting their turn
     * @param evt not null
     */
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
        else if (END_PHASE.name().equals(propertyName)) {
            getClient().changeViewBuilder(WinLooseBuilder.getBuilder(getClient().isCLI()));
        }
    }
}
