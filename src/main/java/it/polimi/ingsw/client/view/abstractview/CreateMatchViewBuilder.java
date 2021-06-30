package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.match.CreateMatchCLI;
import it.polimi.ingsw.client.view.GUI.CreateMatchGUI;

public abstract class CreateMatchViewBuilder extends ViewBuilder {

    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new CreateMatchCLI();
        else return new CreateMatchGUI();
    }

}
