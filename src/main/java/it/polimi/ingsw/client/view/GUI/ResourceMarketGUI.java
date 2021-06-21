package it.polimi.ingsw.client.view.GUI;


import it.polimi.ingsw.client.view.GUI.board.CamState;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;

import it.polimi.ingsw.network.assets.marbles.MarbleAsset;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.*;

/**
 * The market is generated via a subscene that is attached to the main scene
 */
public class ResourceMarketGUI extends ResourceMarketViewBuilder {


    Text error=new Text("NOT ALLOWED RIGHT NOW.");
    boolean selected=false;
    public Sphere toPut;

    public int NUMBEROFROWS =4;
    public int NUMBEROFCOLUMNS =3;

    public double ballsize=40;
    int rowButtonHeight=130;
    int columnButtonOffset=-75;


    double ballsXOffset=-4.5;

    double toPutStartingY=ballsXOffset+ NUMBEROFCOLUMNS *ballsize*2;
    double toPutStartingX;

    public boolean enabled=false;
    public double width=400;
    public double reaWidth=550;
    public double len=320;
    public double reaLen=700;

    //public AnchorPane marketPane;
    double marketTranslateX=600;
    double MarketTranslateY=-30;

    double subSceneTranslateY=75;
    double subSceneTranslateX=75;
    public List<List<Sphere>> rows=new ArrayList<>();
    public List<List<Sphere>> columns=new ArrayList<>();
    private Group root3D;
    private Group buttons;


    /**
     * This runnable simply enables the market for the user to choose
     */
    @Override
    public void run() {
        SetupPhase.getBoard().getController().setMarket(true);
        SetupPhase.getBoard().changeCamState(CamState.SEE_RESOURCE_MARKET);
    }

    public Group getRoot() {

        root3D = new Group();
        //root3D.setRotate(-90);
        buttons = new Group();
        buttons.setTranslateX(-200);
        buttons.setTranslateY(350);

        root3D.getChildren().add(buttons);
        buttons.setRotate(90);

        //marketPane=new AnchorPane();
        //marketPane.setMinSize(700,1000);


        //PerspectiveCamera camera = new PerspectiveCamera(true);
        //camera.getTransforms().addAll(new Rotate(90,Rotate.Z_AXIS),new Rotate(0,Rotate.X_AXIS), new Rotate(0,Rotate.Y_AXIS), new Translate(0, 0, -20));
        //camera.translateXProperty().set(0);
        //camera.translateYProperty().set(-2.0);
        //camera.setTranslateZ(4);


        double x=ballsize*2;
        for(int i = 0; i< NUMBEROFROWS; i++)
        {
            generateRow(root3D,x,ballsXOffset,i);
            x-=2*ballsize;
        }

        toPutStartingX=x;

        toPut=new Sphere(ballsize);
        toPut.translateYProperty().set(toPutStartingY);
        toPut.translateXProperty().set(toPutStartingX+10*ballsize);
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
        generateColumns(rows,columnButtonOffset);

        MarbleAsset[][] marketMatrix=getMarketMatrix();

        for(int j=0;j<NUMBEROFCOLUMNS;j++)
            for(int i=0;i<NUMBEROFROWS;i++)
                columns.get(j).get(NUMBEROFROWS-i-1).setMaterial(new PhongMaterial(colors.get(marketMatrix[j][i].ordinal())));
        //SubScene slideSubscene = new SubScene(root3D, width, len, true, SceneAntialiasing.BALANCED);
        //slideSubscene.setTranslateY(subSceneTranslateY);
        //slideSubscene.setTranslateX(subSceneTranslateX);;
        //slideSubscene.setFill(Color.TRANSPARENT);
        //slideSubscene.setCamera(camera);


        //marketPane.getChildren().add(slideSubscene);
        //marketPane.setId("marketPane");
        error.setOpacity(0);
        error.setLayoutX(width/2);
        error.setLayoutY((len*4)/5);
        buttons.getChildren().add(error);
        getClient().getStage().show();

        return root3D;

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
        for(int i = 0; i< NUMBEROFCOLUMNS; i++)
        {
            Sphere ball=new Sphere(ballsize);
            ball.translateYProperty().set(y+i*2*ballsize);
            ball.translateXProperty().set(x);
            root3D.getChildren().add(ball);
            row.add(ball);

        }
        Button button=new Button();
        button.setTranslateX(250);
        button.setTranslateY(-350+100*k);
        button.setGraphic(new Text(Integer.toString(7-1-k)));
        button.setOnAction( p-> {

            if(!SetupPhase.getBoard().getController().isMarket())
                return;


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


            TranslateTransition transition = new TranslateTransition(Duration.seconds(0.75), toPut);
            transition.setToX(toPutStartingX+(NUMBEROFCOLUMNS+2)*ballsize*2);
            transition.setAutoReverse(false);
            transition.setDelay(new Duration(600));
            transition.setOnFinished(e->
            {

                sendLine(7-1-k);
                System.out.println(7-1-k);

            });
            transition.play();

            SetupPhase.getBoard().getController().setMarket(false);

        });
        rows.add(row);
        buttons.getChildren().add(button);

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
            button.setTranslateX(h);
            h+=80;
            button.setTranslateY(-700);

            setBut(button,i);
            buttons.getChildren().add(button);


            ArrayList<Sphere> column= new ArrayList<>();
            for (List<Sphere> row : rows) column.add(row.get(i));

            columns.add(column);
        }


    }

    public void setBut(Button but,int i)
    {
        but.setGraphic(new Text(Integer.toString(i)));

        but.setOnAction( p-> {

            if(!SetupPhase.getBoard().getController().isMarket())
                return;


            toPut.setTranslateY(toPut.getTranslateY()-(2-i+1)*ballsize*2);
            toPut.setTranslateX(toPutStartingX+(NUMBEROFCOLUMNS+2)*ballsize*2);

            moveX(toPut,toPut.getTranslateX()-2*ballsize,new Duration(100));


            ArrayList<Sphere> column= new ArrayList<>();
            for (List<Sphere> row : rows) column.add(row.get(i));



            for (Sphere circle : column) {


                moveX(circle,circle.getTranslateX()-ballsize*2,new Duration(100));



            }
            int len=column.size()-1;
            Sphere temp=column.get(len);
            column.remove(len);
            column.add(0,toPut);
            toPut=temp;


            moveY(toPut,toPutStartingY-(NUMBEROFCOLUMNS+1)*ballsize*2,new Duration(700));

            TranslateTransition transition = new TranslateTransition(Duration.seconds(0.75), toPut);
            transition.setToX(toPutStartingX+(NUMBEROFCOLUMNS+2)*ballsize*2);
            transition.setAutoReverse(false);
            transition.setDelay(new Duration(1300));
            transition.setOnFinished(e->
            {

                sendLine(i);

            });
            transition.play();

            SetupPhase.getBoard().getController().setMarket(false);





        });


    }


    /**
     * Called every time a resource is moved in the personalBoard or when the discardBox is received
     */
    @Override
    public void choosePositions() {
        Platform.runLater(()-> {
            SetupPhase.getBoard().setMode(BoardView3D.Mode.MOVING_RES);
            SetupPhase.getBoard().changeCamState(CamState.TOP);
        });
    }

    @Override
    public void chooseMarbleConversion() {


    }

}