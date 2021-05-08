package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.Client;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The GUIStarter will call this method. The first stage will be replaced each view transition, and all the buttons and containers
 * already defined in the FXML files will be given a function accordingly. Each time the stage is replaced, the client will be
 * referenced through static method.
 */
public class GUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Client client = Client.getInstance();
        client.setGUI();
        client.setStage(stage);
        stage.show();
        client.changeViewBuilder(new ConnectToServer(), null);
    }
}
