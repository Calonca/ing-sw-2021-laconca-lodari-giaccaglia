package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.abstractview.DisconnectViewBuilder;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.SubScene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This controller is used to communicate the player that he got disconnected from the server.
 */
public class DisconnectGUI extends DisconnectViewBuilder implements GUIView {

    AnchorPane winLoosePane;

    Text countDown;

    /**
     * Upon disconnection, informations get displayed
     */
    @Override
    public void run() {
        SubScene root=getRoot();

        root.translateYProperty().set(GUI.getRealPane().getHeight());
        Timeline timeline=new Timeline();
        KeyValue kv= new KeyValue(root.translateYProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf= new KeyFrame(Duration.seconds(0.5),kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        GUI.addLast(root);
    }


    /**
     * Method called only once, during END_PHASE
     * @return the ending SubScene
     */
    public SubScene getRoot() {


        double width=GUI.GUIwidth;
        double len=GUI.GUIlen;

        winLoosePane=new AnchorPane();
        winLoosePane.setMinSize(GUI.GUIwidth,GUI.GUIlen);



        countDown=new Text(DISCONNECTED_STRING + SECONDS);
        countDown.setLayoutX(width/2);
        countDown.setLayoutY(len/2);
        winLoosePane.getChildren().add(countDown);

        startWaitingThread();

        return new SubScene(winLoosePane,GUI.GUIwidth,GUI.GUIlen);


    }


    @Override
    public void updateCountDown(int remaining) {
        Platform.runLater(()->countDown.setText(DISCONNECTED_STRING +  remaining));
    }

    @Override
    public void exit() {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
