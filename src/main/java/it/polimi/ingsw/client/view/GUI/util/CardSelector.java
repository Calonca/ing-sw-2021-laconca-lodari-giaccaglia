package it.polimi.ingsw.client.view.GUI.util;

import javafx.scene.image.ImageView;

import java.util.List;

public class CardSelector {
    public void cardSelectorFromImage(List<Boolean> selected, List<ImageView> scenesLeadersToChoose, int maxselection)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();


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
