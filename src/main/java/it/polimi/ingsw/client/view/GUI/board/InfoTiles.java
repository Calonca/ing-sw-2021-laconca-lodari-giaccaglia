package it.polimi.ingsw.client.view.GUI.board;

import it.polimi.ingsw.client.simplemodel.PlayerCache;
import it.polimi.ingsw.client.view.GUI.BoardView3D;
import it.polimi.ingsw.client.view.GUI.util.NodeAdder;
import it.polimi.ingsw.network.assets.tokens.ActionTokenAsset;
import it.polimi.ingsw.network.simplemodel.*;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape3D;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getSimpleModel;

public class InfoTiles implements PropertyChangeListener {


    double faithStartingX=150;
    double faithStartingY=120;
    final int boardWidth = 2407;
    final int boardHeight = 1717;
    List<Rectangle> popeTiles=new ArrayList<>();
    Rectangle firstTile;
    Rectangle secondTile;
    Rectangle thirdTile;
    Shape3D player;
    Shape3D lorenzo;
    int playerNumber;
    List<AnchorPane> playersInfo;
    Group infoGroup=new Group();
    private PlayerCache cache;

    public void infoBuilder(BoardView3D view3D, Group parent, Rectangle board, int playerNumber, PlayerCache cache){

        this.playerNumber =playerNumber;
        this.cache = cache;

        parent.getChildren().add(infoGroup);



    }



    public AnchorPane playerStatsTile(SimplePlayerInfo playerInfo, String state)
    {
        AnchorPane anchor=new AnchorPane();

        Text text;
        text=new Text("NICK:  " + playerInfo.getNickname());
        text.setLayoutX(50);
        text.setLayoutY(50);
        anchor.getChildren().add(text);

        anchor.setMinSize(200,200);

        if(playerInfo.isOnline())
            text=new Text("STATUS:  "+state);
        else
            text=new Text("STATUS: OFFLINE");
        text.setLayoutX(50);
        text.setLayoutY(20);

        anchor.getChildren().add(text);

        text=new Text("FAITH:  " + playerInfo.getCurrentPosition());
        text.setLayoutX(50);
        text.setLayoutY(110);

        anchor.getChildren().add(text);

        text=new Text("PLAYER INDEX:  " + playerInfo.getPlayerIndex());
        text.setLayoutX(50);
        text.setLayoutY(80);

        anchor.getChildren().add(text);

        text=new Text("VICTORY POINTS:  " + playerInfo.getCurrentVictoryPoints());
        text.setLayoutX(50);
        text.setLayoutY(150);



        anchor.getChildren().add(text);
        anchor.setId("background");

        return anchor;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(PlayersInfo.class.getSimpleName()))
        {
            PlayersInfo playersInfo =  (PlayersInfo) evt.getNewValue();


            Runnable done = () -> {
                infoGroup.getChildren().clear();

                if (getSimpleModel().getPlayersCaches().length==1)
                {
                    SimpleSoloActionToken actionToken = getSimpleModel().getPlayerCache(0).getElem(SimpleSoloActionToken.class).orElseThrow();
                    ActionTokenAsset tokenAsset = actionToken.getSoloActionToken();

                    Rectangle actionTokenRectangle=new Rectangle(100,100);
                    actionTokenRectangle.setFill(new ImagePattern(new Image(tokenAsset.getFrontPath().toString())));
                    NodeAdder.addNodeToParent(infoGroup,infoGroup,actionTokenRectangle,infoGroup.localToParent(-100,-100,1000));
                }

                for(int i=0;i<playersInfo.getSimplePlayerInfoMap().size();i++)
                {
                        NodeAdder.addNodeToParent(infoGroup, infoGroup,playerStatsTile(playersInfo.getSimplePlayerInfoMap().get(i),getSimpleModel().getPlayerCache(i).getCurrentState()),new Point3D(-400,210*i,1100));

                }

            };


            Platform.runLater(done);
        }

    }
}
