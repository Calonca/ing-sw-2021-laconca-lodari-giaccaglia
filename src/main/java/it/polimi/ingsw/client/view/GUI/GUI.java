package it.polimi.ingsw.client.view.GUI;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Scanner;

public class GUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Label label = new Label("Hello");
        AnchorPane anchorPane = new AnchorPane(label);
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.setTitle("TEst");
        stage.show();
    }
}
