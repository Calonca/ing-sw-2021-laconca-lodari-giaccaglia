package it.polimi.ingsw.client.view.GUI.util;

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

    List<DragAndDropData> shapes;
    private int lastDroppedPos;

    public DragAndDropHandler() {
        this.shapes = new ArrayList<>();
    }

    public void startDragAndDropOnEach(Group g, Rectangle board){
        shapes.stream().filter(s->s.available).forEach(s-> {
            s.shape3D.setOnMouseEntered((MouseEvent event)-> {
                startDragAndDrop(g,board,s.globalPos);
            });
        });
    }

    private Optional<DragAndDropData> dataWithPos(int pos){
        return shapes.stream().filter(d->d.globalPos==pos).findFirst();
    }

    public void startDragAndDrop(Group g, Rectangle dragArea, int gPos) {
        if (dataWithPos(gPos).isEmpty())
            return;
        DragAndDropData draggedData = dataWithPos(gPos).get();
        final Shape3D dragged = draggedData.shape3D;

        DragAndDropData oldShapeData = new DragAndDropData();
        oldShapeData.setShape3D(draggedData.resourceGUI.generateShape());
        oldShapeData.setResourceGUI(draggedData.resourceGUI);
        oldShapeData.setAvailable(true);
        oldShapeData.setGlobalPos(draggedData.globalPos);
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
            dragArea.setMouseTransparent(false);
            dragged.setCursor(Cursor.MOVE);
            dragged.startFullDrag();
        });

        dragged.setOnMouseReleased((MouseEvent event)-> {
            dragged.setMouseTransparent(false);
            shapes.forEach(s->s.shape3D.setMouseTransparent(false));
            shapes.forEach(s->ResourceGUI.setColor(s.resourceGUI,s.shape3D,false,false));
            optionInRange(draggedData).ifPresent(da->{
                Shape3D near = da.shape3D;
                dragged.setTranslateX(near.getTranslateX());
                dragged.setTranslateY(near.getTranslateY());
                dragged.setTranslateZ(near.getTranslateZ());

                near.setTranslateX(oldShape.getTranslateX());
                near.setTranslateY(oldShape.getTranslateY());
                near.setTranslateZ(oldShape.getTranslateZ());
                lastDroppedPos = da.globalPos;
                draggedData.onDrop.run();
            });
            ResourceGUI.setColor(draggedData.resourceGUI,dragged,false,false);
            g.getChildren().remove(oldShape);
            shapes.remove(oldShapeData);
            shapes.add(draggedData);
            dragStarted.set(false);
            dragArea.setMouseTransparent(true);
            dragged.setCursor(Cursor.DEFAULT);
        });

        dragArea.setOnMouseDragOver((MouseEvent event)->{
            Point3D stoneCoords = event.getPickResult().getIntersectedPoint();
            stoneCoords = dragArea.localToParent(stoneCoords);

            shapes.forEach(s->ResourceGUI.setColor(s.resourceGUI,s.shape3D,false,false));
            optionInRange(draggedData).ifPresent(s->ResourceGUI.setColor(s.resourceGUI,s.shape3D,true,true));
            //stoneCoords = boardTopLeft;
            dragged.setTranslateX(stoneCoords.getX());
            dragged.setTranslateY(stoneCoords.getY());
            dragged.setTranslateZ(stoneCoords.getZ());
            //Translate translate = new Translate(stoneCoords.getX(),stoneCoords.getY(),stoneCoords.getZ());
            //shield.getTransforms().add(translate);
        });
    }


    public int getLastDroppedPos() {
        return lastDroppedPos;
    }

    private Optional<DragAndDropData> optionInRange(DragAndDropData draggedData){
        Shape3D dragged = draggedData.shape3D;
        Map<DragAndDropData,Double> indexAndDist = shapes.stream().collect(Collectors.toMap(
                s->s,
                s->pos(s.shape3D).distance(pos(dragged))
        ));
        return indexAndDist.entrySet().stream()
                .filter(e->e.getValue()<range)
                .filter(e->draggedData.availablePos.contains(e.getKey().globalPos))
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).findFirst();
    }

    private Point3D pos(Shape3D shape){
        return new Point3D(shape.getTranslateX(),shape.getTranslateY(),shape.getTranslateZ());
    }


    public void addShape(DragAndDropData ddD){
        shapes.add(ddD);
    }
}
