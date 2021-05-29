package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.SpinnerBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.VerticalListBody;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;

import java.beans.PropertyChangeEvent;
import java.util.*;
import java.util.stream.Stream;

public class CreateJoinLoadMatch extends CreateJoinLoadMatchViewBuilder implements CLIBuilder {


    @Override
    public void run() {
        SpinnerBody spinnerBody = new SpinnerBody();
        getCLIView().setTitle(new Title("Waiting for matches data"));

        spinnerBody.performWhenReceiving(CommonData.matchesDataString);
        spinnerBody.setPerformer(()->
                {
                    getCLIView().clearScreen();
                    getCLIView().setTitle(new Title("Hey "+getCommonData().getCurrentnick()+", what do you want to do?"));
                    //Initial options
                    Stream<Option> optionsToAdd = getNewOptionList(getClient().getCommonData().getMatchesData());

                    VerticalListBody optionList = new VerticalListBody(optionsToAdd);

                    optionList.performWhenReceiving(CommonData.matchesDataString);
                    Runnable performer = ()->{
                        Optional<Map<UUID,String[]>> list = (Optional<Map<UUID, String[]>>) optionList.getEvt().getNewValue();
                        optionList.updateOptions(getNewOptionList(list),getClient());
                        getCLIView().setBody(optionList);
                        getCLIView().refreshCLI();
                    };
                    optionList.setPerformer(performer);

                    getCLIView().setBody(optionList);
                    getCLIView().refreshCLI();
                    getCLIView().performLastChoice();

                }
        );
        getCLIView().setBody(spinnerBody);
        getCLIView().refreshCLI();
    }

    private Option getOption(Map.Entry<UUID, String[]> uuidPair) {
        JoinMatch joinMatch = new JoinMatch();
        joinMatch.setMatchId(uuidPair.getKey());
        Runnable r = () -> getClient().changeViewBuilder(joinMatch);
        return Option.from(
                    uuidPair.getKey().toString(),
                    Arrays.toString(uuidPair.getValue()),
                    r);

    }

    private Stream<Option> getNewOptionList(Optional<Map<UUID, String[]>> matchesData){
        Stream<Option> optionsToAdd = matchesData.map(
                (o)-> o.entrySet().stream().map(this::getOption)).orElse(Stream.empty());

        Option newMatch = Option.from("New Match",()->getClient().changeViewBuilder(new CreateMatch()));
        return  Stream.concat(optionsToAdd,Stream.of(newMatch));
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
