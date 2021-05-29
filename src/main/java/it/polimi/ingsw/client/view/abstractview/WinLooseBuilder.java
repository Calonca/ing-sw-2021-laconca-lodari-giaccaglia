package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.InitialOrFinalPhaseCLI;
import it.polimi.ingsw.client.view.CLI.WinLooseCLI;
import it.polimi.ingsw.client.view.GUI.InitialOrFinalPhaseGUI;
import it.polimi.ingsw.client.view.GUI.WinLooseGUI;

public abstract class WinLooseBuilder extends ViewBuilder{
    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new WinLooseCLI();
        else return new WinLooseGUI();
    }
}
