package it.polimi.ingsw.client.view.GUI.util;

import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class DragAndDropHandler {

    private final static int range = 10000;

    List<DragAndDropData> shapes;
    private int lastDroppedPos;
    private Rectangle extendedArea;

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


    /**
     * @param globGroup the corresponding group in which to start
     * @param dragArea the starting drag area
     * @param gPos the starting global position
     */
    public void startDragAndDrop(Group globGroup,Rectangle dragArea, int gPos) {

        if (extendedArea==null){
            extendedArea = new Rectangle(dragArea.getWidth(),dragArea.getHeight());
            globGroup.getChildren().add(extendedArea);
            extendedArea.setOpacity(0);
        }
        extendedArea.setTranslateX(dragArea.getTranslateX());
        extendedArea.setTranslateY(dragArea.getTranslateY());
        extendedArea.setTranslateZ(dragArea.getTranslateZ());
        extendedArea.setScaleX(2);
        dragArea = extendedArea;

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
        Rectangle finalDragArea = dragArea;
        dragged.setOnDragDetected((MouseEvent event)-> {
            if (!dragStarted.get()) {
                dragStarted.set(true);
                ResourceGUI.setColor(draggedData.resourceGUI,dragged,true,true);
                Point3D inParent = new Point3D(dragged.getTranslateX(),dragged.getTranslateY(),dragged.getTranslateZ());
                oldShape.setTranslateX(inParent.getX());
                oldShape.setTranslateY(inParent.getY());
                oldShape.setTranslateZ(inParent.getZ());
                shapes.remove(draggedData);
                shapes.add(oldShapeData);
                g.getChildren().add(oldShape);
                globGroup.getChildren().add(g);
            }
            dragged.setMouseTransparent(true);
            shapes.forEach(s->s.shape3D.setMouseTransparent(true));
            finalDragArea.setMouseTransparent(false);
            dragged.setCursor(Cursor.MOVE);
            dragged.startFullDrag();
        });

        Rectangle finalDragArea1 = dragArea;
        dragged.setOnMouseReleased((MouseEvent event)-> {
            dragged.setMouseTransparent(false);
            shapes.forEach(s->s.shape3D.setMouseTransparent(false));
            shapes.forEach(s->ResourceGUI.setColor(s.resourceGUI,s.shape3D,false,true));
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
                ResourceGUI.setColor(draggedData.resourceGUI,dragged,false,true);
                g.getChildren().clear();
                globGroup.getChildren().remove(g);
                shapes.remove(oldShapeData);
                shapes.add(draggedData);
                dragStarted.set(false);
                finalDragArea1.setMouseTransparent(true);
                dragged.setCursor(Cursor.DEFAULT);
                draggedData.onDrop.run();
            });
        });

        Rectangle finalDragArea2 = dragArea;
        dragArea.setOnMouseDragOver((MouseEvent event)->{
            Point3D boardCoords = event.getPickResult().getIntersectedPoint();
            boardCoords = finalDragArea2.localToParent(boardCoords);
            boardCoords = boardCoords.subtract(dragged.getParent().localToParent(new Point3D(0,0,0)));

            shapes.forEach(s->ResourceGUI.setColor(s.resourceGUI,s.shape3D,false,true));
            optionInRange(draggedData).ifPresent(s->ResourceGUI.setColor(s.resourceGUI,s.shape3D,true,true));
            //stoneCoords = boardTopLeft;
            dragged.setTranslateX(boardCoords.getX());
            dragged.setTranslateY(boardCoords.getY());
            dragged.setTranslateZ(boardCoords.getZ());
            //Translate translate = new Translate(stoneCoords.getX(),stoneCoords.getY(),stoneCoords.getZ());
            //shield.getTransforms().add(translate);
        });
    }


    /**
     * @return the position of the last dropped element
     */
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
