package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.layout.GridElem;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.SizedBox;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.abstractview.ConnectToServerViewBuilder;

import java.beans.PropertyChangeEvent;

public class ConnectToServer extends ConnectToServerViewBuilder implements CLIBuilder {

    /**
     * This method acquires the initial data to start the server connection
     */
    @Override
    public void run() {
        getCLIView().setBody(buildBody());
        getCLIView().runOnInput("Write the server ip", () -> {
            String portString = getCLIView().getLastInput();
            getCLIView().runOnIntInput("Port number: ", "Insert a port", 0, 65535, () -> {
                getCLIView().runOnInput("Write your nickname", () -> {
                    String nickname = getCLIView().getLastInput();
                    getCommonData().setCurrentNick(nickname);
                    getClient().run();
                });
                getClient().setServerConnection(portString, getCLIView().getLastInt());
                getCLIView().show();
            });
            getCLIView().show();
        });
        getCLIView().show();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public CanvasBody buildBody() {
        Column c = new Column();
        c.setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);
        c.addElem(Option.noNumber(title1));
        c.addElem(new SizedBox(0,2));
        c.addElem(Option.noNumber(title1line2));
        return CanvasBody.fromGrid(c);
    }

    final String title1 = """
                    ███╗   ███╗ █████╗ ███████╗███████╗████████╗██████╗ ██╗    ██████╗ ███████╗██╗
                    ████╗ ████║██╔══██╗██╔════╝██╔════╝╚══██╔══╝██╔══██╗██║    ██╔══██╗██╔════╝██║
                    ██╔████╔██║███████║█████╗  ███████╗   ██║   ██████╔╝██║    ██║  ██║█████╗  ██║
                    ██║╚██╔╝██║██╔══██║██╔══╝  ╚════██║   ██║   ██╔══██╗██║    ██║  ██║██╔══╝  ██║
                    ██║ ╚═╝ ██║██║  ██║███████╗███████║   ██║   ██║  ██║██║    ██████╔╝███████╗███████╗
                    ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝    ╚═════╝ ╚══════╝╚══════╝
                    
                    """;
    final String title1line2 = """                
                    ██████╗ ██╗███╗   ██╗ █████╗ ███████╗ ██████╗██╗███╗   ███╗███████╗███╗   ██╗████████╗ ██████╗
                    ██╔══██╗██║████╗  ██║██╔══██╗██╔════╝██╔════╝██║████╗ ████║██╔════╝████╗  ██║╚══██╔══╝██╔═══██╗
                    ██████╔╝██║██╔██╗ ██║███████║███████╗██║     ██║██╔████╔██║█████╗  ██╔██╗ ██║   ██║   ██║   ██║
                    ██╔══██╗██║██║╚██╗██║██╔══██║╚════██║██║     ██║██║╚██╔╝██║██╔══╝  ██║╚██╗██║   ██║   ██║   ██║
                    ██║  ██║██║██║ ╚████║██║  ██║███████║╚██████╗██║██║ ╚═╝ ██║███████╗██║ ╚████║   ██║   ╚██████╔╝
                    ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚══════╝ ╚═════╝╚═╝╚═╝     ╚═╝╚══════╝╚═╝  ╚═══╝   ╚═╝    ╚═════╝""";

}