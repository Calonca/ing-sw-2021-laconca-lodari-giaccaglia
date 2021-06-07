package it.polimi.ingsw.client.view.GUI;


import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.ChooseLineEvent;
import javafx.animation.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The market is generated via a subscene that is attached to the main scene
 */
public class ResourceMarketGUI extends ResourceMarketViewBuilder implements GUIView {


    Text error=new Text("NOT ALLOWED RIGHT NOW.");
    boolean selected=false;
    public Sphere toPut;

    public int ROWSIZE=4;
    public int ROWNUMBER=3;

    public double ballsize=0.75;
    int h=70;


    double toPutStartingY=-5+ROWSIZE*ballsize*2;
    double toPutStartingX;

    public double width=300;
    public double len=260;

    public AnchorPane marketPane;


    public List<List<Sphere>> rows=new ArrayList<>();


    /**
     * This runnable simply enables the market for the user to choose
     */
    @Override
    public void run() {

        ViewPersonalBoard.getController().setMarket(true);
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

        return new SubScene(root,width,len);

    }


    public void addMatrix() {
        SubScene toadd=getRoot();
        toadd.setTranslateX(350);
        toadd.setTranslateY(-250);
        toadd.setId("MARKET");

        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(toadd);
        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());

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

    public boolean isUserAllowed()
    {

        if(!ViewPersonalBoard.getController().isMarket()||selected)
        {
            error.setOpacity(1);
            return false;
        }
        else
        {
            selected=true;
            ViewPersonalBoard.getController().isMarket(false);
            return true;
        }
    }

    /**
     * Given a starting position, using the ROWSIZE, a list of lists (rows) is created
     * @param root3D is not null
     * @param x is not null, coordinate
     * @param y is not null, coordinate
     * @param k index
     */
    public void generateRow(Group root3D,double x, double y, int k){


        List<Sphere> row=new ArrayList<>();
        for(int i=0;i<ROWSIZE;i++)
        {
            Sphere ball=new Sphere(ballsize);
            ball.translateYProperty().set(y+i*2*ballsize);
            ball.translateXProperty().set(x);
            root3D.getChildren().add(ball);
            row.add(ball);

        }
        Button button=new Button();
        button.setLayoutX(width-30);
        button.setLayoutY(k*30+h);
        button.setOnAction( p-> {

            if(!isUserAllowed())
                return;
            error.setOpacity(0);

            selected=true;

            moveX(toPut,x,new Duration(0));
            moveY(toPut,toPut.getTranslateY()-2*ballsize,new Duration(700));


            for (Sphere circle : row) {

                moveY(circle,circle.getTranslateY()-ballsize*2,new Duration(700));
            }
            Sphere toswap=row.get(0);
            row.remove(0);
            row.add(toPut);
            toPut=toswap;

            moveX(toPut,4*ballsize,new Duration(1700));

            moveY(toPut,toPutStartingY,new Duration(3000));

            moveX(toPut,toPutStartingX,new Duration(3700));



            ChooseLineEvent event=new ChooseLineEvent(k);
            getClient().getServerHandler().sendCommandMessage(new EventMessage(event));




        });
        rows.add(row);
        marketPane.getChildren().add(button);

    }

    /**
     * Given a list of rows, it will be explored traversally to create a list of lists(columns)
     * @param rows is not null
     * @param h is not null, coordinate
     */
    public void generateColumns(List<List<Sphere>> rows,int h){



        for(int i=0;i<rows.get(0).size();i++)
        {




            Button button=new Button();
            button.setLayoutX(h);
            h+=30;
            button.setLayoutY(250);

            setBut(button,i);
            marketPane.getChildren().add(button);

        }


    }

    public void setBut(Button but,int i)
    {
        but.setOnAction( p-> {

            if(!isUserAllowed())
                return;

            error.setOpacity(0);

            moveY(toPut,toPut.getTranslateY()-(ROWSIZE-i)*ballsize*2,new Duration(0));
            moveX(toPut,toPut.getTranslateX()+2*ballsize,new Duration(700));


            ArrayList<Sphere> column= new ArrayList<>();
            for (List<Sphere> row : rows) column.add(row.get(i));

            for (Sphere circle : column) {


                moveX(circle,circle.getTranslateX()+ballsize*2,new Duration(700));



            }
            Sphere temp=column.get(0);
            column.remove(0);
            column.add(toPut);
            toPut=temp;
            moveY(toPut,toPutStartingY,new Duration(1400));
            moveX(toPut,toPutStartingX,new Duration(2200));

            ChooseLineEvent event=new ChooseLineEvent(i+3);
            getClient().getServerHandler().sendCommandMessage(new EventMessage(event));





        });


    }

    /**
     * This method dinamically generates a market matrix, colors it and adapts it to scene width and lenght
     * @param url is ignored
     * @param resourceBundle is ignored
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {




        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(new Rotate(90,Rotate.Z_AXIS),new Rotate(0,Rotate.X_AXIS), new Rotate(0,Rotate.Y_AXIS), new Translate(0, 0, -20));
        camera.translateXProperty().set(0);
        camera.translateYProperty().set(-2.0);
        camera.setTranslateZ(4);



        Group root3D = new Group(camera);





        double x=ballsize*2;
        for(int i=0;i<ROWNUMBER;i++)
        {
            System.out.println(Arrays.toString(getMarketMatrix()[i]));
            generateRow(root3D,x,-5,i);
            x-=2*ballsize;
        }

        toPutStartingX=x;

        toPut=new Sphere(ballsize);
        toPut.translateYProperty().set(toPutStartingY);
        toPut.translateXProperty().set(toPutStartingX);
        root3D.getChildren().add(toPut);

        List<Color> colors=new ArrayList<>(){{
            add(Color.WHITE);
            add(Color.BLUE);
            add(Color.GREY);
            add(Color.GOLD);
            add(Color.PURPLE);
            add(Color.RED);
            add(Color.BLACK);
        }};

        toPut.setMaterial(new PhongMaterial(colors.get(getSimpleMarketBoard().getSlideMarble().ordinal())));

        for(int i=0;i<ROWNUMBER;i++)
            for(int j=0;j<ROWSIZE;j++)
            {
                rows.get(i).get(j).setMaterial(new PhongMaterial(colors.get(getMarketMatrix()[i][j].ordinal())));

            }

        generateColumns(rows,70);


        SubScene subScene = new SubScene(root3D, 300, 260, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.AQUAMARINE);
        subScene.setCamera(camera);

        marketPane.getChildren().add(0,subScene);
        error.setOpacity(0);
        error.setLayoutX(width/2);
        error.setLayoutY((len*4)/5);
        marketPane.getChildren().add(error);
        getClient().getStage().show();
    }

    /**
     * Called every time a resource is moved in the personalBoard or when the discardBox is received
     */
    @Override
    public void choosePositions() {

    }

}