package it.polimi.ingsw.client.abstractview.CLI;

import it.polimi.ingsw.client.abstractview.View;

import java.util.Scanner;

public class ConnectToServerView extends View implements CLIView {


    /**
     * The main method of the view. Handles user interaction. User interaction
     * is considered ended when this method exits.
     *
     * @implNote This method shall exit as soon as possible after stopInteraction()
     * is called (from another thread).
     */
    @Override
    public void run() {
        String ip;
        int port;

        Scanner scanner = new Scanner(System.in);
        CLIView.cleanConsole();
        System.out.println("Write the server number");
        ip = scanner.nextLine();
        System.out.println("Write the server port");
        port = Integer.parseInt(scanner.nextLine());

        getClient().setServerConnection(ip,port);
        getClient().transitionToView(new WaitForServerConnection());
        getClient().run();
    }
}
