package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.CLIelem.Spinner;
import it.polimi.ingsw.client.view.abstractview.ConnectToServerViewBuilder;

public class ConnectToServer extends ConnectToServerViewBuilder implements CLIBuilder {

    @Override
    public void run() {
        getCLIView().runOnInput("Write the server ip",()->{
            String portString = getCLIView().getLastInput();
            getCLIView().runOnIntInput("Port number: ","Insert a port",0,65535,()->{

                getClient().setServerConnection(portString, getCLIView().getLastInt());
                getClient().changeViewBuilder(new CreateJoinLoadMatch(), this);
                getClient().run();
            });
            getCLIView().displayWithDivider();
        });
        getCLIView().displayWithDivider();
    }

}
