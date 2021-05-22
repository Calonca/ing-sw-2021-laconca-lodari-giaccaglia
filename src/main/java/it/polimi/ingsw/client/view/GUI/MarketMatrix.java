package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The user will be asked to insert a nickname and the number of players
 */
public class MarketMatrix extends CreateJoinLoadMatchViewBuilder implements GUIView {

    public Button button1;
    Sphere sphere1;

    public AnchorPane marketPane;

    public List<List<Sphere>> rows=new ArrayList<>();
    public List<List<Sphere>> columns=new ArrayList<>();


    @Override
    public void run() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/MarketMatrix.fxml"));
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



    public void generateRow(List<Sphere> row, Group root3D,double x, double y, int h){

        for(int i=0;i<4;i++)
        {
            Sphere ball=new Sphere(0.4);
            ball.translateYProperty().set(y+i*2);
            ball.translateXProperty().set(x);
            marketPane.getChildren().add(ball);
            root3D.getChildren().add(ball);

            row.add(ball);

        }
        Button button=new Button();
        button.setLayoutX(900);
        button.setLayoutY(h);
        button.setOnAction( p-> {
            for (Sphere circle : row) {

                System.out.println("X" + circle.getTranslateX()+"Y"+circle.getTranslateY()+"Z"+circle.getTranslateZ());
                TranslateTransition transition = new TranslateTransition(Duration.seconds(1.5), circle);
                transition.setToY(circle.getTranslateY()-2);
                transition.setAutoReverse(false);
                transition.play();


            }
        });
        rows.add(row);
        marketPane.getChildren().add(button);

    }

    public void generateColumns(List<List<Sphere>> rows,int h){


        for(int i=0;i<rows.get(0).size();i++)
        {
            List<Sphere> column=new ArrayList<>();

            for(int k=0;k<rows.size();k++)
                column.add(rows.get(k).get(i));

            Button button=new Button();
            button.setLayoutX(h);
            h+=125;
            button.setLayoutY(650);
            button.setOnAction( p-> {
                for (Sphere circle : column) {

                    System.out.println("X" + circle.getTranslateX()+"Y"+circle.getTranslateY()+"Z"+circle.getTranslateZ());
                    TranslateTransition transition = new TranslateTransition(Duration.seconds(1.5), circle);
                    transition.setToX(circle.getTranslateX()+2);
                    transition.setAutoReverse(false);
                    transition.play();


                }
            });

            columns.add(column);
            marketPane.getChildren().add(button);

        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
      /*  for(int i=0;i<4;i++)
        {
            Circle circle1=new Circle();
            circle1.setRadius(10);
            circle1.setLayoutX(225+125*i);
            circle1.setLayoutY(175);
            marketPane.getChildren().add(circle1);
            firstRow.add(circle1);

        }



        for(int i=0;i<4;i++)
        {
            Circle circle1=new Circle();
            circle1.setRadius(10);
            circle1.setLayoutX(225+125*i);
            circle1.setLayoutY(325);
            marketPane.getChildren().add(circle1);

        }

        for(int i=0;i<4;i++)
        {
            Circle circle1=new Circle();
            circle1.setRadius(10);
            circle1.setLayoutX(225+125*i);
            circle1.setLayoutY(475);
            marketPane.getChildren().add(circle1);

        }


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



        marketPane.getChildren().add(circle2);

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

        marketPane.getChildren().add(circle3);

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

        marketPane.getChildren().add(circle4);
        */

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(new Rotate(90,Rotate.Z_AXIS),new Rotate(0,Rotate.X_AXIS), new Rotate(0,Rotate.Y_AXIS), new Translate(0, 0, -20));
        camera.translateXProperty().set(-1.0);
        camera.translateYProperty().set(-0.5);


        Group root3D = new Group(camera);


        generateRow(new ArrayList<Sphere>(),root3D,1.5,-4.5,175);
        generateRow(new ArrayList<Sphere>(),root3D,-0.5,-4.5,325);
        generateRow(new ArrayList<Sphere>(),root3D,-2.5,-4.5,475);

        generateColumns(rows,225);


        SubScene subScene = new SubScene(root3D, 1000, 700, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.AQUAMARINE);
        subScene.setCamera(camera);

        marketPane.getChildren().add(0,subScene);
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