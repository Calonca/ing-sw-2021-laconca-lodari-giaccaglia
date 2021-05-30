package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.CLI.layout.Grid;

public class GridListBody extends CLIelem {
    Grid grid;

    public GridListBody(Grid grid) {
        this.grid = grid;
    }

    public Grid getGrid() {
        return grid;
    }

    @Override
    public String toString() {
        return grid.toString();
    }
}
