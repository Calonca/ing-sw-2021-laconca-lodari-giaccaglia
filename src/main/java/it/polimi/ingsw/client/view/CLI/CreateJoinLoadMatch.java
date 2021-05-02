package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.CLI.CLIelem.OptionList;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;

import java.beans.PropertyChangeEvent;
import java.security.cert.PKIXRevocationChecker;
import java.util.*;

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
        getCLIView().resetCLIelems();
        getCLIView().setTitle(new Title("Menu:"));

        OptionList optionList = updatedOptionList();

        getCLIView().setOptionList(CLIPos.CENTER,optionList);
        getCLIView().displayWithScroll();

        optionList.selectOption();
        getCLIView().performLastChoice();
    }

    private void addOption(Map.Entry<UUID, String[]> uuidPair, OptionList optionList) {
        Runnable r = () -> getClient().transitionToView(new JoinMatch(uuidPair.getKey()));
        optionList.addOption(
                Option.from(
                        uuidPair.getKey().toString(),
                        Arrays.toString(uuidPair.getValue()),
                        r)
        );
    }

    private OptionList updatedOptionList(){
        OptionList optionList = new OptionList();

        //Adds matches and saved matches to the CLIBuilder
        getClient().getCommonData().getMatchesData().ifPresent(
                (o)-> o.entrySet().forEach((op)->addOption(op,optionList)));

        Option newMatch = Option.from("New Match",()->getClient().transitionToView(new CreateMatch()));
        optionList.addOption(newMatch);

        optionList.performWhenReceiving(CommonData.matchesDataString);
        Runnable performer = ()->{
            getCLIView().setOptionList(CLIPos.CENTER,updatedOptionList());
        };
        optionList.setPerformer(performer);
        return optionList;
    }


}
