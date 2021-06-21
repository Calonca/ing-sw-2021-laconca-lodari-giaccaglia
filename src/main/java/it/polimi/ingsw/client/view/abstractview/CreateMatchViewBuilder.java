package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.match.CreateMatch;
import it.polimi.ingsw.client.view.GUI.MatchToStart;

public abstract class CreateMatchViewBuilder extends ViewBuilder {

    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new CreateMatch();
        else return new MatchToStart();
    }

}
