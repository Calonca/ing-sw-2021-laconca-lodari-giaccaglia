package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.Spinner;
import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.CLI.CLIelem.OptionList;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.VerticalListBody;

public class MainMenu extends it.polimi.ingsw.client.view.abstractview.MainMenu implements CLIBuilder {


    @Override
    public void run() {
        getCLIView().setTitle(new Title("Main main title"));

        VerticalListBody verticalListBody = new VerticalListBody();
        Runnable r;

        r = ()->getClient().changeViewBuilder(new CreateJoinLoadMatch(), this);
        verticalListBody.addOption(Option.from("Browse matches","Join or create",r));

        r = ()->getClient().changeViewBuilder(new CreateMatch(), this);
        verticalListBody.addOption(Option.from("Create a match","One or more players",r));

        r = ()->getCLIView().setSpinner(new Spinner("Waiting for exit"));
        verticalListBody.addOption(Option.from("Edit Cards","Costs and effects",r));

        r = ()->getCLIView().setSpinner(new Spinner("Waiting for exit"));
        verticalListBody.addOption(Option.from("Exit",":*",r));

        getCLIView().setBody(verticalListBody);
        getCLIView().displayWithScroll();

        getCLIView().displayWithDivider();
        getCLIView().performLastChoice();

    }

}
