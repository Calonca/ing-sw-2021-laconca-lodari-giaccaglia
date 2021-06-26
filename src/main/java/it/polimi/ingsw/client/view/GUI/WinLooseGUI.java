package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.abstractview.WinLooseBuilder;
import it.polimi.ingsw.network.simplemodel.EndGameInfo;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WinLooseGUI extends WinLooseBuilder implements GUIView {


    AnchorPane winLoosePane;

    double width=600;
    double len=500;
    @Override
    public void run() {
        SubScene root=getRoot();

        root.translateYProperty().set(GUI.getRealPane().getHeight());
        Timeline timeline=new Timeline();
        KeyValue kv= new KeyValue(root.translateYProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf= new KeyFrame(Duration.seconds(0.5),kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        GUI.getRealPane().getChildren().remove(0);
        GUI.getRealPane().getChildren().add(root);

        System.out.println(GUI.getRealPane().getChildren());
    }

    public SubScene getRoot() {


        winLoosePane=new AnchorPane();
        winLoosePane.setMinSize(1800,1000);


        return new SubScene(winLoosePane,GUI.GUIwidth,GUI.GUIlen);


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(PlayersInfo.SimplePlayerInfo.class.getSimpleName()))
        {
        int numberOfWinners=getEndGameInfo().getPlayersEndingTheGame().size();
        Text text;
        int cd;
        for(int i=0;i<numberOfWinners;i++)
        {

            winLoosePane.getChildren().clear();
                //todo make text look better
                text=new Text(getEndGameInfo().getPlayerInfo(getEndGameInfo().getPlayersEndingTheGame().get(i)).getPlayerNickname());
                text.setLayoutY(len/3);
                text.setLayoutX((i+1)*width/(numberOfWinners+1));
                winLoosePane.getChildren().add(text);

                cd=getEndGameInfo().getPlayerInfo(getEndGameInfo().getPlayersEndingTheGame().get(i)).getVictoryPoints();
                text=new Text(Integer.toString(cd));
                winLoosePane.getChildren().add(text);

                cd=getEndGameInfo().getPlayerInfo(getEndGameInfo().getPlayersEndingTheGame().get(i)).getVictoryPoints();
                text=new Text(Integer.toString(cd));
                winLoosePane.getChildren().add(text);



        }
    }
    }
}

/*ObservableList<Node> workingCollection = FXCollections.observableArrayList(((Pane)getClient().getStage().getScene().getRoot()).getChildren());
            Collections.swap(workingCollection, 0, 1);
            ((Pane)getClient().getStage().getScene().getRoot()).getChildren().setAll(workingCollection);

 */
