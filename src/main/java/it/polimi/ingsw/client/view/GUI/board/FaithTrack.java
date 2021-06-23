package it.polimi.ingsw.client.view.GUI.board;

import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableLine;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import it.polimi.ingsw.client.view.GUI.BoardView3D;

import it.polimi.ingsw.client.view.GUI.util.ResourceGUI;

import it.polimi.ingsw.network.simplemodel.EndGameInfo;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;
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
import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getThisPlayerCache;


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

    public void faithTrackBuilder(BoardView3D view3D, Group parent, Rectangle board,int pos){
        player = view3D.addAndGetShape(faithGroup,faithGroup,ResourceGUI.FAITH,board.localToParent(new Point3D(faithStartingX,faithStartingY,0)));
        if (getSimpleModel().getPlayersCaches().length==1) {
            lorenzo = view3D.addAndGetShape(faithGroup,faithGroup, ResourceGUI.FAITH,board.localToParent(new Point3D(faithStartingX+10,faithStartingY+10,0)));
            lorenzo.setMaterial(new PhongMaterial(Color.BLACK));
        }
        playerNumber =pos;
        faithStartingX= player.getLayoutX();
        faithStartingY= player.getLayoutY();

        firstTile=new Rectangle(200,200);
        firstTile.setLayoutX(0);
        firstTile.setLayoutY(0);
        view3D.addNodeToParent(faithGroup,faithGroup,firstTile,board.localToParent(new Point3D(560,235,0)));
        ImagePattern tempImage = new ImagePattern(new Image("assets/track/FAVOUR_TILE_1_ACTIVE.png"));
        firstTile.setFill(tempImage);

        infoGroup=new Group();
        secondTile=new Rectangle(200,200);
        secondTile.setLayoutX(0);
        secondTile.setLayoutY(0);
        view3D.addNodeToParent(faithGroup,faithGroup,secondTile,board.localToParent(new Point3D(1150,110,0)));

        tempImage = new ImagePattern(new Image("assets/track/FAVOUR_TILE_2_ACTIVE.png"));
        secondTile.setFill(tempImage);


        thirdTile=new Rectangle(200,200);
        thirdTile.setLayoutX(0);
        thirdTile.setLayoutY(0);
        view3D.addNodeToParent(faithGroup,faithGroup,thirdTile,board.localToParent(new Point3D(1860,235,0)));

        tempImage = new ImagePattern(new Image("assets/track/FAVOUR_TILE_3_ACTIVE.png"));
        thirdTile.setFill(tempImage);


        popeTiles.add(firstTile);
        popeTiles.add(secondTile);
        popeTiles.add(thirdTile);

        moveFaith(0);
        if (getSimpleModel().getPlayersCaches().length==1)
            moveLorenzo(0);
        parent.getChildren().add(faithGroup);
        parent.getChildren().add(infoGroup);


    }

    public void moveFaith(int i) {

        SimpleFaithTrack faith= getThisPlayerCache().getElem(SimpleFaithTrack.class).orElseThrow();
        player.setLayoutX(faithStartingX+faith.getTrack().get(i).getX_pos()*boardWidth/20.5);
        player.setLayoutY(faithStartingY+faith.getTrack().get(i).getY_pos()* boardHeight /13);
        if(i==9)
            firstTile.setOpacity(0);
        if(i==17)
            secondTile.setOpacity(0);
        if(i==21)
            thirdTile.setOpacity(0);


    }

    public void moveLorenzo(int i) {

        SimpleFaithTrack faith= getThisPlayerCache().getElem(SimpleFaithTrack.class).orElseThrow();

        lorenzo.setLayoutX(faithStartingX+faith.getTrack().get(i).getX_pos()*boardWidth/20.5);
        lorenzo.setLayoutY(faithStartingY+faith.getTrack().get(i).getY_pos()* boardHeight /13);
        if(i==9)
            firstTile.setOpacity(0);
        if(i==17)
            secondTile.setOpacity(0);
        if(i==21)
            thirdTile.setOpacity(0);


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






            Runnable done = new Runnable()
            {
                public void run()
                {
                    infoGroup.getChildren().clear();

                    PlayersInfo.SimplePlayerInfo playerInfo;
                    for(int i=0;i<playersInfo.getSimplePlayerInfoMap().size();i++)
                    {

                        playerInfo=playersInfo.getSimplePlayerInfoMap().get(i);

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

                        BoardView3D.getBoard().addNodeToParent(infoGroup, infoGroup,anchor,new Point3D(0,150*i,0));
                        System.out.println(anchor.getTranslateZ());

                        anchor.setId("background");
                        if(!infoGroup.getChildren().contains(anchor))
                            infoGroup.getChildren().add(anchor);



                        VaticanReportInfo vaticanReportInfo;
                        if(getSimpleModel().getElem(VaticanReportInfo.class).get().hasReportOccurred())
                            {
                                vaticanReportInfo = getSimpleModel().getElem(VaticanReportInfo.class).get();

                                for(int k=0;k<vaticanReportInfo.getPopeTileStatusMap().size();k++)
                                    if(vaticanReportInfo.getPopeTileStatusMap().get(k).getValue())
                                        popeTiles.get(k).setOpacity(0);
                            }




                    }
                }
            };


            Platform.runLater(done);
        }

    }
}
