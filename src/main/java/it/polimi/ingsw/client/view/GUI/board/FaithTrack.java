package it.polimi.ingsw.client.view.GUI.board;

import it.polimi.ingsw.client.simplemodel.PlayerCache;
import it.polimi.ingsw.client.view.GUI.BoardView3D;

import it.polimi.ingsw.client.view.GUI.util.NodeAdder;
import it.polimi.ingsw.client.view.GUI.util.ResourceGUI;

import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;
import it.polimi.ingsw.network.simplemodel.TileState;
import it.polimi.ingsw.network.simplemodel.VaticanReportInfo;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape3D;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getSimpleModel;


public class FaithTrack implements PropertyChangeListener {


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
    Group faithGroup=new Group();
    Group infoGroup;
    private PlayerCache cache;

    public void faithTrackBuilder(BoardView3D view3D, Group parent, Rectangle board, int playerNumber, PlayerCache cache){
        player = view3D.addAndGetShape(faithGroup,ResourceGUI.FAITH,board.localToParent(new Point3D(faithStartingX,faithStartingY,0)));
        if (getSimpleModel().getPlayersCaches().length==1) {
            lorenzo = view3D.addAndGetShape(faithGroup, ResourceGUI.FAITH,board.localToParent(new Point3D(faithStartingX+10,faithStartingY+10,0)));
            lorenzo.setMaterial(new PhongMaterial(Color.BLACK));
        }
        this.playerNumber =playerNumber;
        faithStartingX= player.getLayoutX();
        faithStartingY= player.getLayoutY();
        this.cache = cache;


        firstTile=new Rectangle(200,200);
        firstTile.setLayoutX(0);
        firstTile.setLayoutY(0);
        NodeAdder.addNodeToParent(faithGroup,faithGroup,firstTile,board.localToParent(new Point3D(560,235,0)));
        ImagePattern tempImage = new ImagePattern(new Image("assets/track/FAVOUR_TILE_1_INACTIVE.png"));
        firstTile.setFill(tempImage);

        infoGroup=new Group();
        secondTile=new Rectangle(200,200);
        secondTile.setLayoutX(0);
        secondTile.setLayoutY(0);
        NodeAdder.addNodeToParent(faithGroup,faithGroup,secondTile,board.localToParent(new Point3D(1150,110,0)));

        tempImage = new ImagePattern(new Image("assets/track/FAVOUR_TILE_2_INACTIVE.png"));
        secondTile.setFill(tempImage);


        thirdTile=new Rectangle(200,200);
        thirdTile.setLayoutX(0);
        thirdTile.setLayoutY(0);
        NodeAdder.addNodeToParent(faithGroup,faithGroup,thirdTile,board.localToParent(new Point3D(1860,235,0)));

        tempImage = new ImagePattern(new Image("assets/track/FAVOUR_TILE_3_INACTIVE.png"));
        thirdTile.setFill(tempImage);


        popeTiles.add(firstTile);
        popeTiles.add(secondTile);
        popeTiles.add(thirdTile);

        player.setLayoutY(faithStartingY);
        player.setLayoutX(faithStartingX);


        parent.getChildren().add(faithGroup);
        parent.getChildren().add(infoGroup);


    }

    public void moveFaith(int i) {

        SimpleFaithTrack faith= cache.getElem(SimpleFaithTrack.class).orElseThrow();
        player.setLayoutX(faithStartingX+faith.getTrack().get(i).getX_pos()*boardWidth/20.5);
        player.setLayoutY(faithStartingY+faith.getTrack().get(i).getY_pos()* boardHeight /13.0);


    }

    public void moveLorenzo(int i) {

        SimpleFaithTrack faith= cache.getElem(SimpleFaithTrack.class).orElseThrow();

        lorenzo.setLayoutX(faithStartingX+faith.getTrack().get(i).getX_pos()*boardWidth/20.5);
        lorenzo.setLayoutY(faithStartingY+faith.getTrack().get(i).getY_pos()* boardHeight /13.0);



    }



    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(PlayersInfo.class.getSimpleName()))
        {
            PlayersInfo playersInfo =  (PlayersInfo) evt.getNewValue();
            moveFaith(playersInfo.getSimplePlayerInfoMap().get(playerNumber).getCurrentPosition());
            System.out.println("CURRENT POSITION" +playersInfo.getSimplePlayerInfoMap().get(playerNumber).getCurrentPosition());
            if (getSimpleModel().getPlayersCaches().length==1)
                moveLorenzo(playersInfo.getSimplePlayerInfoMap().get(playerNumber).getLorenzoPosition());





            Runnable done = () -> {
                infoGroup.getChildren().clear();

                PlayersInfo.SimplePlayerInfo playerInfo;
                for(int i=0;i<playersInfo.getSimplePlayerInfoMap().size();i++)
                {

                    playerInfo=playersInfo.getSimplePlayerInfoMap().get(i);


                    if(getSimpleModel().getElem(VaticanReportInfo.class).isPresent())
                    {
                        VaticanReportInfo vaticanReportInfo;
                        vaticanReportInfo=getSimpleModel().getElem(VaticanReportInfo.class).get();

                        if(vaticanReportInfo.getPopeTileStateMap().get(playerNumber).getKey()!=-1)
                            if(!vaticanReportInfo.getPopeTileStateMap().get(playerNumber).getValue().equals(TileState.ACTIVE))
                                popeTiles.get(vaticanReportInfo.getPopeTileStateMap().get(playerNumber).getKey()).setOpacity(0);
                            else
                                popeTiles.get(vaticanReportInfo.getPopeTileStateMap().get(playerNumber).getKey()).setFill(new ImagePattern(new Image("assets/track/FAVOUR_TILE_"+vaticanReportInfo.getPopeTileStateMap().get(playerNumber).getKey()+"_ACTIVE.png")));

                        System.out.println(vaticanReportInfo.getPopeTileStateMap().get(playerNumber));



                    }

                    AnchorPane anchor=new AnchorPane();

                    Text text;
                    text=new Text("NICK:  " + playerInfo.getNickname());
                    text.setLayoutX(50);
                    text.setLayoutY(20);
                    anchor.getChildren().add(text);

                    anchor.setMinSize(200,100);

                    if(playerInfo.isOnline())
                        text=new Text("STATUS: ONLINE");
                    else
                        text=new Text("STATUS: OFFLINE");
                    text.setLayoutX(50);
                    text.setLayoutY(10);

                    anchor.getChildren().add(text);

                    text=new Text("FAITH:  " + playerInfo.getCurrentPosition());
                    text.setLayoutX(50);
                    text.setLayoutY(60);

                    anchor.getChildren().add(text);

                    text=new Text("PLAYER INDEX:  " + playerInfo.getPlayerIndex());
                    text.setLayoutX(50);
                    text.setLayoutY(80);

                    anchor.getChildren().add(text);

                    text=new Text("VICTORY POINTS:  " + playerInfo.getCurrentVictoryPoints());
                    text.setLayoutX(50);
                    text.setLayoutY(40);

                    anchor.getChildren().add(text);

                    NodeAdder.addNodeToParent(infoGroup, infoGroup,anchor,new Point3D(0,150*i,0));

                    anchor.setId("background");
                    if(!infoGroup.getChildren().contains(anchor))
                        infoGroup.getChildren().add(anchor);



                }
            };


            Platform.runLater(done);
        }

    }
}
