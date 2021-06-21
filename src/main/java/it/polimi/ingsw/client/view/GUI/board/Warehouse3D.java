package it.polimi.ingsw.client.view.GUI.board;

import it.polimi.ingsw.client.view.GUI.BoardView3D;
import it.polimi.ingsw.client.view.GUI.layout.ResChoiceRowGUI;
import it.polimi.ingsw.client.view.GUI.util.DragAndDropData;
import it.polimi.ingsw.client.view.GUI.util.DragAndDropHandler;
import it.polimi.ingsw.client.view.GUI.util.ResourceGUI;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ProductionViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
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
        view3D.setWarehouse(w3d);

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
            final int finalLineN = Math.min(lineN, 2);
            AtomicInteger nInLine = lineN>2? new AtomicInteger(-lineN-2): new AtomicInteger();
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
        if (view3D.mode.equals(BoardView3D.Mode.SELECT_CARD_SHOP)||view3D.mode.equals(BoardView3D.Mode.SELECT_RESOURCE_FOR_PROD)){
            setSelectable(toSelect, view3D);
        }

    }

    public void setSelectable(ResChoiceRowGUI toSelect, BoardView3D view3D) {
        SelectablePositions selectablePositions = getThisPlayerCache().getElem(SelectablePositions.class).orElseThrow();

        SimpleWarehouseLeadersDepot simpleWarehouseLeadersDepot = getThisPlayerCache().getElem(SimpleWarehouseLeadersDepot.class).orElseThrow();
        Map<Integer,Integer> selectedPositionsMap = selectablePositions.getUpdatedSelectablePositions(toSelect.getChosenInputPos());

        AtomicInteger gPos = new AtomicInteger();
        for(Map.Entry<Integer, List<Pair<ResourceAsset, Boolean>>> line: simpleWarehouseLeadersDepot.getDepots().entrySet()) {
            line.getValue().forEach(e->{
                final int globalPos = gPos.get();
                ResourceGUI resourceGUI = ResourceGUI.fromAsset(e.getKey());
                Shape3D testShape = (Shape3D) wareGroup.getChildren().get(globalPos);
                boolean isSelectable = selectedPositionsMap.getOrDefault(globalPos, 0) > 0;
                ResourceGUI.setColor(resourceGUI, testShape, toSelect.getChosenInputPos().contains(globalPos), isSelectable);
                testShape.setOnMousePressed((u) -> {
                    if (isSelectable) {
                        toSelect.setNextInputPos(globalPos, e.getKey());
                        setSelectable(toSelect, view3D);
                        view3D.getStrongBox().updateStrongBox(view3D);
                        testShape.setOnMousePressed(n -> {
                        });
                        if (toSelect.getPointedResource().isEmpty()) {
                            if (view3D.mode.equals(BoardView3D.Mode.SELECT_CARD_SHOP)) {
                                CardShopViewBuilder.sendResourcesToBuy(toSelect.getChosenInputPos());
                            } else if (view3D.mode.equals(BoardView3D.Mode.SELECT_RESOURCE_FOR_PROD)) {
                                ProductionViewBuilder.sendChosenResources(toSelect.getChosenInputPos(),toSelect.getChosenOutputRes());
                            }
                        }
                    }
                });
                gPos.getAndIncrement();
            });
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

    public void clear() {
        wareGroup.getChildren().clear();
    }
}
