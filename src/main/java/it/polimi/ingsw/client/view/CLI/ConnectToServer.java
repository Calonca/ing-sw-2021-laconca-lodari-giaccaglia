package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.textUtil.Canvas;
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
                getCLIView().refreshCLI();
            });
            getCLIView().refreshCLI();
        });
        getCLIView().refreshCLI();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public Canvas buildCanvas() {
        Canvas canvas = Canvas.withBorder(CLI.width, getCLIView().getMaxBodyHeight());
        canvas.drawCenterXDefault(0, title1);
        canvas.drawCenterXDefault(7, title1line2);
        canvas.drawCenterXDefault(20, title2);
        canvas.drawCenterXDefault(30, title2Line2);
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

    String title2 = """   
             ███▄ ▄███▓ ▄▄▄      ▓█████   ██████ ▄▄▄█████▓ ██▀███   ██▓   ▓█████▄ ▓█████  ██▓    
            ▓██▒▀█▀ ██▒▒████▄    ▓█   ▀ ▒██    ▒ ▓  ██▒ ▓▒▓██ ▒ ██▒▓██▒   ▒██▀ ██▌▓█   ▀ ▓██▒    
            ▓██    ▓██░▒██  ▀█▄  ▒███   ░ ▓██▄   ▒ ▓██░ ▒░▓██ ░▄█ ▒▒██▒   ░██   █▌▒███   ▒██░    
            ▒██    ▒██ ░██▄▄▄▄██ ▒▓█  ▄   ▒   ██▒░ ▓██▓ ░ ▒██▀▀█▄  ░██░   ░▓█▄   ▌▒▓█  ▄ ▒██░    
            ▒██▒   ░██▒ ▓█   ▓██▒░▒████▒▒██████▒▒  ▒██▒ ░ ░██▓ ▒██▒░██░   ░▒████▓ ░▒████▒░██████▒
            ░ ▒░   ░  ░ ▒▒   ▓▒█░░░ ▒░ ░▒ ▒▓▒ ▒ ░  ▒ ░░   ░ ▒▓ ░▒▓░░▓      ▒▒▓  ▒ ░░ ▒░ ░░ ▒░▓  ░
            ░  ░      ░  ▒   ▒▒ ░ ░ ░  ░░ ░▒  ░ ░    ░      ░▒ ░ ▒░ ▒ ░    ░ ▒  ▒  ░ ░  ░░ ░ ▒  ░
            ░      ░     ░   ▒      ░   ░  ░  ░    ░        ░░   ░  ▒ ░    ░ ░  ░    ░     ░ ░   
                   ░         ░  ░   ░  ░      ░              ░      ░        ░       ░  ░    ░  ░
                                                                                                 """;
    String title2Line2 = """        
             ██▀███   ██▓ ███▄    █  ▄▄▄        ██████  ▄████▄   ██▓ ███▄ ▄███▓▓█████  ███▄    █ ▄▄▄█████▓ ▒█████     
            ▓██ ▒ ██▒▓██▒ ██ ▀█   █ ▒████▄    ▒██    ▒ ▒██▀ ▀█  ▓██▒▓██▒▀█▀ ██▒▓█   ▀  ██ ▀█   █ ▓  ██▒ ▓▒▒██▒  ██▒   
            ▓██ ░▄█ ▒▒██▒▓██  ▀█ ██▒▒██  ▀█▄  ░ ▓██▄   ▒▓█    ▄ ▒██▒▓██    ▓██░▒███   ▓██  ▀█ ██▒▒ ▓██░ ▒░▒██░  ██▒   
            ▒██▀▀█▄  ░██░▓██▒  ▐▌██▒░██▄▄▄▄██   ▒   ██▒▒▓▓▄ ▄██▒░██░▒██    ▒██ ▒▓█  ▄ ▓██▒  ▐▌██▒░ ▓██▓ ░ ▒██   ██░   
            ░██▓ ▒██▒░██░▒██░   ▓██░ ▓█   ▓██▒▒██████▒▒▒ ▓███▀ ░░██░▒██▒   ░██▒░▒████▒▒██░   ▓██░  ▒██▒ ░ ░ ████▓▒░   
            ░ ▒▓ ░▒▓░░▓  ░ ▒░   ▒ ▒  ▒▒   ▓▒█░▒ ▒▓▒ ▒ ░░ ░▒ ▒  ░░▓  ░ ▒░   ░  ░░░ ▒░ ░░ ▒░   ▒ ▒   ▒ ░░   ░ ▒░▒░▒░    
              ░▒ ░ ▒░ ▒ ░░ ░░   ░ ▒░  ▒   ▒▒ ░░ ░▒  ░ ░  ░  ▒    ▒ ░░  ░      ░ ░ ░  ░░ ░░   ░ ▒░    ░      ░ ▒ ▒░    
              ░░   ░  ▒ ░   ░   ░ ░   ░   ▒   ░  ░  ░  ░         ▒ ░░      ░      ░      ░   ░ ░   ░      ░ ░ ░ ▒     
               ░      ░           ░       ░  ░      ░  ░ ░       ░         ░      ░  ░         ░              ░ ░  """;
}