package it.polimi.ingsw.client.view.GUI.GUIelem;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ButtonSelectionModel {

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isMoving() {
        return moving;
    }

    boolean moving=false;
    boolean transferring=false;
    boolean movingCard=false;
    int[] allowedRes=new int[]{0,0,0,0};

    boolean isLeader=false;
    boolean isMarket=false;
    boolean isDevelop=false;
    boolean isProduction=false;
    Resource tomove;
    Resource totransfer;
    DevelopmentCard bought;
    boolean isTransferring()
    {
        return transferring;
    }

    public void setAllowedRes(int[] allowedRes) {
        this.allowedRes = allowedRes;
    }


    public void bindForPayment(List<ResourceButton> scenePaymentButtons,List<Boolean> scenePaidButtons)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();

        for (ResourceButton pay : scenePaymentButtons) {

            if(pay.getResource()==Resource.EMPTY)
            {
                scenePaidButtons.set(scenePaymentButtons.indexOf(pay),false);
                pay.setDisable(true);
            }
            pay.color();
            pay.setOnAction(e ->
            {

                if (moving)
                {
                    moving=false;
                    tomove=Resource.EMPTY;
                    scenePaidButtons.set(scenePaymentButtons.indexOf(pay),false);
                    pay.setDisable(true);

                }
                for (ResourceButton pay2 : scenePaymentButtons)
                    if(!pay2.isDisabled())
                        return;
                movingCard=true;


            });

        }
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
                            //highlightFalse(selected,resourcesToMove);
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

    public void setProductionIn(List<Boolean> selected,List<ResourceButton> toggled)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();
        for(ResourceButton tog : toggled)
        {

            tog.setOnAction(p ->
            {

                if(moving)
                {
                    if(tog.getResource()==Resource.TOCHOOSE)
                    {
                    moving=false;
                    //disableFalseEnableTrue(selected,resourcesToMove);
                    selected.set(toggled.indexOf(tog),true);
                    //disableFalseEnableTrue(selected,resourcesToMove);
                    tog.setResource(tomove);
                    tomove=Resource.EMPTY;
                    tog.color();
                    }
                    else
                        if(tog.getResource()==tomove)
                        {
                            moving=false;
                            //disableFalseEnableTrue(selected,resourcesToMove);
                            selected.set(toggled.indexOf(tog),true);
                            //disableFalseEnableTrue(selected,resourcesToMove);
                            tog.setResource(tomove);
                            tomove=Resource.EMPTY;
                            tog.color();
                        }
                }

            });
        }


    }


    public void selectChoiceFromDispenser(List<ResourceButton> toggled, List<Boolean> selected)
    {

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();
        for(ResourceButton tog : toggled)
        {

            tog.setOnAction(p ->
            {

                if(tog.getResource()==Resource.TOCHOOSE)
                {

                    if(transferring)
                    {
                        transferring=false;
                        //disableFalseEnableTrue(selected,resourcesToMove);
                        //disableFalseEnableTrue(selected,resourcesToMove);
                        selected.set(toggled.indexOf(tog),true);
                        tog.setResource(totransfer);
                        tomove=Resource.EMPTY;
                        tog.color();
                    }

                }
                else
                if(transferring)
                    if(totransfer==tog.getResource())
                {
                    tog.setGraphic(new Text(tog.getResource().toString()));
                    transferring=false;
                    //disableFalseEnableTrue(selected,resourcesToMove);
                    //disableFalseEnableTrue(selected,resourcesToMove);
                    selected.set(toggled.indexOf(tog),true);
                    tog.setResource(totransfer);
                    tomove=Resource.EMPTY;
                    tog.color();
                }


            });
        }


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

