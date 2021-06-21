package it.polimi.ingsw.client.view.GUI.board;

import javafx.scene.Group;
import javafx.scene.text.Text;

public class Text3D {

    private Text text;

    private static Text3D from(String s){
        Text3D text3D = new Text3D();
        text3D.text = new Text(s);
        text3D.text.setStyle("-fx-font-size: 40;");
        text3D.text.setCache(true);
        text3D.text.setMouseTransparent(true);
        return text3D;
    }

    public void setText(String s){
        text.setText(s);
    }

    public void setText(int i){
        text.setText(String.valueOf(i));
    }

    private static Text3D from(int i){
        return Text3D.from(String.valueOf(i));
    }


    /**
     * Adds text to teh group
     * @return the newly created Text3D
     */
    public static Text3D addTextOnGroup(Group lineGroup, int shifty, String s) {
        return addTextOnGroup(lineGroup,shifty,0,s);
    }

    /**
     * Adds text to teh group
     * @return the newly created Text3D
     */
    public static Text3D addTextOnGroup(Group lineGroup, int shifty,int shiftx, String num) {
        Text3D text3D = Text3D.from(num);
        text3D.text.setTranslateZ(-80);
        text3D.text.setLayoutY(shifty);
        text3D.text.setLayoutX(shiftx);
        lineGroup.getChildren().add(text3D.text);
        return text3D;
    }

}
