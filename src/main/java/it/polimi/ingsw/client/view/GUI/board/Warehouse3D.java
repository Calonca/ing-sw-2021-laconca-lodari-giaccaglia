package it.polimi.ingsw.client.view.GUI.board;

import it.polimi.ingsw.client.view.GUI.BoardView3D;
import it.polimi.ingsw.client.view.GUI.layout.ResChoiceRowGUI;
import it.polimi.ingsw.client.view.GUI.util.DragAndDropData;
import it.polimi.ingsw.client.view.GUI.util.DragAndDropHandler;
import it.polimi.ingsw.client.view.GUI.util.ResourceGUI;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.simplemodel.SelectablePositions;
import it.polimi.ingsw.network.simplemodel.SimpleWarehouseLeadersDepot;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape3D;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getThisPlayerCache;

public class Warehouse3D {

    private final Group wareGroup;

    private Warehouse3D(){
        wareGroup = new Group();
    }

    public static void wareBuilder(BoardView3D view3D, Group parent, Rectangle board, DragAndDropHandler dropHandler){
        Warehouse3D w3d= new Warehouse3D();
        w3d.buildView(view3D, dropHandler);
        view3D.setWarehouse(w3d.wareGroup);

        Point3D initialPos = new Point3D(100,800,0);
        view3D.addNodeToParent(parent, board, w3d.wareGroup, initialPos);
    }

    public void buildView(BoardView3D view3D, DragAndDropHandler dropHandler){
        wareGroup.getChildren().clear();
        final ResChoiceRowGUI toSelect = view3D.getToSelect();

        final double wareWidth = 500;
        final double lineHeight = 150;
        SimpleWarehouseLeadersDepot simpleWarehouseLeadersDepot = getThisPlayerCache().getElem(SimpleWarehouseLeadersDepot.class).orElseThrow();

        AtomicInteger gPos = new AtomicInteger();
        int lineN = 0;
        for (Map.Entry<Integer, List<Pair<ResourceAsset, Boolean>>> line: simpleWarehouseLeadersDepot.getDepots().entrySet()){
            int finalLineN = lineN;
            AtomicInteger nInLine = new AtomicInteger();
            line.getValue().forEach(e->{
                double x  = (wareWidth/(1+line.getValue().size())*(1+nInLine.get()));
                Point3D shift = new Point3D(x,lineHeight* finalLineN,0);
                nInLine.getAndIncrement();
                final int globalPos = gPos.get();
                ResourceGUI resourceGUI = ResourceGUI.fromAsset(e.getKey());
                Shape3D testShape = view3D.addAndGetShape(wareGroup,wareGroup,resourceGUI,shift);
                if (view3D.mode.equals(BoardView3D.Mode.MOVING_RES))
                    addToDropHandler(view3D, dropHandler, simpleWarehouseLeadersDepot, globalPos, resourceGUI, testShape);
                gPos.getAndIncrement();
            });
            lineN++;
        }
        if (view3D.mode.equals(BoardView3D.Mode.SELECT_CARD_SHOP)){
            setSelectable(toSelect);
        }

    }

    private void setSelectable(ResChoiceRowGUI toSelect) {
        SelectablePositions selectablePositions = getThisPlayerCache().getElem(SelectablePositions.class).orElseThrow();

        SimpleWarehouseLeadersDepot simpleWarehouseLeadersDepot = getThisPlayerCache().getElem(SimpleWarehouseLeadersDepot.class).orElseThrow();

        AtomicInteger gPos = new AtomicInteger();
        int lineN = 0;
        for(Map.Entry<Integer, List<Pair<ResourceAsset, Boolean>>> line: simpleWarehouseLeadersDepot.getDepots().entrySet()) {
            line.getValue().forEach(e->{
                final int globalPos = gPos.get();
                ResourceGUI resourceGUI = ResourceGUI.fromAsset(e.getKey());
                Shape3D testShape = (Shape3D) wareGroup.getChildren().get(globalPos);
                testShape.setOnMousePressed((u) -> {
                    Map<Integer,Integer> selectedPositionsMap = selectablePositions.getUpdatedSelectablePositions(toSelect.getChosenInputPos());
                    boolean isSelectable = selectedPositionsMap.getOrDefault(globalPos, 0) > 0;
                    if (isSelectable) {
                        toSelect.setNextInputPos(globalPos, e.getKey());
                        ResourceGUI.setColor(resourceGUI, testShape, true, false);
                        setSelectable(toSelect);
                        testShape.setOnMousePressed(n -> {
                        });
                        if (toSelect.getPointedResource().isEmpty()) {
                            CardShopViewBuilder.sendResourcesToBuy(toSelect.getChosenInputPos());
                        }
                    }
                });
                gPos.getAndIncrement();
            });
            lineN++;
        }
    }


    private void addToDropHandler(BoardView3D view3D, DragAndDropHandler dropHandler, SimpleWarehouseLeadersDepot simpleWarehouseLeadersDepot, int globalPos, ResourceGUI resourceGUI, Shape3D testShape) {
        DragAndDropData dragAndDropData = new DragAndDropData();
        dragAndDropData.setResourceGUI(resourceGUI);
        dragAndDropData.setShape3D(testShape);
        dragAndDropData.setAvailable(simpleWarehouseLeadersDepot.isPosAvailable(globalPos));
        dragAndDropData.setAvailablePos(simpleWarehouseLeadersDepot.getAvailableMovingPositions().get(globalPos));
        dragAndDropData.setGlobalPos(globalPos);

        dragAndDropData.setOnDrop(()->{
            if (view3D.mode.equals(BoardView3D.Mode.MOVING_RES)) {
                dropHandler.stopDragAndDrop();
                ResourceMarketViewBuilder.sendMove(globalPos, dropHandler.getLastDroppedPos());
            }
        });

        dropHandler.addShape(dragAndDropData);
    }

}
