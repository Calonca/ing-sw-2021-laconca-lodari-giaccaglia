package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.CLI.layout.HorizontalList;

public class HorizontalListBody extends CLIelem {
    HorizontalList horizontalList;

    public HorizontalListBody(HorizontalList horizontalList) {
        this.horizontalList = horizontalList;
    }

    public HorizontalList getHorizontalList() {
        return horizontalList;
    }

    @Override
    public String toString() {
        return horizontalList.toString();
    }
}
