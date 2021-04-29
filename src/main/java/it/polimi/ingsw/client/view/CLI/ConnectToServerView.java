package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.abstractview.View;

import java.beans.PropertyChangeEvent;
import java.util.Scanner;

public class ConnectToServerView extends it.polimi.ingsw.client.view.abstractview.ConnectToServerView implements CLIView {


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
        CLIBuilder.scroll();
        System.out.println("Write the server number");
        ip = scanner.nextLine();
        System.out.println("Write the server port");
        port = Integer.parseInt(scanner.nextLine());

        getClient().setServerConnection(ip,port);
        getClient().transitionToView(new WaitForServerConnection());
        getClient().run();
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
