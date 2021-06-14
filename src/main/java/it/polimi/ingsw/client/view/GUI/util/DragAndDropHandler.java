package it.polimi.ingsw.client.view.GUI.util;

import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.server.model.player.track.PopeFavourTile;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DragAndDropHandler {

    private final static int range = 10000;

    private class Data {
        Shape3D shape3D;
        ResourceGUI resourceGUI;
        Runnable onDrop;
        boolean available;

        public Data(Shape3D shape3D, ResourceGUI resourceGUI, Runnable onDrop, boolean available) {
            this.shape3D = shape3D;
            this.resourceGUI = resourceGUI;
            this.onDrop = onDrop;
            this.available = available;
        }
    }

    List<Data> shapes;
    private Shape3D dragged;

    public DragAndDropHandler() {
        this.shapes = new ArrayList<>();
    }

    public void startDragAndDropOnEach(Group g, Rectangle board){
        shapes.forEach(s->
                startDragAndDrop(g,board,s.shape3D,s.resourceGUI.generateShape()));
    }

    public void startDragAndDrop(Group g, Rectangle board, Shape3D dragged,Shape3D oldShape) {
        Data oldShapeData = new Data(oldShape,ResourceGUI.EMPTY,()->{},true);
        Shape3D initialPosShape = oldShapeData.shape3D;


        AtomicBoolean dragStarted = new AtomicBoolean(false);
        dragged.setOnDragDetected((MouseEvent event)-> {
            if (!dragStarted.get()) {
                dragStarted.set(true);
                ResourceGUI.getSelected(ResourceGUI.STONE,true,false);
                initialPosShape.setTranslateX(dragged.getTranslateX());
                initialPosShape.setTranslateY(dragged.getTranslateY());
                initialPosShape.setTranslateZ(dragged.getTranslateZ());
                Rotate rotate1 = new Rotate(270   ,new Point3D(1,0,0));
                Rotate rotate2 = new Rotate(270   ,new Point3D(0,1,0));
                initialPosShape.getTransforms().setAll(rotate1,rotate2);
                shapes.add(oldShapeData);
                g.getChildren().add(initialPosShape);
            }
            dragged.setMouseTransparent(true);
            shapes.forEach(s->s.shape3D.setMouseTransparent(true));
            board.setMouseTransparent(false);
            dragged.setCursor(Cursor.MOVE);
            dragged.startFullDrag();
        });

        dragged.setOnMouseReleased((MouseEvent event)-> {
            dragged.setMouseTransparent(false);
            shapes.forEach(s->s.shape3D.setMouseTransparent(false));
            shapes.forEach(s->ResourceGUI.setColor(s.resourceGUI,s.shape3D,false,false));
            optionInRange(dragged).ifPresent(da->{
                Shape3D near = da.shape3D;
                dragged.setTranslateX(near.getTranslateX());
                dragged.setTranslateY(near.getTranslateY());
                dragged.setTranslateZ(near.getTranslateZ());

                dragged.setTranslateY(near.getTranslateY());
                dragged.setTranslateZ(near.getTranslateZ());
            });
            ResourceGUI.getSelected(ResourceGUI.STONE,false,false);
            g.getChildren().remove(initialPosShape);
            shapes.remove(oldShapeData);
            dragStarted.set(false);
            board.setMouseTransparent(true);
            dragged.setCursor(Cursor.DEFAULT);
        });

        board.setOnMouseDragOver((MouseEvent event)->{
            Point3D stoneCoords = event.getPickResult().getIntersectedPoint();
            stoneCoords = board.localToParent(stoneCoords);

            shapes.forEach(s->ResourceGUI.setColor(s.resourceGUI,s.shape3D,false,false));
            optionInRange(dragged).ifPresent(s->ResourceGUI.setColor(s.resourceGUI,s.shape3D,true,true));
            //stoneCoords = boardTopLeft;
            dragged.setTranslateX(stoneCoords.getX());
            dragged.setTranslateY(stoneCoords.getY());
            dragged.setTranslateZ(stoneCoords.getZ());
            //Translate translate = new Translate(stoneCoords.getX(),stoneCoords.getY(),stoneCoords.getZ());
            //shield.getTransforms().add(translate);
        });
    }

    private Optional<Data> optionInRange(Shape3D dragged){
        Map<Integer,Double> indexAndDist = IntStream.range(0,shapes.size()).boxed().collect(Collectors.toMap(
                i->i,
                i->pos(shapes.get(i).shape3D).distance(pos(dragged))
        ));
        Optional<Integer> pos = indexAndDist.entrySet().stream().filter(e->e.getValue()<range)
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).findFirst();
        return pos.map(i->shapes.get(i));
    }

    private Point3D pos(Shape3D shape){
        return new Point3D(shape.getTranslateX(),shape.getTranslateY(),shape.getTranslateZ());
    }

    public void addShape(ResourceGUI resourceGUI,Shape3D shape3D,Runnable onDrop,boolean available){
        shapes.add(new Data(shape3D,resourceGUI,onDrop,available));
    }
}
