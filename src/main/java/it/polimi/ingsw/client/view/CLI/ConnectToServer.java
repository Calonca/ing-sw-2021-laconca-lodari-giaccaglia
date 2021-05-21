package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.abstractview.ConnectToServerViewBuilder;

import java.beans.PropertyChangeEvent;

public class ConnectToServer extends ConnectToServerViewBuilder implements CLIBuilder {

    @Override
    public void run() {
        getCLIView().runOnInput("Write the server ip",()->{
            String portString = getCLIView().getLastInput();
            getCLIView().runOnIntInput("Port number: ","Insert a port",0,65535,()->{

                getClient().setServerConnection(portString, getCLIView().getLastInt());
                getClient().changeViewBuilder(new CreateJoinLoadMatch());
                getClient().run();
            });
            getCLIView().refreshCLI();
        });
        getCLIView().refreshCLI();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
