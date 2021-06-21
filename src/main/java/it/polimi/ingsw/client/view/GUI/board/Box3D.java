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
import it.polimi.ingsw.network.simplemodel.SimpleDiscardBox;
import it.polimi.ingsw.network.simplemodel.SimpleStrongBox;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape3D;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.client.view.GUI.board.Text3D.addTextOnGroup;
import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getThisPlayerCache;

public class Box3D {

    public static void discardBuilder(BoardView3D view3D, Group parent, Rectangle board, DragAndDropHandler dropHandler){
        Group discardBoxGroup = new Group();
        Point3D initialPos = new Point3D(800,800,0);
        SimpleDiscardBox simpleDiscardBox =  getThisPlayerCache().getElem(SimpleDiscardBox.class).orElseThrow();
        //Pos       //Res           //NOfRes
        final Set<Map.Entry<Integer, Pair<ResourceAsset, Integer>>> entries = simpleDiscardBox.getResourceMap()
                .entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toCollection(LinkedHashSet::new));
        final double lineHeight = 150;
        int addedLines = 0;
        for (Map.Entry<Integer, Pair<ResourceAsset, Integer>> line : entries) {
            Group lineGroup = new Group();

            Integer num = line.getValue().getValue();
            addTextOnGroup(lineGroup, 0, String.valueOf(num));

            lineGroup.setTranslateY(lineHeight * addedLines);

            int gPos = line.getKey();

            ResourceGUI resTest = ResourceGUI.fromAsset(line.getValue().getKey());
            Shape3D shape = view3D.addAndGetShape(discardBoxGroup,discardBoxGroup,resTest,new Point3D(0,lineHeight * addedLines,0));
            discardBoxGroup.getChildren().add(lineGroup);
            DragAndDropData dragAndDropData = new DragAndDropData();
            dragAndDropData.setResourceGUI(resTest);
            dragAndDropData.setShape3D(shape);
            dragAndDropData.setAvailable(simpleDiscardBox.isPosAvailable(gPos));
            dragAndDropData.setAvailablePos(simpleDiscardBox.getAvailableMovingPositions().get(gPos));
            dragAndDropData.setGlobalPos(gPos!=0?gPos:null);
            if (view3D.mode.equals(BoardView3D.Mode.MOVING_RES))
                dragAndDropData.setOnDrop(()->{
                    ResourceMarketViewBuilder.sendMove(gPos,dropHandler.getLastDroppedPos());
                });
            dropHandler.addShape(dragAndDropData);

            addedLines++;
        }
        addDiscardButton(view3D, discardBoxGroup, simpleDiscardBox, addedLines);

