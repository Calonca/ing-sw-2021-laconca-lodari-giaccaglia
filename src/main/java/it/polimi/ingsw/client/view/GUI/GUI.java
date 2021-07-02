package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.text.View;

/**
 * The GUIStarter will call this method. The first stage will be replaced each view transition, and all the buttons and containers
 * already defined in the FXML files will be given a function accordingly. Each time the stage is replaced, the client will be
 * referenced through static method.
 */
public class GUI extends Application {

    public static double GUIwidth=1800;
    public static double GUIlen=1000;
    public static Node appendedScene;

    static String[] arguments;
    public static void main(String[] args) {
        arguments = args;
        launch(args);
    }

    /**
     * Method to BootsTrap the graphics
     * @param stage is provided by the application
     * @throws Exception if incorrect basic configuration
     */
    @Override
    public void start(Stage stage) throws Exception {
        Client client = ViewBuilder.getClient();
        client.setStage(stage);
        stage.setTitle("Maestri");
        stage.setResizable(false);
        stage.centerOnScreen();

        //INITIAL SCREEN BOOTSTRAP, THEN UPDATE IS LEFT TO ChangeViewBuilder
        Scene scene = new Scene(getRealPane());

        new StartingScreen().run();

        client.getStage().setScene(scene);
        client.getStage().getScene().getStylesheets().add("assets/application.css");
        client.getStage().show();
        client.run();

    }

    @Override
    public void stop() throws Exception {
        super.stop();

        final Client client = ViewBuilder.getClient();
        if (client !=null)
            client.terminate();
    }

    private static StackPane realPane = null;

    public static StackPane getRealPane()
    {
        if (realPane == null)
        {
            realPane = new StackPane();
            realPane.setPrefHeight(GUIlen);
            realPane.setPrefWidth(GUIwidth);

        }

        return realPane;
    }

    public static void removeLast()
    {
        Runnable run = () -> realPane.getChildren().remove(appendedScene);
        Platform.runLater(run);
    }

    public static void addLast(Node scene)
    {
        if(realPane.getChildren().size()<3)
        {
            appendedScene=scene;
            realPane.getChildren().add(appendedScene);
        }

    }


}
