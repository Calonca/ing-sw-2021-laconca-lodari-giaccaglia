package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.GUI.GUIelem.ResourceSphere;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.server.model.Resource;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
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
public class ResourceMarketGUI extends ResourceMarketViewBuilder implements GUIView {

    boolean selected=false;
    public ResourceSphere toPut;
    public int ROWSIZE=4;
    public int ROWNUMBER=3;
    public double ballsize=0.7;

    public AnchorPane marketPane;

    double LOWER_CORNER_X=-4.5;
    double LOWER_CORNER_Y=3.5;
    public List<List<ResourceSphere>> rows=new ArrayList<>();
    public List<List<ResourceSphere>> columns=new ArrayList<>();


    @Override
    public void run() {


    }

    public SubScene getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/MarketMatrix.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SubScene(root,400,300);

    }

    public void addMatrix() {
        Node toadd=getRoot();
        toadd.setTranslateX(0);
        toadd.setTranslateY(-240);
        ///metti cio che sta nel run qui cazzone
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(toadd);
        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());

    }

    public void moveX(ResourceSphere sphere,double x, Duration duration)
    {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.75), sphere);
        transition.setToX(x);
        transition.setDelay(duration);
        transition.setAutoReverse(false);
        transition.play();
    }

    public void moveY(ResourceSphere sphere,double y, Duration duration)
    {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.75), sphere);
        transition.setToY(y);
        transition.setAutoReverse(false);
        transition.setDelay(duration);
        transition.play();
    }

    /**
     * Given a starting position, using the ROWSIZE, a list of lists (rows) is created
     * @param root3D is not null
     * @param x is not null, coordinate
     * @param y is not null, coordinate
     * @param h is not null, coordinate
     */
    public void generateRow(Group root3D,double x, double y, int h){

        List<Color> colors=new ArrayList<>();



        List<ResourceSphere> row=new ArrayList<>();
        for(int i=0;i<ROWSIZE;i++)
        {
            int res=((int) ((Math.random() * (4)) ));
            ResourceSphere ball=new ResourceSphere(ballsize, Resource.fromInt(res) );
            ball.translateYProperty().set(y+i*2*ballsize);
            ball.translateXProperty().set(x);
            ball.color();
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


            for (ResourceSphere circle : row) {

                moveY(circle,circle.getTranslateY()-2,new Duration(700));
                System.out.println("you will get a " + circle.getResource().toString());


            }
            ResourceSphere toswap=row.get(0);
            row.remove(0);
            row.add(toPut);
            toPut=toswap;
            toPut.setMaterial(new PhongMaterial(Color.BLANCHEDALMOND));

            moveX(toPut,3.5,new Duration(1700));

            moveY(toPut,LOWER_CORNER_Y,new Duration(3000));







        });
        rows.add(row);
        marketPane.getChildren().add(button);

    }

    /**
     * Given a list of rows, it will be explored traversally to create a list of lists(columns)
     * @param rows is not null
     * @param h is not null, coordinate
     */
    public void generateColumns(List<List<ResourceSphere>> rows,int h){

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


            ArrayList<ResourceSphere> column= new ArrayList<>();
            for (List<ResourceSphere> row : rows) column.add(row.get(i));

            for (ResourceSphere circle : column) {


                moveX(circle,circle.getTranslateX()+2,new Duration(700));

                System.out.println("you will get a " + circle.getResource().toString());


            }
            ResourceSphere temp=column.get(0);
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

        button.setOnAction(p -> getClient().changeViewBuilder(new ResourceMarketGUI()));

        marketPane.getChildren().add(button);


        button=new Button();
        button.setLayoutX(900);
        button.setLayoutY(100);

        button.setOnAction(p -> getClient().changeViewBuilder(new ViewPersonalBoard()));

        marketPane.getChildren().add(button);


        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(new Rotate(90,Rotate.Z_AXIS),new Rotate(0,Rotate.X_AXIS), new Rotate(0,Rotate.Y_AXIS), new Translate(0, 0, -20));
        camera.translateXProperty().set(-3.0);
        camera.translateYProperty().set(-2.0);
        camera.setTranslateZ(-5);


        Group root3D = new Group(camera);



        toPut=new ResourceSphere(ballsize,Resource.SERVANT);
        toPut.translateYProperty().set(LOWER_CORNER_Y);
        toPut.translateXProperty().set(LOWER_CORNER_X);
        toPut.setMaterial(new PhongMaterial(Color.GOLD));
        root3D.getChildren().add(toPut);

        double x=1.5;
        int h=300;
        for(int i=0;i<ROWNUMBER;i++)
        {
            generateRow(root3D,x,-10,h);
            x-=2*ballsize;
            h+=150;
        }



        generateColumns(rows,225);


        SubScene subScene = new SubScene(root3D, 1000, 700, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.AQUAMARINE);
        subScene.setCamera(camera);

        marketPane.getChildren().add(0,subScene);
        getClient().getStage().show();
    }

    /**
     * Called every time a resource is moved in the personalBoard or when the discardBox is received
     */
    @Override
    public void choosePositions() {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(CommonData.thisMatchData))
            Platform.runLater(()->
                    {
                        getClient().changeViewBuilder(new SetupPhase());
                    }
            );

    }
}