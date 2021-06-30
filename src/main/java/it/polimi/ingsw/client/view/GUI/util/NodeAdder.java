package it.polimi.ingsw.client.view.GUI.util;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.List;

public class NodeAdder {


    public static void addNodeToParent(Group parent, Node board, Node shape, Point3D shift){
        Point3D boardTopLeft = board.localToParent(new Point3D(0,0,0));
        shape.setTranslateX(boardTopLeft.getX()+shift.getX());
        shape.setTranslateY(boardTopLeft.getY()+shift.getY());
        shape.setTranslateZ(boardTopLeft.getZ()+shift.getZ());
        parent.getChildren().add(shape);
    }

    public static void addNodeToParent(Group parent, Node shape, Point3D shift){
        shape.setTranslateX(shift.getX());
        shape.setTranslateY(shift.getY());
        shape.setTranslateZ(shift.getZ());
        parent.getChildren().add(shape);
    }

    public static void shiftAndAddToList(List<Node> parent, Node shape, Point3D shift){
        shape.setTranslateX(shift.getX());
        shape.setTranslateY(shift.getY());
        shape.setTranslateZ(shift.getZ());
        parent.add(shape);
    }
}
