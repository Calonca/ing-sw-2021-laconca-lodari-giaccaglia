package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.Spinner;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.network.messages.clienttoserver.JoinMatchRequest;

import java.beans.PropertyChangeEvent;
import java.util.UUID;

public class JoinMatch extends it.polimi.ingsw.client.view.abstractview.JoinMatch implements CLIBuilder {

    @Override
    public void run() {
        getCLIView().setTitle(new Title("Joining match"));

        getCLIView().runOnInput("Your nickname: ",()->
        {
            getClient().getServerHandler().sendCommandMessage(new JoinMatchRequest(matchId,getCLIView().getLastInput()));
            getCLIView().resetCLI();
            getCLIView().setSpinner(Spinner.matchToStart(getClient(),this));
            getCLIView().displayWithDivider();
        });
        getCLIView().displayWithDivider();


    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
