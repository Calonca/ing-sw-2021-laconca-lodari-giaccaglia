package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
        Client client = ViewBuilder.getClient();
        client.setStage(stage);
        stage.setTitle("Maestri");
        stage.setResizable(false);
        stage.centerOnScreen();

        //INITIAL SCREEN BOOTSTRAP, THEN UPDATE IS LEFT TO ChangeViewBuilder
        Scene scene = new Scene(new StartingScreen().getRoot());

        client.getStage().setScene(scene);
        System.out.println(client.getStage().getScene().getRoot().getChildrenUnmodifiable());
        client.getStage().show();
        client.run();

    }
}
