package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.CLI.CreateJoinLoadMatch;
import it.polimi.ingsw.client.view.abstractview.ConnectToServerViewBuilder;
import it.polimi.ingsw.network.Maestri;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Arrays;

/**
 * The GUIStarter will call this method. The first stage will be replaced each view transition, and all the buttons and containers
 * already defined in the FXML files will be given a function accordingly. Each time the stage is replaced, the client will be
 * referenced through static method.
 */
public class GUI extends Application {
    static String[] arguments;
    public static void main(String[] args) {
        arguments = args;
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Client client = Client.getInstance();
        client.setStage(stage);
        stage.setResizable(false);
        stage.setHeight(700);
        stage.setWidth(1000);
        stage.setTitle("Maestri");
        stage.centerOnScreen();
        stage.show();
        client.run();
    }
}
