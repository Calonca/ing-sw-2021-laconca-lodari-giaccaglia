package it.polimi.ingsw.client.view.GUI.board;

import it.polimi.ingsw.client.view.GUI.BoardView3D;

import it.polimi.ingsw.client.view.GUI.util.ResourceGUI;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;

import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape3D;

import java.beans.PropertyChangeEvent;


public class FaithTrack extends ViewBuilder {


    double faithStartingX=150;
    double faithStartingY=120;
    Shape3D shape;
    public void faithTrackBuilder(BoardView3D view3D, Group parent, Rectangle board){
        shape = view3D.addAndGetShape(parent,parent,ResourceGUI.FAITH,board.localToParent(new Point3D(faithStartingX,faithStartingY,0)));
        faithStartingX=shape.getLayoutX();
        faithStartingY=shape.getLayoutY();
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
