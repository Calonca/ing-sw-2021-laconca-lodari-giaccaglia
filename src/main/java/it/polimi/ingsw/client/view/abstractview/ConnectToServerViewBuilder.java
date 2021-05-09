package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.ConnectToServer;

public abstract class ConnectToServerViewBuilder extends ViewBuilder {

    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new ConnectToServer();
        else return new it.polimi.ingsw.client.view.GUI.ConnectToServer();
    }

}
