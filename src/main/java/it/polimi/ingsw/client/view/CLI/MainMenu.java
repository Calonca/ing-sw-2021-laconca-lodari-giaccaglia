package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.Spinner;
import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.CLI.CLIelem.OptionList;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;

public class MainMenu extends it.polimi.ingsw.client.view.abstractview.MainMenu implements CLIBuilder {


    /**
     * The main method of the view. Handles user interaction. User interaction
     * is considered ended when this method exits.
     *
     * @implNote This method shall exit as soon as possible after stopInteraction()
     * is called (from another thread).
     */
    @Override
    public void run() {
        getCLIView().setTitle(new Title("Main main title"));

        OptionList optionList = new OptionList();
        Runnable r;

        r = ()->getClient().changeViewBuilder(new CreateJoinLoadMatch(), this);
        optionList.addOption(Option.from("Browse matches","Join or create",r));

        r = ()->getClient().changeViewBuilder(new CreateMatch(), this);
        optionList.addOption(Option.from("Create a match","One or more players",r));

        r = ()->getCLIView().setSpinner(new Spinner("Waiting for exit"));
        optionList.addOption(Option.from("Edit Cards","Costs and effects",r));

        r = ()->getCLIView().setSpinner(new Spinner("Waiting for exit"));
        optionList.addOption(Option.from("Exit",":*",r));

        getCLIView().setOptionList(CLIPos.CENTER,optionList);
        getCLIView().displayWithScroll();

        getCLIView().displayWithDivider();
        getCLIView().performLastChoice();

    }

}
