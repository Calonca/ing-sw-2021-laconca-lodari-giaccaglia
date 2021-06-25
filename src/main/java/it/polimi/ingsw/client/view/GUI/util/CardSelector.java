package it.polimi.ingsw.client.view.GUI.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class CardSelector {
    public void cardSelectorFromImage(List<Boolean> selected, List<ImageView> scenesLeadersToChoose, int maxselection)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();
        List<javafx.scene.image.ImageView> res=new ArrayList<>();
        res.add(new javafx.scene.image.ImageView(new Image("assets/resources/GOLD.png")));
        res.add(new javafx.scene.image.ImageView(new Image("assets/resources/SERVANT.png")));
        res.add(new javafx.scene.image.ImageView(new Image("assets/resources/SHIELD.png")));
        res.add(new javafx.scene.image.ImageView(new Image("assets/resources/STONE.png")));

        for (ImageView sceneButton : scenesLeadersToChoose) {


            //sceneButton.setGraphic(temp);
            sceneButton.setOnMouseClicked(e ->
            {
                int booleanCount=0;
                for (Boolean aBoolean : selected)
                    if (aBoolean)
                        booleanCount++;
                {
                    if(!selected.get(scenesLeadersToChoose.indexOf(sceneButton)))
                    {
                        if(booleanCount<maxselection)
                            selected.set(scenesLeadersToChoose.indexOf(sceneButton),true);
                    }
                    else
                    {
                        selected.set(scenesLeadersToChoose.indexOf(sceneButton),false);
                    }
                }
            });

        }}

}
