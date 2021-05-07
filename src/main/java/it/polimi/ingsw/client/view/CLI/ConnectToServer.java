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
                Spinner spinner = new Spinner("server connection");
                spinner.setPerformer(()->getClient().changeViewBuilder(new CreateJoinLoadMatch(), this));
                spinner.performWhenReceiving(CommonData.matchesDataString);

                getCLIView().setSpinner(spinner);
                getClient().run();
            });
            getCLIView().displayWithDivider();
        });
        getCLIView().displayWithDivider();
    }

}
