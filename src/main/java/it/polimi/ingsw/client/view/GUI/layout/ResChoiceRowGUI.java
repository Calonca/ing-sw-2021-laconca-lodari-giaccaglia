package it.polimi.ingsw.client.view.GUI.layout;

import it.polimi.ingsw.client.view.GUI.board.Text3D;
import it.polimi.ingsw.client.view.GUI.util.ResourceGUI;
import it.polimi.ingsw.client.view.abstractview.ResChoiceRow;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.scene.Group;
import javafx.scene.shape.Shape3D;

import java.util.List;

public class ResChoiceRowGUI extends ResChoiceRow {

    private final int lineHeight = 150;
    private final Group rowGroup;

    public ResChoiceRowGUI(int arrowPos, List<ResourceAsset> in, List<ResourceAsset> out) {
        super(arrowPos, in, out);
        rowGroup = new Group();
        update();
    }

    public Group getRowGroup() {
        return rowGroup;
    }

    @Override
    public void setNextInputPos(int pos, ResourceAsset res) {
        super.setNextInputPos(pos, res);
        update();
    }

    private void update(){
        clear();
        int addedLines = 0;
        addedLines = addRes(rowGroup, addedLines, in);
        final int dividerHeight = lineHeight*addedLines;
        addedLines++;
        addRes(rowGroup, addedLines, out);
        if (!in.isEmpty())
            Text3D.addTextOnGroup(rowGroup,dividerHeight,-200,"inputs/outputs");

        //final int numOfOut = numOfOutputChoices();
        //if (numOfOut>0) {
        //    ResourceGUI res = ResourceGUI.TO_CHOSE;
        //    for (int i =0;i<numOfOut;i++){
        //        Shape3D s = res.generateShape();
        //        s.setTranslateY(lineHeight * addedLines);
        //        list.getChildren().add(s);
        //        addedLines++;
        //    }
        //}
        if (getPointedResource().isPresent()) {
            rowGroup.getChildren().set(arrowPos, getSelectedRes((Shape3D) rowGroup.getChildren().get(arrowPos)));
        }
    }

    private Shape3D getSelectedRes(Shape3D oldRes){
        ResourceGUI r = ResourceGUI.fromAsset(getPointedResource().orElse(ResourceAsset.EMPTY));
        Shape3D toR = r.generateShape();
        toR.setTranslateX(oldRes.getTranslateX());
        toR.setTranslateY(oldRes.getTranslateY());
        toR.setTranslateZ(oldRes.getTranslateZ());
        ResourceGUI.setColor(r,toR,true,false);
        return toR;
    }

    private int addRes(Group list,int addedLines, List<ResourceAsset> inOrOut) {
        for (ResourceAsset r : inOrOut) {
            ResourceGUI res = ResourceGUI.fromAsset(r);
            Shape3D s = res.generateShape();
            s.setTranslateY(lineHeight * addedLines);
            list.getChildren().add(s);
            addedLines++;
        }
        return addedLines;
    }

    public void clear(){
        rowGroup.getChildren().clear();
    }

}
