package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.SpinnerBody;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.CLI.textUtil.LoadableMatches;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.SendNickname;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
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
        joinMatch.setMatchId(uuidPair.getKey());
        Runnable r = () -> getClient().changeViewBuilder(joinMatch);

        return Option.from(
                uuidPair.getKey().toString() + " ",
                Arrays.toString(uuidPair.getValue().getKey()),
                r);

    }

    private Stream<Option> getNewOptionList(){

        Option loadMatch = Option.from("Load Match" , () -> getClient().changeViewBuilder(new LoadableMatches()));
        if(getCommonData().getSavedMatchesData().isEmpty())
            loadMatch.setEnabled(false);

        Option joinMatch = Option.from("Join Match" , () -> getClient().changeViewBuilder(new JoinableMatches()));
        if(getCommonData().getAvailableMatchesData().isEmpty())
            joinMatch.setEnabled(false);

        Option newMatch = Option.from("New Match",()->getClient().changeViewBuilder(new CreateMatch()));

        return  Stream.of(loadMatch,joinMatch,newMatch);

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
