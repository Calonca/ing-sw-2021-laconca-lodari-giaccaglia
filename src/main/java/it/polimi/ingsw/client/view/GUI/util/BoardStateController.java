package it.polimi.ingsw.client.view.GUI.util;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BoardStateController {

    public void setMoving(boolean moving) {
        this.moving = moving;
    }
    boolean moving=false;
    boolean transferring=false;
    boolean movingCard=false;
    int[] allowedRes=new int[]{0,0,0,0};
    ImageView boughtCard;
    boolean isLeader=false;
    boolean isMarket=false;
    boolean isDevelop=false;
    boolean isProduction=false;

    DevelopmentCard bought;

    public void setAllowedRes(int[] allowedRes) {
        this.allowedRes = allowedRes;
    }


    public ImageView getBoughtCard() {
        return boughtCard;
    }

    public void setBoughtCard(ImageView boughtCard) {
        this.boughtCard = boughtCard;
    }



    public void cardSelector(List<Boolean> selected,List<Button> scenesLeadersToChoose,int maxselection)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();


        for (Button sceneButton : scenesLeadersToChoose) {


            //sceneButton.setGraphic(temp);
            sceneButton.setOnAction(e ->
            {
                int booleanCount=0;
                for (Boolean aBoolean : selected)
                    if (aBoolean)
                        booleanCount++;
                {
                    if(movingCard)
                    {
                        //todo get observer on productionboard to update board
                    }
                    else if(!selected.get(scenesLeadersToChoose.indexOf(sceneButton)))
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





    public void cardSelectorFromImage(List<Boolean> selected,List<ImageView> scenesLeadersToChoose,int maxselection)
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


    public void disableFalseEnableTrue(List<Boolean> selected,List<Button> resourcesToMove)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();


        for (Button sceneButton : resourcesToMove) {


                    if(!selected.get(resourcesToMove.indexOf(sceneButton)))
                    {
                        resourcesToMove.get(resourcesToMove.indexOf(sceneButton)).setDisable(true);
                    }
                    else
                    {
                        resourcesToMove.get(resourcesToMove.indexOf(sceneButton)).setDisable(false);
                    }

            };

        }

    public void bindDispenser(List<Integer> control,List<Button> selected,int maxSelection)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();
        Button temp;

        for (int i=0; i<selected.size();i++)
        {

            selected.get(i).setDisable(false);

            int finalI = i;
            if(allowedRes[finalI]==0)
            {
                selected.get(finalI).setDisable(true);
            }
            selected.get(i).setOnAction(p ->
                {
                    int currentSelected=0;

                    for (Integer integer : control) currentSelected += integer;
                    if(currentSelected==maxSelection)
                        return;
                    if(allowedRes[finalI]==0)
                    {
                        selected.get(finalI).setDisable(true);
                        return;
                    }
                    else
                    {
                        allowedRes[finalI]--;
                        control.set(finalI,control.get(finalI)+1);
                    }

                    if(!moving)
                    {
                        transferring=true;
                       // totransfer=selected.get(finalI).getResource();
                    }
                    else
                    {
                        System.out.println("PUT YOUR RESOURCE BACK FIRST!");
                    }
                });
        }
    }


    public void bindDispenserFromImage(List<Integer> control,List<ImageView> selected,int maxSelection)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();
        Button temp;

        for (int i=0; i<selected.size();i++)
        {

            selected.get(i).setDisable(false);

            int finalI = i;
            if(allowedRes[finalI]==0)
            {
                selected.get(finalI).setDisable(true);
            }
            selected.get(i).setOnMouseClicked(p ->
            {
                int currentSelected=0;

                for (Integer integer : control) currentSelected += integer;
                if(currentSelected==maxSelection)
                    return;
                if(allowedRes[finalI]==0)
                {
                    selected.get(finalI).setDisable(true);
                    return;
                }
                else
                {
                    allowedRes[finalI]--;
                    control.set(finalI,control.get(finalI)+1);
                }

                if(!moving)
                {
                    transferring=true;
                    // totransfer=selected.get(finalI).getResource();
                }
                else
                {
                    System.out.println("PUT YOUR RESOURCE BACK FIRST!");
                }
            });
        }
    }

    public void highlightFalse(List<Boolean> selected,List<Button> resourcesToMove)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();


        for (Button sceneButton : resourcesToMove) {


            if(!selected.get(resourcesToMove.indexOf(sceneButton)))
            {
                resourcesToMove.get(resourcesToMove.indexOf(sceneButton)).setStyle(" -fx-background-color: #66ff00");
            }


        };

    }


    public void highlightTrue(List<Boolean> selected,List<ImageView> elementsToHighlight)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();


        for (ImageView sceneButton : elementsToHighlight) {


            if(selected.get(elementsToHighlight.indexOf(sceneButton)))
            {
                elementsToHighlight.get(elementsToHighlight.indexOf(sceneButton)).setStyle(" -fx-background-color: #fefe33;     -fx-border-radius: 1 ;");
            }


        };

    }

    public void dehighlightTrue(List<Boolean> selected,List<ImageView> resourcesToMove)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();


        for (ImageView sceneButton : resourcesToMove) {


            if(!selected.get(resourcesToMove.indexOf(sceneButton)))
            {
                resourcesToMove.get(resourcesToMove.indexOf(sceneButton)).setStyle(" -fx-background-color: transparent");
            }


        };

    }

    public void deHighlightFalse(List<Boolean> selected,List<Button> resourcesToMove)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();


        for (Button sceneButton : resourcesToMove) {


            if(!selected.get(resourcesToMove.indexOf(sceneButton)))
            {
                resourcesToMove.get(resourcesToMove.indexOf(sceneButton)).setStyle(" -fx-background-color: transparent");
            }


        };

    }
    public void disableTrueEnableFalse(List<Boolean> selected,List<Button> resourcesToMove)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();


        for (Button sceneButton : resourcesToMove) {


            if(selected.get(resourcesToMove.indexOf(sceneButton)))
            {
                resourcesToMove.get(resourcesToMove.indexOf(sceneButton)).setDisable(true);
            }
            else
            {
                resourcesToMove.get(resourcesToMove.indexOf(sceneButton)).setDisable(false);
            }

        };

    }





    public void setLeader(boolean leader) {
        isLeader = leader;
    }

    public void setMarket(boolean market) {
        isMarket = market;
    }

    public void isCardShopOpen(boolean develop) {
        isDevelop = develop;
    }

    public void setProduction(boolean production) {
        isProduction = production;
    }



    public boolean isProduction() {
        return isProduction;
    }

    public void isProduction(boolean b) {
       isProduction=b;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public boolean isMarket() {
        return isMarket;
    }

    public void isMarket(boolean b) {
        isMarket=b;
    }

    public boolean isCardShopOpen() {
        return isDevelop;
    }



    public DevelopmentCard getBought() {
        return bought;
    }

    public void setBought(DevelopmentCard bought) {
        this.bought = bought;
    }



}

