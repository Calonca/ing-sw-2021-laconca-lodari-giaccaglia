package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.Spinner;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.abstractview.CreateMatchViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.CreateMatchRequest;
import it.polimi.ingsw.network.messages.clienttoserver.JoinMatchRequest;

import java.beans.PropertyChangeEvent;
import java.util.Scanner;

public class CreateMatch extends CreateMatchViewBuilder implements CLIBuilder {


    /**
     * The main method of the view. Handles user interaction. User interaction
     * is considered ended when this method exits.
     *
     * @implNote This method shall exit as soon as possible after stopInteraction()
     * is called (from another thread).
     */
    @Override
    public void run() {
        getCLIView().setTitle(new Title("Creating match"));
        getCLIView().displayWithDivider();
        int numberOfPlayers;
        String nickname;
        do {
            getCLIView().putDivider();
            String in = getCLIView().getIN("Number of people: ");
            numberOfPlayers = Integer.parseInt(in);
            nickname = getCLIView().getIN("Your nickname: ");
        }while (numberOfPlayers<0||numberOfPlayers>4);

        getClient().getServerHandler().sendCommandMessage(new CreateMatchRequest(numberOfPlayers,nickname));

        getCLIView().setSpinner(Spinner.matchToStart(getClient()));
        getCLIView().displayWithDivider();

    }

}
