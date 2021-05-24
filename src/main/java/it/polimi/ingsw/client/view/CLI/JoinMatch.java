package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.WaitingForMatchToStart;
import it.polimi.ingsw.network.messages.clienttoserver.JoinMatchRequest;

import java.beans.PropertyChangeEvent;

public class JoinMatch extends it.polimi.ingsw.client.view.abstractview.JoinMatch implements CLIBuilder {

    @Override
    public void run() {
        getCLIView().setTitle(new Title("Joining match"));
        getClient().getServerHandler().sendCommandMessage(new JoinMatchRequest(matchId,getCommonData().getCurrentnick()));
        getCLIView().resetCLI();
        getCLIView().setBody(WaitingForMatchToStart.test(getClient()));
        getCLIView().refreshCLI();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
