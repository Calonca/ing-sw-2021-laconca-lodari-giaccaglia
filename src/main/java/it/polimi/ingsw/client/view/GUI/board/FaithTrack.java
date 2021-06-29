package it.polimi.ingsw.client.view.GUI.board;

import it.polimi.ingsw.client.simplemodel.PlayerCache;
import it.polimi.ingsw.client.view.GUI.BoardView3D;

import it.polimi.ingsw.client.view.GUI.WinLooseGUI;
import it.polimi.ingsw.client.view.GUI.util.NodeAdder;
import it.polimi.ingsw.client.view.GUI.util.ResourceGUI;

import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;
import it.polimi.ingsw.network.simplemodel.TileState;
import it.polimi.ingsw.network.simplemodel.VaticanReportInfo;
import javafx.animation.*;
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
import javafx.util.Duration;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getSimpleModel;


public class FaithTrack implements PropertyChangeListener {


    final double faithStartingX=150;
    final double faithStartingY=120;
    final int boardWidth = 2407;
    final int boardHeight = 1717;

    private Shape3D player;
    private int playerPos;
    private Shape3D lorenzo;
    private int lorenzoPos;
    int playerNumber;
    private final Group faithGroup=new Group();
    private PlayerCache cache;
    private BoardView3D view3D;

    public void faithTrackBuilder(BoardView3D view3D, Group parent, Rectangle board, int playerNumber, PlayerCache cache){
        player = ResourceGUI.addAndGetShape(faithGroup,ResourceGUI.FAITH,board.localToParent(new Point3D(faithStartingX,faithStartingY,0)));
        this.playerNumber =playerNumber;
        this.cache = cache;
        this.view3D = view3D;

        if (cache.getElem(SimpleFaithTrack.class).orElseThrow().getTrack()!=null) {
            animateInFaithTrack(-1, 0, player, cache.getElem(SimpleFaithTrack.class).orElseThrow(), 0);
            if (getSimpleModel().getPlayersCaches().length == 1) {
                lorenzo = ResourceGUI.addAndGetShape(faithGroup, ResourceGUI.FAITH, board.localToParent(new Point3D(faithStartingX + 10, faithStartingY + 10, 0)));
                lorenzo.setMaterial(new PhongMaterial(Color.BLACK));
                animateInFaithTrack(-1, 0, lorenzo, cache.getElem(SimpleFaithTrack.class).orElseThrow(), 10);
            }
        }




        parent.getChildren().add(faithGroup);



    }

    public void moveFaith(int endPos, SimpleFaithTrack track) {
        animateInFaithTrack(playerPos,endPos,player,track,0);
        playerPos = endPos;
    }

    public void moveLorenzo(int endPos, SimpleFaithTrack track) {
        animateInFaithTrack(lorenzoPos,endPos,lorenzo,track,10);
        lorenzoPos = endPos;
    }

    /**
     * Animates the piece from the start to the end position following the faithTrack
     * Shift is used to shift overlapping pieces
     */
    private void animateInFaithTrack(int start, int end, Shape3D piece, SimpleFaithTrack track, int shift){
        if (start<end && end>-1) {
            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), piece);

            translateTransition.setFromX(piece.getTranslateX());
            translateTransition.setFromY(piece.getTranslateY());


            int nextPos = start+1;
            Point3D nextPoint =  view3D.boardRec.localToParent(
                    shift+faithStartingX+track.getTrack().get(nextPos).getX_pos()*boardWidth/20.5,
                    shift+faithStartingY+track.getTrack().get(nextPos).getY_pos()*boardHeight/13.0,
                    0);
            translateTransition.setToX(nextPoint.getX());
            translateTransition.setToY(nextPoint.getY());

            //Animate next position
            translateTransition.setOnFinished(e->animateInFaithTrack(nextPos,end,piece,track, shift));

            translateTransition.play();
        }
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(SimpleFaithTrack.class.getSimpleName()))
        {
            SimpleFaithTrack simpleFaithTrack = (SimpleFaithTrack) evt.getNewValue();
            moveFaith(simpleFaithTrack.getPlayerPosition(), simpleFaithTrack);
            if (getSimpleModel().getPlayersCaches().length==1)
                moveLorenzo(simpleFaithTrack.getLorenzoPosition(), simpleFaithTrack);


        }

    }
}
