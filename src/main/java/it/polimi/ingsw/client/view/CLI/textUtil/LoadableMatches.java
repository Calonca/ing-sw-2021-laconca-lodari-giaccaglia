package it.polimi.ingsw.client.view.CLI.textUtil;

import it.polimi.ingsw.client.view.CLI.CLIBuilder;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.SpinnerBody;
import it.polimi.ingsw.client.view.CLI.CreateJoinLoadMatch;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public class LoadableMatches extends CreateJoinLoadMatch implements CLIBuilder {

    @Override
    public void run() {

        SpinnerBody spinnerBody = new SpinnerBody();

        getCLIView().clearScreen();
        getCLIView().setTitle( new Title( "Hey " + getCommonData().getCurrentNick() + ", here are the available matches to load" ) );


        Map<UUID, Pair<String[], String[]>> savedMatches = getCommonData().getSavedMatchesData().get();

        Stream<Option> optionsToAdd = getMatchesOptionList( savedMatches);

        Row initialRow = new Row( optionsToAdd );

        CanvasBody horizontalListBody = CanvasBody.centered( initialRow );

        getCLIView().setBody( horizontalListBody );
        initialRow.selectAndRunOption( getCLIView() );
        getCLIView().show();

    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
