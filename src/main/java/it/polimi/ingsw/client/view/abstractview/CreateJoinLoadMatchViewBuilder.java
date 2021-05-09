package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.CreateJoinLoadMatch;

public abstract class CreateJoinLoadMatchViewBuilder extends ViewBuilder {

    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new CreateJoinLoadMatch();
        else return new it.polimi.ingsw.client.view.GUI.CreateJoinLoadMatch();
    }

}
