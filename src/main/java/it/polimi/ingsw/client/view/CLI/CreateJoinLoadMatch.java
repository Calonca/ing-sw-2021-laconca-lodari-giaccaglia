package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.CLI.CLIelem.OptionList;
import it.polimi.ingsw.client.view.CLI.CLIelem.Spinner;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.VerticalListBody;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;

import java.util.*;
import java.util.stream.Stream;

public class CreateJoinLoadMatch extends CreateJoinLoadMatchViewBuilder implements CLIBuilder {


    @Override
    public void run() {
        Spinner spinner = new Spinner("matches data");
        spinner.performWhenReceiving(CommonData.matchesDataString);
        spinner.setPerformer(()->
                {
                    getCLIView().resetCLI();
                    getCLIView().setTitle(new Title("Menu:"));
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
                    getCLIView().displayWithDivider();
                    getCLIView().performLastChoice();

                }
        );
        getCLIView().setSpinner(spinner);
        getCLIView().displayWithDivider();
    }

    private Option getOption(Map.Entry<UUID, String[]> uuidPair) {
        Runnable r = () -> getClient().changeViewBuilder(new JoinMatch(uuidPair.getKey()), this);
        return Option.from(
                    uuidPair.getKey().toString(),
                    Arrays.toString(uuidPair.getValue()),
                    r);

    }

    private Stream<Option> getNewOptionList(Optional<Map<UUID, String[]>> matchesData){
        Stream<Option> optionsToAdd = matchesData.map(
                (o)-> o.entrySet().stream().map(this::getOption)).orElse(Stream.empty());

        Option newMatch = Option.from("New Match",()->getClient().changeViewBuilder(new CreateMatch(), this));
        return  Stream.concat(optionsToAdd,Stream.of(newMatch));
    }


}
