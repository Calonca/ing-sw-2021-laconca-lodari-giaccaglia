package it.polimi.ingsw.client.view.CLI.match;

import it.polimi.ingsw.client.CommonData;
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

public class LoadableMatches extends CreateJoinLoadMatchCLI implements CLIBuilder {


    /**
     * This method displays the loadable matches, and then the player is asked to choose one
     */
    @Override
    public void run() {

        getCLIView().clearScreen();

        getCLIView().setTitle( new Title( "Hey " + getCommonData().getCurrentNick() + ", here are the available matches to load" ));


                    Map<UUID, Pair<String[], String[]>> savedMatches = getCommonData().getSavedMatchesData().get();
                    Stream<Option> optionsToAdd = getMatchesOptionList( savedMatches );

                    Row initialRow = new Row( optionsToAdd );

                    CanvasBody horizontalListBody = CanvasBody.centered( initialRow );

                    horizontalListBody.performWhenReceiving(CommonData.matchesDataString);

                    Runnable performer = ()->{

                        Map<UUID, Pair<String[], String[]>> updatedSavedMatches = getCommonData().getSavedMatchesData().get();
                        Stream<Option> updatedOptionsToAdd = getMatchesOptionList( updatedSavedMatches );

                        Row updatedRow = new Row((updatedOptionsToAdd));
                        getCLIView().setBody(CanvasBody.centered(updatedRow));
                        updatedRow.selectAndRunOption(getCLIView());
                        getCLIView().show();

                    };

                    horizontalListBody.setPerformer(performer);
                    getCLIView().setBody( horizontalListBody );
                    initialRow.selectAndRunOption( getCLIView() );
                    getCLIView().show();

    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