        view3D.addNodeToParent(parent, board, discardBoxGroup, initialPos);
        view3D.setDiscardBox(discardBoxGroup);
    }

    public void strongBuilder(BoardView3D view3D, Group parent, Rectangle board){
        if (strongBoxGroup==null) {
          strongBoxInit(view3D, parent, board);
        }
        updateStrongBox(view3D);
    }

    public void updateStrongBox(BoardView3D view3D) {
        SimpleStrongBox simpleStrongBox =  getThisPlayerCache().getElem(SimpleStrongBox.class).orElseThrow();
        SelectablePositions selectablePositions = getThisPlayerCache().getElem(SelectablePositions.class).orElseThrow();
        ResChoiceRowGUI toSelect = view3D.getToSelect();
        Map<Integer,Integer> selectablePositionsMap = new HashMap<>();
        if (toSelect!=null)
            selectablePositionsMap = selectablePositions.getUpdatedSelectablePositions(toSelect.getChosenInputPos());

        int addedLines = 0;
        //                  Pos           Res                 number   selected
        final Set<Map.Entry<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>>> entries = simpleStrongBox.getResourceMap().entrySet();
        for (Map.Entry<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>> line : entries) {
            Group lineGroup = new Group();
            int gPos = line.getKey();

            final StrongLine sLine = strongMap.get(gPos);
            sLine.resNum.setText(SimpleStrongBox.numAndSel(line).getKey());

            ResourceGUI resTest = sLine.res;
            Shape3D shape = sLine.shape;


            boolean isSelectable = false;
            long numOfSelected =  SimpleStrongBox.numAndSel(line).getValue();
            final long numSelectable = selectablePositionsMap.getOrDefault(line.getKey(), 0);
            if (toSelect!=null) {
                numOfSelected += toSelect.getChosenInputPos().stream().filter(i -> i.equals(gPos)).count();
                isSelectable = numSelectable>0 || !toSelect.choosingInput();
                // numOfSelected;
                ResourceGUI.setColor(resTest, shape, false, isSelectable);
                //ResourceGUI.setColor(resTest, shape, toSelect.getChosenInputPos().contains(gPos), isSelectable);
            }
            System.out.println("Pos "+gPos+" sel: "+numSelectable);
            sLine.selected.setText((int) numOfSelected);
            boolean finalIsSelectable = isSelectable;
            int finalAddedLines = addedLines;
            shape.setOnMousePressed((u)->{
                if (true) {
                    if (toSelect.choosingInput())
                        view3D.getToSelect().setNextInputPos(gPos, line.getValue().getKey());
                    else view3D.getToSelect().setNextInputPos(0, line.getValue().getKey());
                    updateStrongBox(view3D);
                    view3D.getWarehouse().setSelectable(toSelect, view3D);
                    if (view3D.getToSelect().getPointedResource().isEmpty()){
                        if (view3D.mode.equals(BoardView3D.Mode.SELECT_CARD_SHOP)) {
                            CardShopViewBuilder.sendResourcesToBuy(view3D.getToSelect().getChosenInputPos());
                        } else if (view3D.mode.equals(BoardView3D.Mode.SELECT_RESOURCE_FOR_PROD)) {
                            ProductionViewBuilder.sendChosenResources(toSelect.getChosenInputPos(),toSelect.getChosenOutputRes());
                        }
                    }
                }
            });

            addedLines++;
        }
    }

    private static class StrongLine {
        Shape3D shape;
        ResourceGUI res;
        Text3D resNum,selected;

    }

    private static final Map<Integer, StrongLine> strongMap = new HashMap<>();
    private static Group strongBoxGroup = null;

    private void strongBoxInit(BoardView3D view3D, Group parent, Rectangle board) {
        strongBoxGroup = new Group();

        Point3D initialPos = new Point3D(200,1350,0);
        SimpleStrongBox simpleStrongBox =  getThisPlayerCache().getElem(SimpleStrongBox.class).orElseThrow();

        //                  Pos           Res                 number   selected
        final Set<Map.Entry<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>>> entries = simpleStrongBox.getResourceMap().entrySet();
        final double lineHeight = 150;
        int addedLines = 0;
        for (Map.Entry<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>> line : entries) {
            Group lineGroup = new Group();

            final double shiftX = lineHeight * (addedLines - (addedLines < 2 ? 0 : 2));
            final double shiftY = lineHeight * (addedLines < 2 ? 0 : 1);
            StrongLine sLine = new StrongLine();

            sLine.resNum = addTextOnGroup(lineGroup, 0, SimpleStrongBox.numAndSel(line).getKey().toString());

            ResourceGUI resTest = ResourceGUI.fromAsset(line.getValue().getKey());
            sLine.res = resTest;

            sLine.shape = view3D.addAndGetShape(strongBoxGroup,strongBoxGroup,resTest,new Point3D(shiftX,shiftY,0));

            long numOfSelected =  SimpleStrongBox.numAndSel(line).getValue();

            sLine.selected = addTextOnGroup(lineGroup, 35, String.valueOf(numOfSelected));

            lineGroup.setTranslateX(shiftX);
            lineGroup.setTranslateY(shiftY);
            strongBoxGroup.getChildren().add(lineGroup);
            strongMap.put(line.getKey(),sLine);

            addedLines++;
        }

        view3D.addNodeToParent(parent, board, strongBoxGroup, initialPos);
        view3D.setStrongBox(this);
    }

    private static void addDiscardButton(BoardView3D view3D, Group discardBoxGroup, SimpleDiscardBox simpleDiscardBox, int addedLines) {
        if (simpleDiscardBox.isDiscardable()) {
            Button discardButton = new Button();
            discardButton.setText("Discard");
            discardButton.setLayoutX(-80);
            discardButton.setLayoutY(150.0 * addedLines);
            discardButton.setTranslateZ(-15);
            discardButton.setPrefHeight(80);
            discardButton.setPrefWidth(200);
            discardButton.setStyle("-fx-font-size:30");

            discardButton.setOnAction(e -> {
                if (simpleDiscardBox.isDiscardable()) {
                    view3D.setMode(BoardView3D.Mode.BACKGROUND);
                    ResourceMarketViewBuilder.sendDiscard();
                }
            });

            discardBoxGroup.getChildren().add(discardButton);
        }
    }


}
