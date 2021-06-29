package it.polimi.ingsw.client.view.CLI.match;

import it.polimi.ingsw.client.view.CLI.CLIBuilder;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public class JoinableMatches extends CreateJoinLoadMatchCLI implements CLIBuilder {


    /**
     * The player is asked to choose a match to join, after they are displayed
     */
    @Override
    public void run() {

        getCLIView().clearScreen();
        getCLIView().setTitle( new Title( "Hey " + getCommonData().getCurrentNick() + ", here are the available matches to join" ) );


        Map<UUID, Pair<String[], String[]>> availableMatches = getCommonData().getAvailableMatchesData().get();
        Stream<Option> optionsToAdd = getMatchesOptionList( availableMatches );

        Row initialRow = new Row( optionsToAdd );

        CanvasBody horizontalListBody = CanvasBody.centered( initialRow );

        getCLIView().setBody( horizontalListBody );
        initialRow.selectAndRunOption( getCLIView() );
        getCLIView().show();
    }



    @Override
    public void propertyChange (PropertyChangeEvent evt) {

    }


}
