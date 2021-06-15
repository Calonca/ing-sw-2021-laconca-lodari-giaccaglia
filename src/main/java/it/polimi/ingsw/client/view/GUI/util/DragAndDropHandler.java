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
        Integer globalPos;

        public Data(Shape3D shape3D, ResourceGUI resourceGUI, Runnable onDrop, boolean available, Integer globalPos) {
            this.shape3D = shape3D;
            this.resourceGUI = resourceGUI;
            this.onDrop = onDrop;
            this.available = available;
            this.globalPos = globalPos;
        }
    }

    List<Data> shapes;

    public DragAndDropHandler() {
        this.shapes = new ArrayList<>();
    }

    public void startDragAndDropOnEach(Group g, Rectangle board){
        shapes.forEach(s-> {
            s.shape3D.setOnMouseEntered((MouseEvent event)-> {
                startDragAndDrop(g,board,s.globalPos);
            });
        });
    }

    private Optional<Data> dataWithPos(int pos){
        return shapes.stream().filter(d->d.globalPos==pos).findFirst();
    }

    public void startDragAndDrop(Group g, Rectangle board, int gPos) {
        if (dataWithPos(gPos).isEmpty())
            return;
        Data draggedData = dataWithPos(gPos).get();
        final Shape3D dragged = draggedData.shape3D;

        Data oldShapeData = new Data(draggedData.resourceGUI.generateShape(),draggedData.resourceGUI,()->{},true,draggedData.globalPos);
        Shape3D oldShape = oldShapeData.shape3D;


        AtomicBoolean dragStarted = new AtomicBoolean(false);
        dragged.setOnDragDetected((MouseEvent event)-> {
            if (!dragStarted.get()) {
                dragStarted.set(true);
                ResourceGUI.setColor(draggedData.resourceGUI,dragged,true,false);
                oldShape.setTranslateX(dragged.getTranslateX());
                oldShape.setTranslateY(dragged.getTranslateY());
                oldShape.setTranslateZ(dragged.getTranslateZ());
                Rotate rotate1 = new Rotate(270   ,new Point3D(1,0,0));
                Rotate rotate2 = new Rotate(270   ,new Point3D(0,1,0));
                oldShape.getTransforms().setAll(rotate1,rotate2);
                shapes.remove(draggedData);
                shapes.add(oldShapeData);
                g.getChildren().add(oldShape);
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

                near.setTranslateX(oldShape.getTranslateX());
                near.setTranslateY(oldShape.getTranslateY());
                near.setTranslateZ(oldShape.getTranslateZ());

                draggedData.onDrop.run();
            });
            ResourceGUI.setColor(draggedData.resourceGUI,dragged,false,false);
            g.getChildren().remove(oldShape);
            shapes.remove(oldShapeData);
            shapes.add(draggedData);
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

    public void addShape(ResourceGUI resourceGUI,Shape3D shape3D,Runnable onDrop,boolean available,int globalPos){
        shapes.add(new Data(shape3D,resourceGUI,onDrop,available,globalPos));
    }
}
