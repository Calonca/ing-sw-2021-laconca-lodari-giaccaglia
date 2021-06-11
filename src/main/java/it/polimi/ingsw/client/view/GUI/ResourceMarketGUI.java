package it.polimi.ingsw.client.view.GUI;


import it.polimi.ingsw.client.view.abstractview.ProductionViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.ChooseLineEvent;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    public double ballsize=0.8;
    int rowButtonHeight=130;
    int columnButtonOffset=150;


    double ballsXOffset=-4.5;

    double toPutStartingY=ballsXOffset+ROWSIZE*ballsize*2;
    double toPutStartingX;

    public double width=400;
    public double reaWidth=550;
    public double len=320;
    public double reaLen=700;

    public AnchorPane marketPane;
    double marketTranslateX=600;
    double MarketTranslateY=-30;

    double subSceneTranslateY=75;
    double subSceneTranslateX=75;
    public List<List<Sphere>> rows=new ArrayList<>();


    /**
     * This runnable simply enables the market for the user to choose
     */
    @Override
    public void run() {
        SubScene toadd=getRoot();
        toadd.setTranslateX(marketTranslateX);
        toadd.setTranslateY(MarketTranslateY);
        toadd.setId("MARKET");

        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(toadd);
        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());
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

        return new SubScene(root,reaWidth,reaLen);

    }


    public void addMatrix() {


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
        button.setLayoutX(reaWidth-30);
        button.setLayoutY(k*80+rowButtonHeight);
        button.setOnAction( p-> {


            error.setOpacity(0);

            selected=true;

            toPut.setTranslateX(x);
            moveY(toPut,toPut.getTranslateY()-2*ballsize,new Duration(100));


            for (Sphere circle : row) {

                moveY(circle,circle.getTranslateY()-ballsize*2,new Duration(100));
            }

            Sphere toswap=row.get(0);
            row.remove(0);
            row.add(toPut);
            toPut=toswap;

            moveX(toPut,4*ballsize,new Duration(600));

            moveY(toPut,toPutStartingY,new Duration(1300));




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
            h+=80;
            button.setLayoutY(reaLen/2+70);

            setBut(button,i);
            marketPane.getChildren().add(button);

        }


    }

    public void setBut(Button but,int i)
    {
        but.setOnAction( p-> {



            error.setOpacity(0);

            toPut.setTranslateY(toPut.getTranslateY()-(ROWSIZE-i)*ballsize*2);
            toPut.setTranslateX(toPut.getTranslateX()-ballsize*8);

            moveX(toPut,toPut.getTranslateX()+2*ballsize,new Duration(100));


            ArrayList<Sphere> column= new ArrayList<>();
            for (List<Sphere> row : rows) column.add(row.get(i));

            for (Sphere circle : column) {


                moveX(circle,circle.getTranslateX()+ballsize*2,new Duration(100));



            }
            Sphere temp=column.get(0);
            column.remove(0);
            column.add(toPut);
            toPut=temp;
            moveY(toPut,toPutStartingY,new Duration(700));

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



        Group root3D = new Group();




        double x=ballsize*2;
        for(int i=0;i<ROWNUMBER;i++)
        {
            System.out.println(Arrays.toString(getMarketMatrix()[i]));
            generateRow(root3D,x,ballsXOffset,i);
            x-=2*ballsize;
        }

        toPutStartingX=x;

        toPut=new Sphere(ballsize);
        toPut.translateYProperty().set(toPutStartingY);
        toPut.translateXProperty().set(toPutStartingX+8*ballsize);
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

        generateColumns(rows,columnButtonOffset);

        SubScene slideSubscene = new SubScene(root3D, width, len, true, SceneAntialiasing.BALANCED);
        slideSubscene.setTranslateY(subSceneTranslateY);
        slideSubscene.setTranslateX(subSceneTranslateX);;
        slideSubscene.setFill(Color.TRANSPARENT);
        slideSubscene.setCamera(camera);


        marketPane.getChildren().add(slideSubscene);
        marketPane.setId("marketPane");
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