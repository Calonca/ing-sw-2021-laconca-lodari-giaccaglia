package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.CLIelem.RunnableWithString;
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

        getCLIView().scroll();
        RunnableWithString rs = new RunnableWithString();
        rs.afterInputCall(()->{
            String portString = rs.getString();
            RunnableWithString rs2 = new RunnableWithString();
            rs2.afterInputCall(()->{
                int port = Integer.parseInt(rs2.getString());

                getClient().setServerConnection(portString,port);
                Spinner spinner = new Spinner("server connection");
                spinner.setPerformer(()->getClient().changeViewBuilder(new MainMenu(), this));
                spinner.performWhenReceiving(CommonData.matchesDataString);

                getCLIView().setSpinner(spinner);
                getClient().run();
            });
            getCLIView().getInputAndLaunchRunnable("Your nickname: ",rs2);

        });
        getCLIView().getInputAndLaunchRunnable("Write the server number",rs);

    }

}
