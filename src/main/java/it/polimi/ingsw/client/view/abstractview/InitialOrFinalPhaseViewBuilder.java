package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.CLI.IDLEViewBuilder;
import it.polimi.ingsw.client.view.CLI.InitialOrFinalPhaseCLI;
import it.polimi.ingsw.client.view.GUI.InitialOrFinalPhaseGUI;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.client.simplemodel.State.INITIAL_PHASE;
import static it.polimi.ingsw.client.simplemodel.State.MIDDLE_PHASE;

public abstract class InitialOrFinalPhaseViewBuilder extends ViewBuilder {
    public static boolean isInitial;

    public InitialOrFinalPhaseViewBuilder(boolean isInitial) {
        isInitial = isInitial;
    }

    public static ViewBuilder getBuilder(boolean isCLI,boolean initial){
        if (isCLI)
            return new InitialOrFinalPhaseCLI(initial);
        else return new InitialOrFinalPhaseGUI(initial);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (MIDDLE_PHASE.name().equals(propertyName)) {
            getClient().changeViewBuilder(MiddlePhaseViewBuilder.getBuilder(getClient().isCLI()));
        } else if (INITIAL_PHASE.name().equals(propertyName)) {
            getClient().changeViewBuilder(getBuilder(getClient().isCLI(), true));
        }else if (State.FINAL_PHASE.name().equals(propertyName)) {
            getClient().changeViewBuilder(getBuilder(getClient().isCLI(), false));
        }else if (State.END_PHASE.name().equals(propertyName)) {
                getClient().changeViewBuilder(WinLooseBuilder.getBuilder(getClient().isCLI()));
        }else
            System.out.println(getThisPlayerCache().getCurrentState()+" received: " + evt.getPropertyName() + JsonUtility.serialize(evt.getNewValue()));

    }
}
