package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.CLI.CLIelem.OptionList;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;

import java.util.*;
import java.util.stream.Stream;

public class CreateJoinLoadMatch extends CreateJoinLoadMatchViewBuilder implements CLIBuilder {


    /**
     * The main method of the view. Handles user interaction. User interaction
     * is considered ended when this method exits.
     *
     * @implNote This method shall exit as soon as possible after stopInteraction()
     * is called (from another thread).
     */
    @Override
    public void run() {
        getCLIView().resetCLI();
        getCLIView().setTitle(new Title("Menu:"));
        //Initial options
        Stream<Option> optionsToAdd = getNewOptionList(getClient().getCommonData().getMatchesData());

        OptionList optionList = new OptionList(optionsToAdd);

        optionList.performWhenReceiving(CommonData.matchesDataString);
        Runnable performer = ()->{
            Optional<Map<UUID,String[]>> list = (Optional<Map<UUID, String[]>>) optionList.getEvt().getNewValue();
            optionList.updateOptions(getNewOptionList(list),getClient());
            getCLIView().setOptionList(CLIPos.CENTER,optionList);
            getCLIView().update();
        };
        optionList.setPerformer(performer);

        getCLIView().setOptionList(CLIPos.CENTER,optionList);
        getCLIView().displayWithScroll();

        optionList.selectOption();
        getCLIView().performLastChoice();
    }

    private Option getOption(Map.Entry<UUID, String[]> uuidPair) {
        Runnable r = () -> getClient().transitionToView(new JoinMatch(uuidPair.getKey()));
        return Option.from(
                    uuidPair.getKey().toString(),
                    Arrays.toString(uuidPair.getValue()),
                    r);

    }

    private Stream<Option> getNewOptionList(Optional<Map<UUID, String[]>> matchesData){
        Stream<Option> optionsToAdd = matchesData.map(
                (o)-> o.entrySet().stream().map(this::getOption)).orElse(Stream.empty());

        Option newMatch = Option.from("New Match",()->getClient().transitionToView(new CreateMatch()));
        return  Stream.concat(optionsToAdd,Stream.of(newMatch));
    }


}
