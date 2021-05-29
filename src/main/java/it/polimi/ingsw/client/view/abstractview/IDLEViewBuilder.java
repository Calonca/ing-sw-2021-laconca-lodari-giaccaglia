package it.polimi.ingsw.client.view.abstractview;

public abstract class IDLEViewBuilder extends ViewBuilder{
    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new it.polimi.ingsw.client.view.CLI.IDLEViewBuilder();
        else return new it.polimi.ingsw.client.view.GUI.IDLEViewBuilder();
    }
}
