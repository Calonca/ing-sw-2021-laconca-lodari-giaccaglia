package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.abstractview.ViewBuilder;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ConnectToServer extends ViewBuilder implements GUIView {
    public StackPane connectionPane;
    public Button connectionButton;
    public TextField addressText;
    public TextField portText;

    @Override
    public void run() {
        Stage stage = getClient().getStage();
        //Load scene and add things to stage
        Label label = new Label("Hello");
        AnchorPane anchorPane = new AnchorPane(label);
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.setTitle("TEst");
    }

    //Add buttons here that call client.changeViewBuilder(new *****, this);
}
