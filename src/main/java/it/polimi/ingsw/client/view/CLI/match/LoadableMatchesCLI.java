package it.polimi.ingsw.client.view.CLI.match;

import it.polimi.ingsw.client.view.CLI.CLIBuilder;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import javafx.util.Pair;

import java.util.Map;
import java.util.UUID;

public class LoadableMatchesCLI extends CreateJoinLoadMatchCLI implements CLIBuilder {


    /**
     * This method displays the loadable matches, and then the player is asked to choose one
     */
    @Override
    public void run() {

        getCLIView().clearScreen();

        getCLIView().setTitle( new Title( "Hey " + getCommonData().getThisPlayerNickname() + ", here are the available matches to load" ));

        Map<UUID, Pair<String[], String[]>> savedMatches = getCommonData().getSavedMatchesData().get();

        Column grid = buildMatchInfo(savedMatches);

        CanvasBody body = CanvasBody.centered(grid);
        getCLIView().setBody(body);
        grid.selectInEnabledOption(getCLIView(), "Select an option");
        getCLIView().show();

    }

}
