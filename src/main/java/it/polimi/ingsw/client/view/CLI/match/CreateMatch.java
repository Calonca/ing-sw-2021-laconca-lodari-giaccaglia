package it.polimi.ingsw.client.view.CLI.match;

import it.polimi.ingsw.client.view.CLI.CLIBuilder;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.WaitingForMatchToStart;
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
            getCLIView().clearScreen();
            getCLIView().setBody(WaitingForMatchToStart.test(getClient()));
            getClient().getServerHandler().sendCommandMessage(new CreateMatchRequest(numberOfPlayers,getCommonData().getCurrentNick()));
            getCLIView().show();
        });
        getCLIView().show();

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
       // System.out.println(evt.getPropertyName());
    }
}
