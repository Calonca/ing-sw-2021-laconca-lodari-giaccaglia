package it.polimi.ingsw.client.view.gui.util;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.List;

public class NodeAdder {


    /**
     * Adds the given shape to the parent, it's position is that of the refSystemPosition shifted of shift
     * @param parent where the shape is added
     * @param refSystem The node used as the reference system
     * @param shape The node to add
     * @param shift The shift in respect to the reference positioning system
     */
    public static void addNodeToParent(Group parent, Node refSystem, Node shape, Point3D shift){
        Point3D boardTopLeft = refSystem.localToParent(new Point3D(0,0,0));
        setTranslate(shape,boardTopLeft.add(shift));
        parent.getChildren().add(shape);
    }

    /**
     * Adds the given shape to the parent, it's position is that of the parent shifted of shift
     * @param parent where the shape is added
     * @param shape The node to add
     * @param shift The shift in respect to the parent
     */
    public static void addNodeToParent(Group parent, Node shape, Point3D shift){
        setTranslate(shape,shift);
        parent.getChildren().add(shape);
    }

    /**
     * Adds the given shape to the list, translated of shift
     * @param parent A list of nodes
     * @param shape The node to add
     * @param shift The translation of the node
     */
    public static void shiftAndAddToList(List<Node> parent, Node shape, Point3D shift){
        setTranslate(shape,shift);
        parent.add(shape);
    }

    /**
     * Translates the node of shift
     */
    public static void setTranslate(Node shape,Point3D shift){
        shape.setTranslateX(shift.getX());
        shape.setTranslateY(shift.getY());
        shape.setTranslateZ(shift.getZ());
    }
}
