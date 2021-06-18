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
        return shapes.stream().filter(d->d.globalPos!=null).filter(d->d.globalPos==pos).findFirst();
    }

    public void startDragAndDrop(Group globGroup,Rectangle dragArea, int gPos) {
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

        Group g = new Group();
        g.setTranslateX(dragged.getParent().getTranslateX());
        g.setTranslateY(dragged.getParent().getTranslateY());
        g.setTranslateZ(dragged.getParent().getTranslateZ());


        AtomicBoolean dragStarted = new AtomicBoolean(false);
        dragged.setOnDragDetected((MouseEvent event)-> {
            if (!dragStarted.get()) {
                dragStarted.set(true);
                ResourceGUI.setColor(draggedData.resourceGUI,dragged,true,false);
                Point3D inParent = new Point3D(dragged.getTranslateX(),dragged.getTranslateY(),dragged.getTranslateZ());
                oldShape.setTranslateX(inParent.getX());
                oldShape.setTranslateY(inParent.getY());
                oldShape.setTranslateZ(inParent.getZ());
                Rotate rotate1 = new Rotate(270   ,new Point3D(1,0,0));
                Rotate rotate2 = new Rotate(270   ,new Point3D(0,1,0));
                oldShape.getTransforms().setAll(rotate1,rotate2);
                shapes.remove(draggedData);
                shapes.add(oldShapeData);
                g.getChildren().add(oldShape);
                globGroup.getChildren().add(g);
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
                Point3D nearGbl = globPos(near);
                nearGbl = nearGbl.subtract(dragged.getParent().localToParent(new Point3D(0,0,0)));
                dragged.setTranslateX(nearGbl.getX());
                dragged.setTranslateY(nearGbl.getY());
                dragged.setTranslateZ(nearGbl.getZ());

                Point3D oldShapeLocPos = globPos(oldShape);
                oldShapeLocPos = oldShapeLocPos.subtract(near.getParent().localToParent(new Point3D(0,0,0)));
                near.setTranslateX(oldShapeLocPos.getX());
                near.setTranslateY(oldShapeLocPos.getY());
                near.setTranslateZ(oldShapeLocPos.getZ());
                lastDroppedPos = da.globalPos;
                ResourceGUI.setColor(draggedData.resourceGUI,dragged,false,false);
                g.getChildren().clear();
                globGroup.getChildren().remove(g);
                shapes.remove(oldShapeData);
                shapes.add(draggedData);
                dragStarted.set(false);
                dragArea.setMouseTransparent(true);
                dragged.setCursor(Cursor.DEFAULT);
                draggedData.onDrop.run();
            });
        });

        dragArea.setOnMouseDragOver((MouseEvent event)->{
            Point3D boardCoords = event.getPickResult().getIntersectedPoint();
            boardCoords = dragArea.localToParent(boardCoords);
            boardCoords = boardCoords.subtract(dragged.getParent().localToParent(new Point3D(0,0,0)));

            shapes.forEach(s->ResourceGUI.setColor(s.resourceGUI,s.shape3D,false,false));
            optionInRange(draggedData).ifPresent(s->ResourceGUI.setColor(s.resourceGUI,s.shape3D,true,true));
            //stoneCoords = boardTopLeft;
            dragged.setTranslateX(boardCoords.getX());
            dragged.setTranslateY(boardCoords.getY());
            dragged.setTranslateZ(boardCoords.getZ());
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
                s-> globPos(s.shape3D).distance(globPos(dragged))
        ));
        return indexAndDist.entrySet().stream()
                .filter(e->e.getValue()<range)
                .filter(e->draggedData.availablePos.contains(e.getKey().globalPos))
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).findFirst();
    }

    private Point3D globPos(Shape3D shape){
        Point3D coords = new Point3D(shape.getTranslateX(),shape.getTranslateY(),shape.getTranslateZ());
        return coords.add(shape.getParent().getTranslateX(),shape.getParent().getTranslateY(),shape.getParent().getTranslateZ());
    }


    public void addShape(DragAndDropData ddD){
        shapes.add(ddD);
    }

    public void stopDragAndDrop() {
        shapes.forEach(s->s.shape3D.setOnMouseEntered(e->{}));
        shapes.forEach(s->s.shape3D.setOnDragDetected(e->{}));
    }
}
