package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.SetupPhase;

public abstract class SetupPhaseViewBuilder extends ViewBuilder {

    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new SetupPhase();
        else return new it.polimi.ingsw.client.view.GUI.SetupPhase();
    }

}
