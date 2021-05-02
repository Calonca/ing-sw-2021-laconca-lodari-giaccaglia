package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.CLIelem.Spinner;
import it.polimi.ingsw.client.view.abstractview.ConnectToServerViewBuilder;

import java.beans.PropertyChangeEvent;
import java.util.Scanner;

public class ConnectToServer extends ConnectToServerViewBuilder implements CLIBuilder {


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

        getCLIView().scroll();
        ip = getCLIView().getIN("Write the server number");
        String portString = getCLIView().getIN("Write the server port");
        port = Integer.parseInt(portString);

        getClient().setServerConnection(ip,port);
        Spinner spinner = new Spinner("server connection");
        spinner.setPerformer(()->getClient().transitionToView(new MainMenu()));
        spinner.performWhenReceiving(CommonData.matchesDataString);

        getCLIView().setSpinner(spinner);
        getClient().run();
    }

}
