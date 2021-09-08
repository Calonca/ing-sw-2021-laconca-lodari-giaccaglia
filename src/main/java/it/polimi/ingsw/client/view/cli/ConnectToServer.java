package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.cli.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.cli.layout.GridElem;
import it.polimi.ingsw.client.view.cli.layout.Option;
import it.polimi.ingsw.client.view.cli.layout.SizedBox;
import it.polimi.ingsw.client.view.cli.layout.recursivelist.Column;
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
                    getCommonData().setThisPlayerNickname(nickname);
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
            //empty method because there's no need to check for event changes
    }

    public CanvasBody buildBody() {
        Column c = new Column();
        c.setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);
        c.addElem(Option.noNumber(TITLE_1_LINE_1));
        c.addElem(new SizedBox(0,2));
        c.addElem(Option.noNumber(TITLE_1_LINE_2));
        return CanvasBody.fromGrid(c);
    }

    private static final String TITLE_1_LINE_1 = """ 
                    ███╗   ███╗ █████╗ ███████╗███████╗████████╗██████╗ ██╗    ██████╗ ███████╗██╗
                    ████╗ ████║██╔══██╗██╔════╝██╔════╝╚══██╔══╝██╔══██╗██║    ██╔══██╗██╔════╝██║
                    ██╔████╔██║███████║█████╗  ███████╗   ██║   ██████╔╝██║    ██║  ██║█████╗  ██║
                    ██║╚██╔╝██║██╔══██║██╔══╝  ╚════██║   ██║   ██╔══██╗██║    ██║  ██║██╔══╝  ██║
                    ██║ ╚═╝ ██║██║  ██║███████╗███████║   ██║   ██║  ██║██║    ██████╔╝███████╗███████╗
                    ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝    ╚═════╝ ╚══════╝╚══════╝
                    
                    """;
    private static final String TITLE_1_LINE_2 = """                
                    ██████╗ ██╗███╗   ██╗ █████╗ ███████╗ ██████╗██╗███╗   ███╗███████╗███╗   ██╗████████╗ ██████╗
                    ██╔══██╗██║████╗  ██║██╔══██╗██╔════╝██╔════╝██║████╗ ████║██╔════╝████╗  ██║╚══██╔══╝██╔═══██╗
                    ██████╔╝██║██╔██╗ ██║███████║███████╗██║     ██║██╔████╔██║█████╗  ██╔██╗ ██║   ██║   ██║   ██║
                    ██╔══██╗██║██║╚██╗██║██╔══██║╚════██║██║     ██║██║╚██╔╝██║██╔══╝  ██║╚██╗██║   ██║   ██║   ██║
                    ██║  ██║██║██║ ╚████║██║  ██║███████║╚██████╗██║██║ ╚═╝ ██║███████╗██║ ╚████║   ██║   ╚██████╔╝
                    ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚══════╝ ╚═════╝╚═╝╚═╝     ╚═╝╚══════╝╚═╝  ╚═══╝   ╚═╝    ╚═════╝""";

}