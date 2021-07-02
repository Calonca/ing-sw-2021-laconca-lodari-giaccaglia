package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.abstractview.WinLooseBuilder;
import it.polimi.ingsw.network.simplemodel.EndGameInfo;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import it.polimi.ingsw.network.simplemodel.SimplePlayerInfo;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.SubScene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.beans.PropertyChangeEvent;
import java.util.Map;

public class WinLooseGUI extends WinLooseBuilder {


    AnchorPane winLoosePane;


    /**
     * Upon game end, the final information get displayed
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

        GUI.getRealPane().getChildren().remove(0);
        GUI.getRealPane().getChildren().add(root);

        System.out.println(GUI.getRealPane().getChildren());
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

        PlayersInfo simplePlayersInfo = getSimpleModel().getElem(PlayersInfo.class).orElseThrow();
        Map<Integer, SimplePlayerInfo> simplePlayerInfoMap = simplePlayersInfo.getSimplePlayerInfoMap();

        int players=simplePlayersInfo.getSimplePlayerInfoMap().size();
        Text text;
        text=new Text("OUTCOME:  " +  getThisPlayerCache().getCurrentState());
        text.setLayoutX(900);
        text.setLayoutY(150);
        winLoosePane.getChildren().add(text);

        double startingX=GUI.GUIwidth/(players+1)-100;
        for(int i=0;i<players;i++)
        {



            //todo make text look better

            AnchorPane anchor=new AnchorPane();



            if(getClient().getSimpleModel().isSinglePlayer())
            {
                EndGameInfo endGameInfo = getSimpleModel().getElem(EndGameInfo.class).orElseThrow();
                text=new Text(endGameInfo.getCauseOfEndStringWithNames());
                text.setLayoutX(50);
                text.setLayoutY(30);
                anchor.getChildren().add(text);
                System.out.println(endGameInfo.getCauseOfEndStringWithNames());
            }

            text=new Text("NICK:  " + simplePlayerInfoMap.get(i).getNickname());
            text.setLayoutX(50);
            text.setLayoutY(50);
            anchor.getChildren().add(text);

            anchor.setMinSize(200,200);


            text=new Text("FAITH:  " + simplePlayerInfoMap.get(i).getCurrentPosition());
            text.setLayoutX(50);
            text.setLayoutY(110);

            anchor.getChildren().add(text);

            text=new Text("PLAYER INDEX:  " + simplePlayerInfoMap.get(i).getPlayerIndex());
            text.setLayoutX(50);
            text.setLayoutY(80);

            anchor.getChildren().add(text);

            text=new Text("VICTORY POINTS:  " + simplePlayerInfoMap.get(i).getCurrentVictoryPoints());
            text.setLayoutX(50);
            text.setLayoutY(150);
            anchor.setLayoutY(len/2);
            anchor.setLayoutX(startingX+200*i);
            System.out.println(startingX);


            anchor.getChildren().add(text);
            anchor.setId("background");

            winLoosePane.getChildren().add(anchor);


        }
        return new SubScene(winLoosePane,GUI.GUIwidth,GUI.GUIlen);


    }



    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

}


/*ObservableList<Node> workingCollection = FXCollections.observableArrayList(((Pane)getClient().getStage().getScene().getRoot()).getChildren());
            Collections.swap(workingCollection, 0, 1);
            ((Pane)getClient().getStage().getScene().getRoot()).getChildren().setAll(workingCollection);

 */
