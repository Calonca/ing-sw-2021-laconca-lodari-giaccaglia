package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.CLIelem.Spinner;
import it.polimi.ingsw.client.view.abstractview.ConnectToServerViewBuilder;

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
        ip = getCLIView().getInAndCallRunnable("Write the server number");
        String portString = getCLIView().getInAndCallRunnable("Write the server port");
        port = Integer.parseInt(portString);

        getClient().setServerConnection(ip,port);
        Spinner spinner = new Spinner("server connection");
        spinner.setPerformer(()->getClient().changeViewBuilder(new MainMenu(), this));
        spinner.performWhenReceiving(CommonData.matchesDataString);

        getCLIView().setSpinner(spinner);
        getClient().run();
    }

}
