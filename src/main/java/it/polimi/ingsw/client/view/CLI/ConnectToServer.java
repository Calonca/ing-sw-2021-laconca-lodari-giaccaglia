package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.CLIelem.Spinner;
import it.polimi.ingsw.client.view.abstractview.ConnectToServerViewBuilder;

public class ConnectToServer extends ConnectToServerViewBuilder implements CLIBuilder {

    @Override
    public void run() {

        getCLIView().scroll();
        Runnable r1 = ()->{
            String portString = getCLIView().getLastInput();
            Runnable r2 = ()->{
                int port = Integer.parseInt(getCLIView().getLastInput());

                getClient().setServerConnection(portString,port);
                Spinner spinner = new Spinner("server connection");
                spinner.setPerformer(()->getClient().changeViewBuilder(new MainMenu(), this));
                spinner.performWhenReceiving(CommonData.matchesDataString);

                getCLIView().setSpinner(spinner);
                getClient().run();
            };
            getCLIView().runOnInput("Your nickname: ",r2);

        };
        getCLIView().runOnInput("Write the server number",r1);

    }

}
