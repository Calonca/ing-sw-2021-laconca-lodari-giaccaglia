package it.polimi.ingsw.client.view.cli.match;

import it.polimi.ingsw.client.view.cli.CLIBuilder;
import it.polimi.ingsw.client.view.cli.CLIelem.Title;
import it.polimi.ingsw.client.view.abstractview.CreateMatchViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.CreateMatchRequest;

import java.beans.PropertyChangeEvent;

public class CreateMatchCLI extends CreateMatchViewBuilder implements CLIBuilder {


    /**
     * After receiving input, a message regarding the number of players gets sent to the server
     */
    @Override
    public void run() {

        getCLIView().setTitle(new Title("Creating match"));
        getCLIView().runOnIntInput("Number of people (1 to 4): ","Number of people not valid",1,4,()->
        {
            int numberOfPlayers = getCLIView().getLastInt();
            getCLIView().clearScreen();
            getClient().getServerHandler().sendCommandMessage(new CreateMatchRequest(numberOfPlayers,getCommonData().getThisPlayerNickname()));
            getClient().changeViewBuilder(new LobbyViewBuilderCLI());
        });
        getCLIView().show();

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
       // empty method because there's no need to handle property changes
    }
}
