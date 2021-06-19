package it.polimi.ingsw.client.view.GUI.board;

import javafx.scene.Group;
import javafx.scene.text.Text;

public class Text3d {

    public static Text from(String s){
        Text text = new Text(s);
        text.setStyle("-fx-font-size: 40;");
        text.setCache(true);
        text.setMouseTransparent(true);
        return text;
    }

    public static Text from(int i){
        return Text3d.from(String.valueOf(i));
    }


    public static void addTextOnGroup(Group lineGroup, int shifty, String num) {
        Text text = Text3d.from(num);
        text.setTranslateZ(-80);
        text.setLayoutY(shifty);
        lineGroup.getChildren().add(text);
    }

}
