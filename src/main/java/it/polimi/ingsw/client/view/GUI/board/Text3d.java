package it.polimi.ingsw.client.view.GUI.board;

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


}
