package it.polimi.ingsw.client.view.GUI.board;

import it.polimi.ingsw.client.view.GUI.BoardView3D;
import it.polimi.ingsw.client.view.GUI.util.DragAndDropData;
import it.polimi.ingsw.client.view.GUI.util.DragAndDropHandler;
import it.polimi.ingsw.client.view.GUI.util.ResourceGUI;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.SimpleDiscardBox;
import it.polimi.ingsw.network.simplemodel.SimpleStrongBox;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape3D;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getThisPlayerCache;

public class BoxGUI {
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
            addTextOnRes(lineGroup, 0, String.valueOf(num));

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

    public static void strongBuilder(BoardView3D view3D, Group parent, Rectangle board){
        Group strongBoxGroup = new Group();
        Point3D initialPos = new Point3D(200,1350,0);
        SimpleStrongBox simpleStrongBox =  getThisPlayerCache().getElem(SimpleStrongBox.class).orElseThrow();

        //                  Pos           Res                 number   selected
        final Set<Map.Entry<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>>> entries = simpleStrongBox.getResourceMap().entrySet();
        final double lineHeight = 150;
        int addedLines = 0;
        for (Map.Entry<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>> line : entries) {
            Group lineGroup = new Group();
            addTextOnRes(lineGroup, 0, SimpleStrongBox.numAndSel(line).getKey().toString());
            addTextOnRes(lineGroup, 35, SimpleStrongBox.numAndSel(line).getValue().toString());

            final double shiftX = lineHeight * (addedLines - (addedLines < 2 ? 0 : 2));
            lineGroup.setTranslateX(shiftX);
            final double shiftY = lineHeight * (addedLines < 2 ? 0 : 1);
            lineGroup.setTranslateY(shiftY);

            int gPos = line.getKey();

            ResourceGUI resTest = ResourceGUI.fromAsset(line.getValue().getKey());
            Shape3D shape = view3D.addAndGetShape(strongBoxGroup,strongBoxGroup,resTest,new Point3D(shiftX,shiftY,0));
            strongBoxGroup.getChildren().add(lineGroup);

            addedLines++;
        }

        view3D.addNodeToParent(parent, board, strongBoxGroup, initialPos);
        view3D.setDiscardBox(strongBoxGroup);
    }

    private static void addTextOnRes(Group lineGroup, int shifty, String num) {
        Text text = Text3d.from(num);
        text.setTranslateZ(-100);
        text.setLayoutY(shifty);
        lineGroup.getChildren().add(text);
    }

    private static void addDiscardButton(BoardView3D view3D, Group discardBoxGroup, SimpleDiscardBox simpleDiscardBox, int addedLines) {
        Text discardText = Text3d.from("Discard");
        discardText.setLayoutY(150.0 * addedLines);
        discardText.setMouseTransparent(true);
        Rectangle r = new Rectangle(200,50, Color.BLUE);
        r.setTranslateZ(-200);
        r.setLayoutY((150.0 * addedLines)-90);
        r.setOnMouseClicked(e-> {
            if (simpleDiscardBox.isDiscardable()) {
                view3D.setMode(BoardView3D.Mode.BACKGROUND);
                ResourceMarketViewBuilder.sendDiscard();
            }
        });
        r.setOnMouseEntered(e-> {
            if (simpleDiscardBox.isDiscardable()) {
                r.setFill(Color.CYAN);
            }
        });
        r.setOnMouseExited(e-> {
            if (simpleDiscardBox.isDiscardable()) {
                r.setFill(Color.BLUE);
            }
        });
        discardBoxGroup.getChildren().add(r);
        discardBoxGroup.getChildren().add(discardText);
    }


}
