package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.network.simplemodel.EndGameInfo;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import it.polimi.ingsw.network.simplemodel.SimplePlayerInfo;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.SubScene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.beans.PropertyChangeEvent;
import java.util.Map;

public class DisconnectGUI extends LobbyViewBuilderGUI {
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



        countDown=new Text("DISCONNECTED, THE SERVER WILL CLOSE IN:");
        countDown.setLayoutX(width/2);
        countDown.setLayoutY(len/2);
        winLoosePane.getChildren().add(countDown);

        waitingThread(5);

        return new SubScene(winLoosePane,GUI.GUIwidth,GUI.GUIlen);


    }



    public void waitingThread(int remainingSeconds) {

        Thread thread=new Thread(()->
        {
            try {
                for(int i=remainingSeconds;i>0;i--)
                {
                    System.out.println(i);
                    Thread.sleep(1000);
                    int finalI = i-1;
                    Platform.runLater(()->countDown.setText("DISCONNECTED, THE SERVER WILL CLOSE IN:" +  finalI));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.exit();
        });

        thread.start();
    }
}
