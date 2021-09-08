package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.cli.endgame.WinLooseCLI;
import it.polimi.ingsw.client.view.gui.WinLooseGUI;

public abstract class WinLooseBuilder extends ViewBuilder{
    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new WinLooseCLI();
        else return new WinLooseGUI();
    }

}
