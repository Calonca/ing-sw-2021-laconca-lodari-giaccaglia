package it.polimi.ingsw.client.view.GUI.board;

import it.polimi.ingsw.client.view.GUI.BoardView3D;

import it.polimi.ingsw.client.view.GUI.util.ResourceGUI;

import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape3D;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getSimpleModel;
import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getThisPlayerCache;


public class FaithTrack implements PropertyChangeListener {


    double faithStartingX=150;
    double faithStartingY=120;
    final int boardWidth = 2407;
    final int boardHeight = 1717;
    Rectangle firstTile;
    Rectangle secondTile;
    Rectangle thirdTile;
    Shape3D player;
    Shape3D lorenzo;
    int playerNumber;

    public void faithTrackBuilder(BoardView3D view3D, Group parent, Rectangle board,int pos){
        Group faithGroup=new Group();
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

        System.out.println("PLAYER POSITION" + playerNumber);

        moveFaith(0);
        if (getSimpleModel().getPlayersCaches().length==1)
            moveLorenzo(0);
        parent.getChildren().add(faithGroup);

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
            System.out.println("PLAYER POSITION" +playersInfo.getSimplePlayerInfoMap().get(playerNumber).getCurrentPosition());
            if (getSimpleModel().getPlayersCaches().length==1)
                moveLorenzo(playersInfo.getSimplePlayerInfoMap().get(playerNumber).getLorenzoPosition());

        }

    }
}
