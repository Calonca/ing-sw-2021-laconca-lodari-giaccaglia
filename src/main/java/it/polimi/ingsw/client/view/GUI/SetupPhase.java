package it.polimi.ingsw.client.view.GUI;


import it.polimi.ingsw.client.Client;


import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * The user will be asked to insert a nickname and the number of players
 */
public class SetupPhase extends  it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder implements GUIView {

    public Button b1;
    public Button b2;
    public Button b3;
    public Button b4;

    boolean[] selected =new boolean[4];
    Spinner<Integer> goldSpin;
    Spinner<Integer> slaveSpin;
    Spinner<Integer> stoneSpin;
    Spinner<Integer> shieldSpin;

    public AnchorPane cjlAnchor;


    @Override
    public void run() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/SetupMenu.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);

        getClient().getStage().setScene(scene);
        getClient().getStage().show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        int height=200;
        int x=120;
        Arrays.fill(selected, false);
        goldSpin=new Spinner<>(0,3,0);
        goldSpin.setPadding(new Insets(10,10,10,10));
        goldSpin.setBackground(Background.EMPTY);
        goldSpin.styleProperty().set(" -fx-background-color: #B8860B");
        goldSpin.setLayoutY(height);
        goldSpin.setLayoutX(x);
        cjlAnchor.getChildren().add(goldSpin);

        goldSpin.setOnMouseClicked( p ->
        {
                Circle circle2=new Circle();
        circle2.setRadius(10);
        circle2.setLayoutX(120);
        circle2.setLayoutY(200);

        FillTransition filltransition2= new FillTransition(Duration.seconds(1.5),circle2,Color.GOLD,Color.PURPLE);
        filltransition2.setCycleCount(Animation.INDEFINITE);
        filltransition2.setAutoReverse(true);
        filltransition2.play();

        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(1.5),circle2);
        transition2.setToX(0);
        transition2.setToY(-200);
        transition2.setAutoReverse(false);
        transition2.play();
        cjlAnchor.getChildren().add(circle2);

        Client.getInstance().getStage().show();
        });

        x+=200;
        slaveSpin=new Spinner<>(0,3,0);
        slaveSpin.setPadding(new Insets(10,10,10,10));
        slaveSpin.setBackground(Background.EMPTY);
        slaveSpin.styleProperty().set(" -fx-background-color: #9400D3");
        slaveSpin.setLayoutY(height);
        slaveSpin.setLayoutX(x);
        cjlAnchor.getChildren().add(slaveSpin);

        x+=200;
        shieldSpin=new Spinner<>(0,3,0);
        shieldSpin.setPadding(new Insets(10,10,10,10));
        shieldSpin.setBackground(Background.EMPTY);
        shieldSpin.styleProperty().set(" -fx-background-color: #0099FF");
        shieldSpin.setLayoutY(height);
        shieldSpin.setLayoutX(x);
        cjlAnchor.getChildren().add(shieldSpin);

        x+=200;
        stoneSpin=new Spinner<>(0,3,0);
        stoneSpin.setPadding(new Insets(10,10,10,10));
        stoneSpin.setBackground(Background.EMPTY);
        stoneSpin.styleProperty().set(" -fx-background-color: #DEB887");
        stoneSpin.setLayoutY(height);
        stoneSpin.setLayoutX(x);
        cjlAnchor.getChildren().add(stoneSpin);

        ImageView temp;

        temp=new ImageView(new Image("assets/leaders/raw/FRONT/Masters of Renaissance_Cards_FRONT_0.png", true));
        temp.setFitHeight(200);
        temp.setFitWidth(200);
        b1.setGraphic(temp);
        temp=new ImageView(new Image("assets/leaders/raw/FRONT/Masters of Renaissance_Cards_FRONT_0.png", true));
        temp.setFitHeight(200);
        temp.setFitWidth(200);
        b2.setGraphic(temp);
        temp=new ImageView(new Image("assets/leaders/raw/FRONT/Masters of Renaissance_Cards_FRONT_0.png", true));
        temp.setFitHeight(200);
        temp.setFitWidth(200);
        b3.setGraphic(temp);
        temp=new ImageView(new Image("assets/leaders/raw/FRONT/Masters of Renaissance_Cards_FRONT_0.png", true));
        temp.setFitHeight(200);
        temp.setFitWidth(200);
        b4.setOnAction(e ->
        {
            ImageView tempo=new ImageView(new Image("assets/leaders/grayed out/BACK/Masters of Renaissance__Cards_BACK.png", true));
            tempo.setFitHeight(200);
            tempo.setFitWidth(200);
            b4.setGraphic(tempo);
        });
        b4.setGraphic(temp);

        b1.setOnAction(e ->
        {
            ImageView tempo;
            if(!selected[1])
            {
                tempo=new ImageView(new Image("assets/leaders/grayed out/BACK/Masters of Renaissance__Cards_BACK.png", true));
                tempo.setFitHeight(200);
                tempo.setFitWidth(200);
                b1.setGraphic(tempo);
                selected[1]=true;
            }
            else
            {
                tempo=new ImageView(new Image("assets/leaders/raw/FRONT/Masters of Renaissance_Cards_FRONT_0.png", true));
                tempo.setFitHeight(200);
                tempo.setFitWidth(200);
                b1.setGraphic(tempo);
                selected[1]=false;
            }

        });

        b2.setOnAction(e ->
        {
            ImageView tempo=new ImageView(new Image("assets/leaders/grayed out/BACK/Masters of Renaissance__Cards_BACK.png", true));
            tempo.setFitHeight(200);
            tempo.setFitWidth(200);
            b2.setGraphic(tempo);
        });

        b3.setOnAction(e ->
        {
            ImageView tempo=new ImageView(new Image("assets/leaders/grayed out/BACK/Masters of Renaissance__Cards_BACK.png", true));
            tempo.setFitHeight(200);
            tempo.setFitWidth(200);
            b3.setGraphic(tempo);
        });

        Client.getInstance().getStage().show();

    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}