package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import javafx.scene.shape.Sphere;
import javafx.util.Duration;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Waiting screen
 */
public class MatchToStart extends CreateJoinLoadMatchViewBuilder implements GUIView {

    @FXML
    private AnchorPane createPane;

    @Override
    public void run() {

        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().remove(0);
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(getRoot());
        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());

    }

    public Parent getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/MatchToStart.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return root;

    }

    /**
     * Basic loading animation
     * @param url is ignored
     * @param resourceBundle is ignored
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Sphere circle1=new Sphere();
        circle1.setRadius(10);
        circle1.setLayoutX(500);
        circle1.setLayoutY(100);

        TranslateTransition transition = new TranslateTransition(Duration.seconds(1.5),circle1);
        transition.setToX(0);
        transition.setToY(500);
        transition.setAutoReverse(true);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.play();

        createPane.getChildren().add(circle1);


        Sphere circle2=new Sphere();
        circle2.setRadius(10);
        circle2.setLayoutX(500);
        circle2.setLayoutY(600);


        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(1.5),circle2);
        transition2.setToX(0);
        transition2.setToY(-500);
        transition2.setAutoReverse(true);
        transition2.setCycleCount(Animation.INDEFINITE);
        transition2.play();



        createPane.getChildren().add(circle2);


        Sphere circle3=new Sphere();
        circle3.setRadius(10);
        circle3.setLayoutX(100);
        circle3.setLayoutY(350);


        TranslateTransition transition3 = new TranslateTransition(Duration.seconds(1.5),circle3);
        transition3.setToX(800);
        transition3.setToY(0);
        transition3.setAutoReverse(true);
        transition3.setCycleCount(Animation.INDEFINITE);
        transition3.play();

        createPane.getChildren().add(circle3);

        Sphere circle4=new Sphere();
        circle4.setRadius(10);
        circle4.setLayoutX(900);
        circle4.setLayoutY(350);

        TranslateTransition transition4 = new TranslateTransition(Duration.seconds(1.5),circle4);
        transition4.setToX(-800);
        transition4.setToY(0);
        transition4.setAutoReverse(true);
        transition4.setCycleCount(Animation.INDEFINITE);
        transition4.play();

        createPane.getChildren().add(circle4);
        getClient().getStage().show();


    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(CommonData.thisMatchData))
            Platform.runLater(()->
                    getClient().changeViewBuilder(new SetupPhase())
            );

    }
}