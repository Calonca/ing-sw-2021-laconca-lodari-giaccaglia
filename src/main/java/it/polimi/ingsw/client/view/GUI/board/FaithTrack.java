package it.polimi.ingsw.client.view.GUI.board;

import it.polimi.ingsw.client.view.GUI.BoardView3D;

import it.polimi.ingsw.client.view.GUI.util.ResourceGUI;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;

import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape3D;

import java.beans.PropertyChangeEvent;


public class FaithTrack extends ViewBuilder {


    double faithStartingX=150;
    double faithStartingY=120;
    Shape3D shape;
    public void faithTrackBuilder(BoardView3D view3D, Group parent, Rectangle board){
        Group faithGroup=new Group();
        shape = view3D.addAndGetShape(faithGroup,faithGroup,ResourceGUI.FAITH,board.localToParent(new Point3D(faithStartingX,faithStartingY,0)));
        faithStartingX=shape.getLayoutX();
        faithStartingY=shape.getLayoutY();

        Rectangle rectangle=new Rectangle(200,200);
        rectangle.setLayoutX(0);
        rectangle.setLayoutY(0);
        view3D.addNodeToParent(faithGroup,faithGroup,rectangle,board.localToParent(new Point3D(560,235,0)));
        ImagePattern tempImage = new ImagePattern(new Image("assets/track/FAVOUR_TILE_1_ACTIVE.png"));
        rectangle.setFill(tempImage);


        rectangle=new Rectangle(200,200);
        rectangle.setLayoutX(0);
        rectangle.setLayoutY(0);
        view3D.addNodeToParent(faithGroup,faithGroup,rectangle,board.localToParent(new Point3D(1150,110,0)));

        tempImage = new ImagePattern(new Image("assets/track/FAVOUR_TILE_2_ACTIVE.png"));
        rectangle.setFill(tempImage);


        rectangle=new Rectangle(200,200);
        rectangle.setLayoutX(0);
        rectangle.setLayoutY(0);
        view3D.addNodeToParent(faithGroup,faithGroup,rectangle,board.localToParent(new Point3D(1860,235,0)));

        tempImage = new ImagePattern(new Image("assets/track/FAVOUR_TILE_3_ACTIVE.png"));
        rectangle.setFill(tempImage);

        parent.getChildren().add(faithGroup);

    }

    public void moveFaith(Shape3D faithBut, int i, double width, double lenght) {

        SimpleFaithTrack faith=getThisPlayerCache().getElem(SimpleFaithTrack.class).orElseThrow();
        faithBut.setLayoutX(faithStartingX+faith.getTrack().get(i).getX_pos()*width/20.5);
        faithBut.setLayoutY(faithStartingY+faith.getTrack().get(i).getY_pos()*lenght/13);


    }

    @Override
    public void run() {
        moveFaith(shape,5, 2403,1717);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
