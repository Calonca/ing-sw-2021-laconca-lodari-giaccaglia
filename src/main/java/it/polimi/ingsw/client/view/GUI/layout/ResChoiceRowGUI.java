package it.polimi.ingsw.client.view.GUI.layout;

import it.polimi.ingsw.client.view.GUI.board.Text3d;
import it.polimi.ingsw.client.view.GUI.util.ResourceGUI;
import it.polimi.ingsw.client.view.abstractview.ResChoiceRow;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.scene.Group;
import javafx.scene.shape.Shape3D;

import java.util.List;

public class ResChoiceRowGUI extends ResChoiceRow {

    private final int lineHeight = 150;

    public ResChoiceRowGUI(int arrowPos, List<ResourceAsset> in, List<ResourceAsset> out) {
        super(arrowPos, in, out);
    }

    private Group rowGroup;

    public Group buildGroup(){
        Group list = new Group();
        int addedLines = 0;
        addedLines = addRes(list, addedLines, in);
        Text3d.addTextOnGroup(list,lineHeight*addedLines,"inputs/outputs");
        addedLines++;
        addRes(list, addedLines, out);

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
        rowGroup=list;
        if (getPointedResource().isPresent())
            list.getChildren().set(arrowPos,getSelectedRes());
        return rowGroup;
    }

    private Shape3D getSelectedRes(){
        ResourceGUI r = ResourceGUI.fromAsset(getPointedResource().orElse(ResourceAsset.EMPTY));
        Shape3D toR = r.generateShape();
        ResourceGUI.setColor(r,toR,true,false);
        return toR;
    }

    private int addRes(Group list,int addedLines, List<ResourceAsset> inOrOut) {
        for (ResourceAsset r : in) {
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
