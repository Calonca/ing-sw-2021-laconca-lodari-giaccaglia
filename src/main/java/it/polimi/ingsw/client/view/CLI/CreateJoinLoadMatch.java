package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.SpinnerBody;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.SendNickname;
import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;

import java.beans.PropertyChangeEvent;
import java.util.*;
import java.util.stream.Stream;

public class CreateJoinLoadMatch extends CreateJoinLoadMatchViewBuilder implements CLIBuilder {

    @Override
    public void run() {

        SpinnerBody spinnerBody = new SpinnerBody();
        getCLIView().setTitle("Waiting for matches data");

        spinnerBody.performWhenReceiving(CommonData.matchesDataString);
        spinnerBody.setPerformer(()->

                {
                    getCLIView().clearScreen();
                    getCLIView().setTitle(new Title("Hey "+ getCommonData().getCurrentNick()+", what do you want to do?"));

                    Stream<Option> optionsToAdd = getNewOptionList();
                    Row initialRow = new Row(optionsToAdd);

                    CanvasBody horizontalListBody = CanvasBody.centered(initialRow);

                    horizontalListBody.performWhenReceiving(CommonData.matchesDataString);
                    Runnable performer = ()->{

                        Stream<Option> updatedOptionsToAdd = getNewOptionList();
                        Row updatedRow = new Row((updatedOptionsToAdd));
                        getCLIView().setBody(CanvasBody.centered(updatedRow));
                        updatedRow.selectAndRunOption(getCLIView());
                        getCLIView().show();

                    };

                    horizontalListBody.setPerformer(performer);
                    getCLIView().setBody(horizontalListBody);
                    initialRow.selectAndRunOption(getCLIView());
                    getCLIView().show();
                }

        );

        getCLIView().setBody(spinnerBody);
        getCLIView().show();

    }

    protected Option getOption(Map.Entry<UUID,Pair<String[], String[]>> uuidPair) {

        JoinMatch joinMatch = new JoinMatch();
        joinMatch.setMatchId( uuidPair.getKey() );
        Runnable r = () -> getClient().changeViewBuilder( joinMatch );

        String matchIdString = "Match ID : " + uuidPair.getKey().toString().substring( 0, 8 );

        String coloredNames = coloredNames( uuidPair.getValue().getKey(), uuidPair.getValue().getValue() );

        return Option.from(
                matchIdString,
                coloredNames,
                r );
    }

    private String coloredNames(String[] onlineNames, String[] offlineNames){

        String[] coloredOnlineNames;
        String[] coloredOfflineNames;

        if(onlineNames.length>0)
            coloredOnlineNames = Arrays
                    .stream(onlineNames)
                    .filter(Objects::nonNull)
                    .map( name -> Color.colorString( name, Color.BIGHT_GREEN ) )
                    .toArray( String[]::new);
        else
            coloredOnlineNames = new String[]{Color.colorString( "No players online", Color.BRIGHT_RED )};

        if(offlineNames.length>0) {
            coloredOfflineNames = Arrays
                    .stream( offlineNames )
                    .filter( Objects::nonNull )
                    .map( name -> Color.colorString( name, Color.BRIGHT_RED ) )
                    .toArray( String[]::new );
        }

        else
            coloredOfflineNames = new String[]{Color.colorString( "No players offline", Color.BRIGHT_RED )};


        return Arrays.toString(ArrayUtils.addAll( coloredOnlineNames, coloredOfflineNames));

    }

    private Stream<Option> getNewOptionList(){

        Option loadMatch = Option.from("Load Match" , () -> getClient().changeViewBuilder(new LoadableMatches()));
        if(getCommonData().getSavedMatchesData().isEmpty())
            loadMatch.setEnabled(false);

        Option joinMatch = Option.from("Join Match" , () -> getClient().changeViewBuilder(new JoinableMatches()));
        if(getCommonData().getAvailableMatchesData().isEmpty())
            joinMatch.setEnabled(false);

        Option newMatch = Option.from("New Match",()->getClient().changeViewBuilder(new CreateMatch()));

        List<Option> optionList = new ArrayList<>();
        Collections.addAll(optionList, loadMatch, joinMatch, newMatch);

        return Option.addSpacesToOptionList(1, optionList);

    }

    protected Stream<Option> getMatchesOptionList(Map<UUID, Pair<String[], String[]>> matchesData){

        Option loadMatch = Option.from("Go back" , () -> {
            getClient().getServerHandler().sendCommandMessage(new SendNickname(getCommonData().getCurrentNick()));
            getClient().changeViewBuilder(new CreateJoinLoadMatch());
        });

        Stream<Option> optionsToAdd = matchesData.entrySet().stream().map(this::getOption);
        return Stream.concat( optionsToAdd,Stream.of(loadMatch));

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

}
