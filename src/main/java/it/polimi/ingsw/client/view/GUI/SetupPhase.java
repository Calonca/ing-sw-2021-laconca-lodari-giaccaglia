package it.polimi.ingsw.client.view.GUI;


import it.polimi.ingsw.client.Client;


import it.polimi.ingsw.server.model.Resource;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The user will be asked to insert a nickname and the number of players
 */
public class SetupPhase extends  it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder implements GUIView {

    @FXML
    private Button b1;
    @FXML
    private Button b2;
    @FXML
    private Button b3;
    @FXML
    private Button b4;
    @FXML
    private Button confirm;
    List<Button> sceneButtons=new ArrayList<Button>();


    boolean[] selected =new boolean[4];
    Spinner<Integer> goldSpin;
    Spinner<Integer> slaveSpin;
    Spinner<Integer> stoneSpin;
    Spinner<Integer> shieldSpin;
    List<Spinner<Integer>> sceneSpinners=new ArrayList<Spinner<Integer>>();
    @FXML
    private AnchorPane cjlAnchor;


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

    public Spinner<Integer> colorAndAnimate(String color ,int height, int x)
    {
        Spinner<Integer> spin=new Spinner<>(0,3,0);
        spin.setPadding(new Insets(10,10,10,10));
        spin.setBackground(Background.EMPTY);
        spin.styleProperty().set(color);
        spin.setLayoutY(height);
        spin.setLayoutX(x);
        spin.setOnMouseClicked( p ->
        {
            Circle circle2=new Circle();
            circle2.setRadius(10);
            circle2.setLayoutX(x);
            circle2.setLayoutY(height);

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

        return spin;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        int height=200;
        int x=120;
        Arrays.fill(selected, false);


        goldSpin=colorAndAnimate(" -fx-background-color: #B8860B",height,x);
        cjlAnchor.getChildren().add(goldSpin);


        x+=200;
        slaveSpin=colorAndAnimate("  -fx-background-color: #0099FF",height,x);
        cjlAnchor.getChildren().add(slaveSpin);


        x+=200;
        shieldSpin=colorAndAnimate(" -fx-background-color: #DEB887",height,x);
        cjlAnchor.getChildren().add(shieldSpin);

        x+=200;
        stoneSpin=colorAndAnimate(" -fx-background-color: #9400D3",height,x);
        cjlAnchor.getChildren().add(stoneSpin);

        sceneSpinners.add(goldSpin);
        sceneSpinners.add((slaveSpin));
        sceneSpinners.add(shieldSpin);
        sceneSpinners.add(stoneSpin);

        ImageView temp;

        sceneButtons.add(b1);
        sceneButtons.add(b2);
        sceneButtons.add(b3);
        sceneButtons.add(b4);

        for (Button sceneButton : sceneButtons) {
            temp = new ImageView(new Image("assets/leaders/raw/FRONT/Masters of Renaissance_Cards_FRONT_0.png", true));
            temp.setFitHeight(200);
            temp.setFitWidth(200);
            sceneButton.setGraphic(temp);
            sceneButton.setOnAction(e ->
            {
                ImageView tempo;
                if(!selected[sceneButtons.indexOf(sceneButton)])
                {
                    tempo=new ImageView(new Image("assets/leaders/grayed out/BACK/Masters of Renaissance__Cards_BACK.png", true));
                    tempo.setFitHeight(200);
                    tempo.setFitWidth(200);
                    sceneButton.setGraphic(tempo);
                    selected[sceneButtons.indexOf(sceneButton)]=true;
                }
                else
                {
                    tempo=new ImageView(new Image("assets/leaders/raw/FRONT/Masters of Renaissance_Cards_FRONT_0.png", true));
                    tempo.setFitHeight(200);
                    tempo.setFitWidth(200);
                    sceneButton.setGraphic(tempo);
                    selected[sceneButtons.indexOf(sceneButton)]=false;
                }

            });

        }


        Client.getInstance().getStage().show();

    }

    public void handleButton(){

        for(int i=0;i<sceneSpinners.size();i++)
            System.out.println(sceneSpinners.get(i).getValue() + Resource.fromInt(i).toString());
        for(int i=0;i<sceneButtons.size();i++)
            System.out.println((selected[i]+ sceneButtons.get(i).toString()));
        //Client.getInstance().changeViewBuilder(new ViewPersonalBoard());

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}