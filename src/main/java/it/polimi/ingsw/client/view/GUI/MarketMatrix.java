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
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
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
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The user will be asked to insert a nickname and the number of players
 */
public class MarketMatrix extends CreateJoinLoadMatchViewBuilder implements GUIView {

    boolean selected=false;
    public Sphere toPut;
    public int ROWSIZE=4;
    public int ROWNUMBER=3;
    public double ballsize=1.03;
    public AnchorPane marketPane;
    double LOWER_CORNER_X=-4.5;
    double LOWER_CORNER_Y=3.5;
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

    public void moveX(Sphere sphere,double x, Duration duration)
    {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.75), sphere);
        transition.setToX(x);
        transition.setDelay(duration);
        transition.setAutoReverse(false);
        transition.play();
    }

    public void moveY(Sphere sphere,double y, Duration duration)
    {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.75), sphere);
        transition.setToY(y);
        transition.setAutoReverse(false);
        transition.setDelay(duration);
        transition.play();
    }

    public void generateRow(Group root3D,double x, double y, int h){

        List<Sphere> row=new ArrayList<>();
        for(int i=0;i<ROWSIZE;i++)
        {
            Sphere ball=new Sphere(ballsize);
            ball.translateYProperty().set(y+i*2);
            ball.translateXProperty().set(x);
            ball.setMaterial(new PhongMaterial(Color.PURPLE));
            root3D.getChildren().add(ball);
            row.add(ball);

        }
        Button button=new Button();
        button.setLayoutX(900);
        button.setLayoutY(h);
        button.setOnAction( p-> {

            if(selected)
                return;
            selected=true;

            moveX(toPut,x,new Duration(0));
            moveY(toPut,toPut.getTranslateY()-2,new Duration(700));


            int k=ROWSIZE*500;
            for (Sphere circle : row) {
                System.out.println("X" + toPut.getTranslateX()+"Y"+toPut.getTranslateY()+"Z"+toPut.getTranslateZ());

                moveY(circle,circle.getTranslateY()-2,new Duration(700));

            }
            Sphere temp=row.get(0);
            row.remove(0);
            row.add(toPut);
            toPut=temp;
            toPut.setMaterial(new PhongMaterial(Color.BLANCHEDALMOND));

            moveX(toPut,3.5,new Duration(1700));

            moveY(toPut,LOWER_CORNER_Y,new Duration(3000));







        });
        rows.add(row);
        marketPane.getChildren().add(button);

    }

    public void generateColumns(List<List<Sphere>> rows,int h){

        double y=-4.5;

        for(int i=0;i<rows.get(0).size();i++)
        {



            Button button=new Button();
            button.setLayoutX(h);
            h+=125;
            button.setLayoutY(650);

            setBut(button,y,i);

            marketPane.getChildren().add(button);
            y+=2;

        }


    }

    public void setBut(Button but,double y,int i)
    {
        but.setOnAction( p-> {

            if(selected)
                return;
            selected=true;


            moveY(toPut,y,new Duration(0));
            moveX(toPut,-2.5,new Duration(700));


            ArrayList<Sphere> column= new ArrayList<>();
            for (List<Sphere> row : rows) column.add(row.get(i));

            for (Sphere circle : column) {


                System.out.println("X" + toPut.getTranslateX()+"Y"+toPut.getTranslateY()+"Z"+toPut.getTranslateZ());
                moveX(circle,circle.getTranslateX()+2,new Duration(700));

                System.out.println("you will get a " + circle.getMaterial().toString());


            }
            Sphere temp=column.get(0);
            column.remove(0);
            column.add(toPut);
            toPut=temp;
            toPut.setMaterial(new PhongMaterial(Color.BLANCHEDALMOND));
            moveX(toPut,LOWER_CORNER_X,new Duration(1000));
            moveY(toPut,LOWER_CORNER_Y,new Duration(1000));



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



        toPut=new Sphere(ballsize);
        toPut.translateYProperty().set(LOWER_CORNER_Y);
        toPut.translateXProperty().set(LOWER_CORNER_X);
        toPut.setMaterial(new PhongMaterial(Color.GOLD));
        root3D.getChildren().add(toPut);

        double x=1.5;
        int h=175;
        for(int i=0;i<ROWNUMBER;i++)
        {
            generateRow(root3D,x,-4.5,h);
            x-=2;
            h+=150;
        }



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