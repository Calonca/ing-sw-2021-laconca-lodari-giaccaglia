package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.CLI.CreateJoinLoadMatch;
import javafx.application.Application;
import javafx.stage.Stage;

public class GUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Client client = new Client();
        client.setGUI();
        client.setStage(stage);
        stage.show();
        client.changeViewBuilder(new ConnectToServer(), null);
    }
}
