package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.Spinner;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.network.messages.clienttoserver.JoinMatchRequest;

import java.beans.PropertyChangeEvent;
import java.util.Scanner;
import java.util.UUID;

public class JoinMatch extends it.polimi.ingsw.client.view.abstractview.JoinMatch implements CLIBuilder {


    public JoinMatch(UUID matchId) {
        super(matchId);
    }

    /**
     * The main method of the view. Handles user interaction. User interaction
     * is considered ended when this method exits.
     *
     * @implNote This method shall exit as soon as possible after stopInteraction()
     * is called (from another thread).
     */
    @Override
    public void run() {
        getCLIView().setTitle(new Title("Joining match"));
        getCLIView().displayWithDivider();

        String nickname = getCLIView().getIN("Your nickname: ");

        getClient().getServerHandler().sendCommandMessage(new JoinMatchRequest(matchId,nickname));

        getCLIView().setSpinner(Spinner.matchToStart(getClient(),this));
        getCLIView().displayWithDivider();

    }

}
