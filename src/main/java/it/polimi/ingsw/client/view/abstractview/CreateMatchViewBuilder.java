package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.CreateMatch;

public abstract class CreateMatchViewBuilder extends ViewBuilder {

    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new CreateMatch();
        else return new it.polimi.ingsw.client.view.GUI.CreateMatch();
    }

}
