package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.GUI.GUIelem.ButtonSelectionModel;
import it.polimi.ingsw.client.view.GUI.GUIelem.ResourceButton;
import it.polimi.ingsw.server.model.Resource;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewPersonalBoard extends it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder implements GUIView
{
    public AnchorPane menuPane;

    List<ResourceButton> sceneResources=new ArrayList<>();
    List<Boolean> selected=new ArrayList<>();


    @Override
    public void run() {
        ((Pane)Client.getInstance().getStage().getScene().getRoot()).getChildren().remove(0);
        ((Pane)Client.getInstance().getStage().getScene().getRoot()).getChildren().add(getRoot());
        System.out.println(((Pane)Client.getInstance().getStage().getScene().getRoot()).getChildren());
    }

    public Parent getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/ViewPersonalBoard.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return root;

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ImageView tempImageView;
        tempImageView=new ImageView(new Image("assets/board/smallerboard.png", true));

        tempImageView.setFitHeight(511);
        tempImageView.setFitWidth(530);
       /* boardView.setImage(new Image("assets/board/Masters of Renaissance_PlayerBoard (11_2020)-1.png", true));
        boardView.setRotate(270);
        boardView.setFitHeight(600);
        boardView.setFitWidth(9000);*/
        StackPane.setAlignment(tempImageView, Pos.BOTTOM_CENTER);
        //menuPane.getChildren().add(0,tempImageView);
        List<Color> colors=new ArrayList<>();
        List<String> colorsToRes=new ArrayList<>();

        for(int i=0;i<4;i++)
        {
            ResourceButton tempbut= new ResourceButton();
            tempbut.setResource(Resource.fromInt(i));
            tempbut.setLayoutY(200);
            tempbut.setLayoutX(100+200*i);
            tempbut.color();

            //tempbut.setGraphic(buttonGraphic);
            menuPane.getChildren().add(tempbut);
            sceneResources.add(tempbut);

        }

        for(int i=0;i<4;i++)
        {
            ResourceButton tempbut= new ResourceButton();
            tempbut.setResource(Resource.EMPTY);
            tempbut.setLayoutY(300);
            tempbut.setLayoutX(100+200*i);
            //tempbut.setGraphic(buttonGraphic);
            menuPane.getChildren().add(tempbut);
            sceneResources.add(tempbut);

        }
        for(int i=0;i<sceneResources.size();i++)
            selected.add(false);
        ButtonSelectionModel selectionModel=new ButtonSelectionModel();
        selectionModel.initializeFalseOnEmpty(selected,sceneResources);
        //selectionModel.disableFalseEnableTrue(selected,sceneResources);
        selectionModel.bindToMove(selected,sceneResources);


        Client.getInstance().getStage().show();
    }
}
