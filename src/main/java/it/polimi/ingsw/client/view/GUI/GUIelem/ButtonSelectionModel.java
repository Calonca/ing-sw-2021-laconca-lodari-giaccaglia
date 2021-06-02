package it.polimi.ingsw.client.view.GUI.GUIelem;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.model.Resource;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.List;

public class ButtonSelectionModel {

    boolean moving=false;
    boolean transferring=false;    boolean isLeader=false;
    boolean isMarket=false;
    boolean isDevelop=false;
    boolean isProduction=false;
    Resource tomove;
    Resource totransfer;

    boolean isTransferring()
    {
        return transferring;
    }


    public void resourceSelector(List<Integer> selected, List<Button> sceneResourcesToChoose, int maxselection)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();

        for (Button sceneButton : sceneResourcesToChoose) {

            sceneButton.setOnAction(e ->
            {
                int currentSelected=0;
                {
                    for (Integer integer : selected) currentSelected += integer;
                    if(currentSelected<maxselection)
                        selected.set(sceneResourcesToChoose.indexOf(sceneButton),selected.get(sceneResourcesToChoose.indexOf(sceneButton))+1);


                }
            });

        }}

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

    public void bindToMove(List<Boolean> selected,List<ResourceButton> resourcesToMove)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();


        for (ResourceButton sceneButton : resourcesToMove) {


            //sceneButton.setGraphic(temp);
            sceneButton.setOnAction(e ->
            {
                {
                    if(isTransferring())
                    {
                        transferring=false;
                        //disableFalseEnableTrue(selected,resourcesToMove);
                        selected.set(resourcesToMove.indexOf(sceneButton),true);
                        //disableFalseEnableTrue(selected,resourcesToMove);
                        sceneButton.setResource(totransfer);
                        totransfer=Resource.EMPTY;
                        sceneButton.color();
                    }
                    else
                    if(sceneButton.getResource()!=Resource.EMPTY)
                        if(!moving)
                        {
                            moving=true;
                            selected.set(resourcesToMove.indexOf(sceneButton),false);
                            //disableTrueEnableFalse(selected,resourcesToMove);
                            highlightFalse(selected,resourcesToMove);
                            tomove=sceneButton.getResource();
                            sceneButton.setResource(Resource.EMPTY);
                            sceneButton.color();
                        }
                        else
                            System.out.println("YOU ALREADY MOVING!!");
                    else
                        if(!moving)
                            System.out.println("DONT SELECT AN EMPTY CELL!");
                        else
                        {
                            moving=false;
                            //disableFalseEnableTrue(selected,resourcesToMove);
                            selected.set(resourcesToMove.indexOf(sceneButton),true);
                            deHighlightFalse(selected,resourcesToMove);
                            //disableFalseEnableTrue(selected,resourcesToMove);
                            sceneButton.setResource(tomove);
                            tomove=Resource.EMPTY;
                            sceneButton.color();
                        }


                }
            });

        }}

    public void bindToTransfer(List<ResourceButton> resourcesToTransfer)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();


        for (ResourceButton sceneButton : resourcesToTransfer) {


            //sceneButton.setGraphic(temp);
            sceneButton.setOnAction(e ->
            {
                {

                    if(moving)
                        return;
                    if(sceneButton.getResource()!=Resource.EMPTY)
                        if(!transferring)
                        {
                            transferring=true;
                            totransfer=sceneButton.getResource();
                            sceneButton.setResource(Resource.EMPTY);
                            sceneButton.color();
                        }
                        else
                            System.out.println("YOU ALREADY TRANSFERRING!!");
                    else
                    if(!transferring)
                        System.out.println("DONT SELECT AN EMPTY CELL!");
                    else
                    {
                        transferring=false;
                        //disableFalseEnableTrue(selected,resourcesToMove);
                        //disableFalseEnableTrue(selected,resourcesToMove);
                        sceneButton.setResource(totransfer);
                        totransfer=Resource.EMPTY;
                        sceneButton.color();
                    }


                }
            });

        }}

    public void disableFalseEnableTrue(List<Boolean> selected,List<ResourceButton> resourcesToMove)
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

    public void highlightFalse(List<Boolean> selected,List<ResourceButton> resourcesToMove)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();


        for (ResourceButton sceneButton : resourcesToMove) {


            if(!selected.get(resourcesToMove.indexOf(sceneButton)))
            {
                resourcesToMove.get(resourcesToMove.indexOf(sceneButton)).setStyle(" -fx-background-color: #66ff00");
            }


        };

    }


    public void highlightTrue(List<Boolean> selected,List<Button> resourcesToMove)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();


        for (Button sceneButton : resourcesToMove) {


            if(selected.get(resourcesToMove.indexOf(sceneButton)))
            {
                resourcesToMove.get(resourcesToMove.indexOf(sceneButton)).setStyle(" -fx-background-color: #000000");
            }


        };

    }

    public void dehighlightTrue(List<Boolean> selected,List<Button> resourcesToMove)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();


        for (Button sceneButton : resourcesToMove) {


            if(!selected.get(resourcesToMove.indexOf(sceneButton)))
            {
                resourcesToMove.get(resourcesToMove.indexOf(sceneButton)).setStyle(" -fx-background-color: #ffffff");
            }


        };

    }

    public void deHighlightFalse(List<Boolean> selected,List<ResourceButton> resourcesToMove)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();


        for (ResourceButton sceneButton : resourcesToMove) {


            if(!selected.get(resourcesToMove.indexOf(sceneButton)))
            {
                resourcesToMove.get(resourcesToMove.indexOf(sceneButton)).setStyle(" -fx-background-color: #ffffff");
            }


        };

    }
    public void disableTrueEnableFalse(List<Boolean> selected,List<ResourceButton> resourcesToMove)
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


    public void initializeFalseOnEmpty(List<Boolean> selected,List<ResourceButton> resourcesToMove)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();


        for (ResourceButton sceneButton : resourcesToMove) {


            if(sceneButton.getResource()!= Resource.EMPTY)
            {
                selected.set(resourcesToMove.indexOf(sceneButton),true);
            }
            else
            {
                selected.set(resourcesToMove.indexOf(sceneButton),false);
            }

        };

    }


    public void setLeader(boolean leader) {
        isLeader = leader;
    }

    public void setMarket(boolean market) {
        isMarket = market;
    }

    public void setDevelop(boolean develop) {
        isDevelop = develop;
    }

    public void setProduction(boolean production) {
        isProduction = production;
    }



    public boolean isProduction() {
        return isProduction;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public boolean isMarket() {
        return isMarket;
    }

    public boolean isDevelop() {
        return isDevelop;
    }



}

