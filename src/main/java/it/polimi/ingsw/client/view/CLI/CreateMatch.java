package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.Spinner;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.abstractview.CreateMatchViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.CreateMatchRequest;

import java.beans.PropertyChangeEvent;

public class CreateMatch extends CreateMatchViewBuilder implements CLIBuilder {


    @Override
    public void run() {
        getCLIView().setTitle(new Title("Creating match"));

        getCLIView().runOnIntInput("Number of people (1 to 4): ","Number of people not valid",1,4,()->
        {
            int numberOfPlayers = getCLIView().getLastInt();
            getCLIView().resetCLI();
            getCLIView().setSpinner(Spinner.matchToStart(getClient(),this));
            getClient().getServerHandler().sendCommandMessage(new CreateMatchRequest(numberOfPlayers,getCommonData().getCurrentnick()));
            getCLIView().refreshCLI();
        });
        getCLIView().refreshCLI();

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
