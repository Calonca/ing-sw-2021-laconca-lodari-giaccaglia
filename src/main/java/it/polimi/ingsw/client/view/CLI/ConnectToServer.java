package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.textUtil.Canvas;
import it.polimi.ingsw.client.view.CLI.textUtil.Drawable;
import it.polimi.ingsw.client.view.abstractview.ConnectToServerViewBuilder;

import java.beans.PropertyChangeEvent;

public class ConnectToServer extends ConnectToServerViewBuilder implements CLIBuilder {

    @Override
    public void run() {
        getCLIView().setBody(new CanvasBody(buildCanvas()));
        getCLIView().runOnInput("Write the server ip", () -> {
            String portString = getCLIView().getLastInput();
            getCLIView().runOnIntInput("Port number: ", "Insert a port", 0, 65535, () -> {
                getClient().setServerConnection(portString, getCLIView().getLastInt());
                getCLIView().runOnInput("Write your nickname", () -> {
                    String nickname = getCLIView().getLastInput();
                    getCommonData().setCurrentnick(nickname);
                    getClient().run();
                });
                getCLIView().show();
            });
            getCLIView().show();
        });
        getCLIView().show();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public Canvas buildCanvas() {
        Canvas canvas = Canvas.withBorder(CLI.width, getCLIView().getMaxBodyHeight());
        Drawable dwl = new Drawable();
        dwl.addToCenter(CLI.width,title1);
        dwl.addEmptyLine();
        dwl.addToCenter(CLI.width,title1line2);
        canvas.addDrawableList(dwl);
        return canvas;
    }

    String title1 = """
                    ███╗   ███╗ █████╗ ███████╗███████╗████████╗██████╗ ██╗    ██████╗ ███████╗██╗
                    ████╗ ████║██╔══██╗██╔════╝██╔════╝╚══██╔══╝██╔══██╗██║    ██╔══██╗██╔════╝██║
                    ██╔████╔██║███████║█████╗  ███████╗   ██║   ██████╔╝██║    ██║  ██║█████╗  ██║
                    ██║╚██╔╝██║██╔══██║██╔══╝  ╚════██║   ██║   ██╔══██╗██║    ██║  ██║██╔══╝  ██║
                    ██║ ╚═╝ ██║██║  ██║███████╗███████║   ██║   ██║  ██║██║    ██████╔╝███████╗███████╗
                    ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝    ╚═════╝ ╚══════╝╚══════╝
                    
                    """;
    String title1line2 = """                
                    ██████╗ ██╗███╗   ██╗ █████╗ ███████╗ ██████╗██╗███╗   ███╗███████╗███╗   ██╗████████╗ ██████╗
                    ██╔══██╗██║████╗  ██║██╔══██╗██╔════╝██╔════╝██║████╗ ████║██╔════╝████╗  ██║╚══██╔══╝██╔═══██╗
                    ██████╔╝██║██╔██╗ ██║███████║███████╗██║     ██║██╔████╔██║█████╗  ██╔██╗ ██║   ██║   ██║   ██║
                    ██╔══██╗██║██║╚██╗██║██╔══██║╚════██║██║     ██║██║╚██╔╝██║██╔══╝  ██║╚██╗██║   ██║   ██║   ██║
                    ██║  ██║██║██║ ╚████║██║  ██║███████║╚██████╗██║██║ ╚═╝ ██║███████╗██║ ╚████║   ██║   ╚██████╔╝
                    ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚══════╝ ╚═════╝╚═╝╚═╝     ╚═╝╚══════╝╚═╝  ╚═══╝   ╚═╝    ╚═════╝""";

}