package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableFaithTrack;
import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;

public class PersonalBoardBody extends CLIelem {

    DrawableFaithTrack faithTrack;

    public PersonalBoardBody(SimpleFaithTrack simpleTrack) {
        this.faithTrack = new DrawableFaithTrack(simpleTrack);
    }

    @Override
    public String toString() {
        return faithTrack.toString();
    }
}
