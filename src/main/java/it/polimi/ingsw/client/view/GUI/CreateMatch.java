package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The user will be asked to insert a nickname and the number of players
 */
public class CreateMatch extends CreateJoinLoadMatchViewBuilder implements GUIView {

    public AnchorPane createPane;
    @FXML
    private StackPane connectionPane;
    @FXML
    private Button connectionButton;
    @FXML
    private TextField addressText;
    @FXML
    private TextField portText;

    @Override
    public void run() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/MatchToStart.fxml"));
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
        Circle circle1=new Circle();
        circle1.setRadius(10);
        circle1.setLayoutX(200);
        circle1.setLayoutY(200);

        FillTransition fillTransition1= new FillTransition(Duration.seconds(1.5),circle1,Color.YELLOW,Color.BLUE);
        fillTransition1.setCycleCount(Animation.INDEFINITE);
        fillTransition1.setAutoReverse(true);
        fillTransition1.play();


        TranslateTransition transition = new TranslateTransition(Duration.seconds(1.5),circle1);
        transition.setToX(200);
        transition.setToY(0);
        transition.setAutoReverse(true);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.play();

        createPane.getChildren().add(circle1);



        Circle circle2=new Circle();
        circle2.setRadius(10);
        circle2.setLayoutX(300);
        circle2.setLayoutY(100);

        FillTransition filltransition2= new FillTransition(Duration.seconds(1.5),circle2,Color.RED,Color.PURPLE);
        filltransition2.setCycleCount(Animation.INDEFINITE);
        filltransition2.setAutoReverse(true);
        filltransition2.play();

        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(1.5),circle2);
        transition2.setToX(0);
        transition2.setToY(200);
        transition2.setAutoReverse(true);
        transition2.setCycleCount(Animation.INDEFINITE);
        transition2.play();



        createPane.getChildren().add(circle2);

        Circle circle3=new Circle();
        circle3.setRadius(10);
        circle3.setLayoutX(400);
        circle3.setLayoutY(200);

        FillTransition fillTransition3= new FillTransition(Duration.seconds(1.5),circle3,Color.BLUE,Color.YELLOW);
        fillTransition3.setCycleCount(Animation.INDEFINITE);
        fillTransition3.setAutoReverse(true);
        fillTransition3.play();

        TranslateTransition transition3 = new TranslateTransition(Duration.seconds(1.5),circle3);
        transition3.setToX(-200);
        transition3.setToY(0);
        transition3.setAutoReverse(true);
        transition3.setCycleCount(Animation.INDEFINITE);
        transition3.play();

        createPane.getChildren().add(circle3);

        Circle circle4=new Circle();
        circle4.setRadius(10);
        circle4.setLayoutX(300);
        circle4.setLayoutY(300);

        FillTransition filltransition4= new FillTransition(Duration.seconds(1.5),circle4,Color.PURPLE,Color.RED);
        filltransition4.setCycleCount(Animation.INDEFINITE);
        filltransition4.setAutoReverse(true);
        filltransition4.play();

        TranslateTransition transition4 = new TranslateTransition(Duration.seconds(1.5),circle4);
        transition4.setToX(0);
        transition4.setToY(-200);
        transition4.setAutoReverse(true);
        transition4.setCycleCount(Animation.INDEFINITE);
        transition4.play();

        createPane.getChildren().add(circle4);
        Client.getInstance().getStage().show();



    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(CommonData.thisMatchData))
            Platform.runLater(()->
                    {
                        Client.getInstance().changeViewBuilder(new SetupPhase());
                    }
            );

    }
}