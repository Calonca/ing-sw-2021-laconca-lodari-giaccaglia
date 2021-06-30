package it.polimi.ingsw.client.view.CLI.match;

import it.polimi.ingsw.client.view.CLI.CLIBuilder;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import javafx.util.Pair;

import java.util.Map;
import java.util.UUID;

public class JoinableMatchesCLI extends CreateJoinLoadMatchCLI implements CLIBuilder {


    /**
     * The player is asked to choose a match to join, after they are displayed
     */
    @Override
    public void run() {

        getCLIView().clearScreen();
        getCLIView().setTitle( new Title( "Hey " + getCommonData().getCurrentNick() + ", here are the available matches to join" ));

        Map<UUID, Pair<String[], String[]>> savedMatches = getCommonData().getAvailableMatchesData().get();
        Column grid = buildMatchInfo(savedMatches);
        CanvasBody body = CanvasBody.centered(grid);

        getCLIView().setBody(body);
        grid.selectInEnabledOption(getCLIView(), "Select an option");
        getCLIView().show();

    }



}
