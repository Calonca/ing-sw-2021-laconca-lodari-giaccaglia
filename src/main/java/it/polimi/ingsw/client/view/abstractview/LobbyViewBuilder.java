package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.cli.match.LobbyViewBuilderCLI;
import it.polimi.ingsw.client.view.gui.LobbyViewBuilderGUI;

public abstract class LobbyViewBuilder extends ViewBuilder {

    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new LobbyViewBuilderCLI();
        else return new LobbyViewBuilderGUI();
    }



}
