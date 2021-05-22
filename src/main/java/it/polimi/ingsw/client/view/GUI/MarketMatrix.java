package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
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

    public Sphere toPut;
    public int rowsize=4;
    public AnchorPane marketPane;
    double LOWER_CORNER=-4.5;
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



    public void generateRow(Group root3D,double x, double y, int h){

        List<Sphere> row=new ArrayList<>();
        for(int i=0;i<rowsize;i++)
        {
            Sphere ball=new Sphere(0.4);
            ball.translateYProperty().set(y+i*2);
            ball.translateXProperty().set(x);
            root3D.getChildren().add(ball);

            row.add(ball);

        }
        Button button=new Button();
        button.setLayoutX(900);
        button.setLayoutY(h);
        button.setOnAction( p-> {

            TranslateTransition transition = new TranslateTransition(Duration.seconds(0.25), toPut);
            transition.setToX(x);
            transition.setAutoReverse(false);
            transition.play();

            transition = new TranslateTransition(Duration.seconds(0.75), toPut);
            transition.setToY(toPut.getTranslateY()-2);
            transition.setAutoReverse(false);
            transition.setDelay(new Duration(500));
            transition.play();

            int k=rowsize*500;
            for (Sphere circle : row) {
                System.out.println("X" + toPut.getTranslateX()+"Y"+toPut.getTranslateY()+"Z"+toPut.getTranslateZ());

                transition = new TranslateTransition(Duration.seconds(0.75), circle);
                transition.setToY(circle.getTranslateY()-2);
                transition.setAutoReverse(false);
                transition.setDelay(new Duration(500+k));
                k-=500;
                transition.play();


            }


        });
        rows.add(row);
        marketPane.getChildren().add(button);

    }

    public void generateColumns(List<List<Sphere>> rows,int h){

        double y=-4.5;

        for(int i=0;i<rows.get(0).size();i++)
        {
            List<Sphere> column=new ArrayList<>();

            for(int k=0;k<rows.size();k++)
                column.add(rows.get(k).get(i));

            Button button=new Button();
            button.setLayoutX(h);
            h+=125;
            button.setLayoutY(650);

            setBut(button,y,column);

            columns.add(column);
            marketPane.getChildren().add(button);
            y+=2;

        }


    }

    public void setBut(Button but,double y, List<Sphere> column)
    {
        but.setOnAction( p-> {


            TranslateTransition transition = new TranslateTransition(Duration.seconds(0.25), toPut);
            transition.setToY(y);
            transition.setAutoReverse(false);
            transition.play();

            TranslateTransition trans = new TranslateTransition(Duration.seconds(0.75), toPut);
            trans.setToX(-2.5);
            trans.setDelay(new Duration(275));
            trans.setAutoReverse(false);
            trans.play();



            int k=rowsize*500;
            for (Sphere circle : column) {


                System.out.println("X" + toPut.getTranslateX()+"Y"+toPut.getTranslateY()+"Z"+toPut.getTranslateZ());
                transition = new TranslateTransition(Duration.seconds(0.75), circle);
                transition.setToX(circle.getTranslateX()+2);
                transition.setDelay(new Duration(k-300));
                k-=500;
                transition.setAutoReverse(false);
                transition.play();


            }

        });


    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {


        Button button=new Button();
        button.setLayoutX(900);
        button.setLayoutY(650);
        button.setOnAction(p -> Client.getInstance().changeViewBuilder(new MarketMatrix()));
        marketPane.getChildren().add(button);
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(new Rotate(90,Rotate.Z_AXIS),new Rotate(0,Rotate.X_AXIS), new Rotate(0,Rotate.Y_AXIS), new Translate(0, 0, -20));
        camera.translateXProperty().set(-1.0);
        camera.translateYProperty().set(-0.5);


        Group root3D = new Group(camera);


        toPut=new Sphere(0.4);
        toPut.translateYProperty().set(3.5);
        toPut.translateXProperty().set(LOWER_CORNER);
        root3D.getChildren().add(toPut);

        generateRow(root3D,1.5,-4.5,175);
        generateRow(root3D,-0.5,-4.5,325);
        generateRow(root3D,-2.5,-4.5,475);

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