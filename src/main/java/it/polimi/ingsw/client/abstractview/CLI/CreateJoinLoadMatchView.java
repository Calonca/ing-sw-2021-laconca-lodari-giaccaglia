package it.polimi.ingsw.client.abstractview.CLI;

import it.polimi.ingsw.client.abstractview.View;
import it.polimi.ingsw.network.messages.clienttoserver.CreateMatchRequest;

import java.util.Scanner;

public class CreateJoinLoadMatchView extends View implements CLIView {


    /**
     * The main method of the view. Handles user interaction. User interaction
     * is considered ended when this method exits.
     *
     * @implNote This method shall exit as soon as possible after stopInteraction()
     * is called (from another thread).
     */
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        int numberOfPlayers;
        String nickname;
        do {
            CLIView.cleanConsole();
            System.out.println("Creating match\nNumber of people: ");
            numberOfPlayers = Integer.parseInt(scanner.nextLine());
            System.out.println("Your nickname: ");
            nickname = scanner.nextLine();
        }while (numberOfPlayers<0||numberOfPlayers>4);


        getClient().getServerHandler().sendCommandMessage(new CreateMatchRequest(numberOfPlayers,nickname));
    }
}
