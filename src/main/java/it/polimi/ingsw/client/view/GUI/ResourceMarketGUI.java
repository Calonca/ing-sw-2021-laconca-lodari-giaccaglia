package it.polimi.ingsw.client.view.GUI;


import it.polimi.ingsw.client.view.GUI.board.CamState;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.assets.leaders.NetworkMarketLeaderCard;
import it.polimi.ingsw.network.assets.marbles.MarbleAsset;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.ChooseWhiteMarbleConversionEvent;
import it.polimi.ingsw.network.simplemodel.ActiveLeaderBonusInfo;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * The market is generated via a subscene that is attached to the main scene
 */
public class ResourceMarketGUI extends ResourceMarketViewBuilder {


    public Sphere toPut;

    public int NUMBEROFCOLUMNS =4;
    public int NUMBEROFROWS =3;

    public double ballsize=40;
    int columnButtonOffset=-75;


    double ballsXOffset =-4.5;

    double toPutStartingY= ballsXOffset + NUMBEROFROWS *ballsize*2;
    double toPutStartingHeight;

    public boolean enabled=false;
    public double width=400;
    public double columnButtonX=250;
    public double columnButtonY=500;

    public double rowButtonY=-700;


    //public AnchorPane marketPane;
    double marketTranslateX=-410;
    double marketTranslateY=-370;

    double leadersTranslateX=-200;
    double leadersTranslateY=350;

    double buttonsTranslateX=-100;
    double buttonsTranslateY=550;
    public List<List<Sphere>> rows=new ArrayList<>();
    public List<List<Sphere>> columns=new ArrayList<>();
    private Group root3D;
    private Group buttons;
    boolean active=false;


    /**
     * This runnable simply enables the market for the user to choose
     */
    @Override
    public void run() {
        active=true;
        Playground.changeCamState(CamState.SEE_RESOURCE_MARKET);
    }

    public Group getRoot() {

        root3D = new Group();
        //root3D.setRotate(-90);
        buttons = new Group();
        Group leaders=new Group();

        leaders.setTranslateX(leadersTranslateX);
        leaders.setTranslateY(leadersTranslateY);

        buttons.setTranslateX(buttonsTranslateX);
        buttons.setTranslateY(buttonsTranslateY);

        root3D.getChildren().add(leaders);
        root3D.getChildren().add(buttons);
        buttons.setRotate(90);
        leaders.setRotate(90);

        SimplePlayerLeaders activeLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).orElseThrow();

        Rectangle sceneLeadersToAdd;

        ImagePattern marketAsset=new ImagePattern(new Image("assets/punchboard/MarketBoard.png"));

        Rectangle marketBoardRectangle =new Rectangle(770,1000);
        marketBoardRectangle.setFill(marketAsset);
        marketBoardRectangle.setTranslateX(marketTranslateX);
        marketBoardRectangle.setTranslateY(marketTranslateY);

        root3D.getChildren().add(marketBoardRectangle);
        List<LeaderCardAsset> activeBonus = activeLeaders.getPlayerLeaders();
        for(int i=0;i<activeBonus.size();i++)
        {

            if(activeBonus.get(i).getNetworkLeaderCard().isLeaderActive())
                if(activeBonus.get(i).getNetworkLeaderCard() instanceof NetworkMarketLeaderCard)
                {
                    sceneLeadersToAdd=new Rectangle(150,100);
                    sceneLeadersToAdd.setTranslateY(250);
                    sceneLeadersToAdd.setTranslateX(-50*(4+i));
                    sceneLeadersToAdd.setTranslateZ(-10);

                    sceneLeadersToAdd.setFill(new ImagePattern(new Image(activeBonus.get(i).getCardPaths().getKey().toString(),false)));
                    leaders.getChildren().add(sceneLeadersToAdd);

                }

        }

        double rowsStartingHeight=ballsize*2;
        for(int i = 0; i< NUMBEROFCOLUMNS; i++)
        {
            generateColumns(root3D,rowsStartingHeight, ballsXOffset,i);
            rowsStartingHeight-=2*ballsize;
        }

        toPutStartingHeight=rowsStartingHeight;

        toPut=new Sphere(ballsize);
        toPut.translateYProperty().set(toPutStartingY);
        toPut.translateXProperty().set(toPutStartingHeight +10*ballsize);
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
        generateRow(rows,columnButtonOffset);

        MarbleAsset[][] marketMatrix=getMarketMatrix();

        for(int j = 0; j< NUMBEROFROWS; j++)
            for(int i = 0; i< NUMBEROFCOLUMNS; i++)
                columns.get(j).get(NUMBEROFCOLUMNS -i-1).setMaterial(new PhongMaterial(colors.get(marketMatrix[j][i].ordinal())));


        getClient().getStage().show();
        toPut.translateZProperty().set(toPut.getTranslateZ()-100);

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
     * @param rowsHeight is not null, coordinate
     * @param rowsStartingX is not null, coordinate
     * @param buttonIndexY index to position correctly
     */
    public void generateColumns(Group root3D, double rowsHeight, double rowsStartingX, int buttonIndexY){


        List<Sphere> row=new ArrayList<>();
        for(int i = 0; i< NUMBEROFROWS; i++)
        {
            Sphere ball=new Sphere(ballsize);
            ball.translateYProperty().set(rowsStartingX+i*2*ballsize);
            ball.translateXProperty().set(rowsHeight);
            ball.translateZProperty().set(ball.getTranslateZ()-100);

            root3D.getChildren().add(ball);
            row.add(ball);

        }

        Button button=new Button();
        button.setTranslateX(columnButtonX);
        button.setTranslateZ(-100);

        button.setTranslateY(- columnButtonY+100*buttonIndexY);
        button.setGraphic(new Text(Integer.toString(7 -1-buttonIndexY)));
        button.setOnAction( p-> {

            if(active)
                return;
            active=true;



            toPut.setTranslateX(rowsHeight);
            moveY(toPut,toPut.getTranslateY()-2*ballsize,new Duration(100));


            for (Sphere circle : row) {

                moveY(circle,circle.getTranslateY()-ballsize*2,new Duration(100));
            }

            Sphere toswap=row.get(0);
            row.remove(0);
            row.add(toPut);
            toPut=toswap;


            TranslateTransition transition = new TranslateTransition(Duration.seconds(0.75), toPut);
            transition.setToX(toPutStartingHeight +(NUMBEROFROWS +2)*ballsize*2);
            transition.setAutoReverse(false);
            transition.setDelay(new Duration(600));
            transition.setOnFinished(e->
            {

                sendLine(NUMBEROFCOLUMNS + NUMBEROFROWS-1-buttonIndexY);
                System.out.println(7-1-buttonIndexY);

            });
            transition.play();

        });
        rows.add(row);
        buttons.getChildren().add(button);

    }

    /**
     * Given a list of rows, it will be explored traversally to create a list of lists(columns)
     * @param rows is not null
     * @param h is not null, coordinate
     */
    public void generateRow(List<List<Sphere>> rows, int h){



        for(int i=0;i<rows.get(0).size();i++)
        {




            Button button=new Button();
            button.setTranslateX(h);
            h+=80;
            button.setTranslateY(rowButtonY);

            setBut(button,i);
            buttons.getChildren().add(button);
            button.setTranslateZ(-100);


            ArrayList<Sphere> column= new ArrayList<>();
            for (List<Sphere> row : rows) column.add(row.get(i));

            columns.add(column);
        }


    }

    public void setBut(Button but,int i)
    {
        but.setGraphic(new Text(Integer.toString(i)));

        but.setOnAction( p-> {

            if(active)
                return;


            toPut.setTranslateY(toPut.getTranslateY()-(2-i+1)*ballsize*2);
            toPut.setTranslateX(toPutStartingHeight +(NUMBEROFROWS +2)*ballsize*2);

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


            moveY(toPut,toPutStartingY-(NUMBEROFROWS +1)*ballsize*2,new Duration(700));

            TranslateTransition transition = new TranslateTransition(Duration.seconds(0.75), toPut);
            transition.setToX(toPutStartingHeight +(NUMBEROFROWS +2)*ballsize*2);
            transition.setAutoReverse(false);
            transition.setDelay(new Duration(1300));
            transition.setOnFinished(e->
                    sendLine(i));
            System.out.println(i);
            transition.play();

            active=false;



        });


    }


    /**
     * Called every time a resource is moved in the personalBoard or when the discardBox is received
     */
    @Override
    public void choosePositions() {
        Platform.runLater(()-> {
            Playground.getThisPlayerBoard().setMode(BoardView3D.Mode.MOVING_RES);
            Playground.changeCamState(CamState.TOP);
        });
    }


    double bonusStartingX=-400;
    double bonusY=500;
    @Override
    public void chooseMarbleConversion() {

        //todo generalize
        List<javafx.scene.image.ImageView> res=new ArrayList<>();
        res.add(new javafx.scene.image.ImageView(new Image("assets/resources/GOLD.png")));
        res.add(new javafx.scene.image.ImageView(new Image("assets/resources/SERVANT.png")));
        res.add(new javafx.scene.image.ImageView(new Image("assets/resources/SHIELD.png")));
        res.add(new javafx.scene.image.ImageView(new Image("assets/resources/STONE.png")));


        ActiveLeaderBonusInfo simplePlayerLeaders = getSimpleModel().getPlayerCache(getClient().getCommonData().getThisPlayerIndex()).getElem(ActiveLeaderBonusInfo.class).orElseThrow();

        Group resourceGroup=new Group();

        root3D.getChildren().add(resourceGroup);

        List<ResourceAsset> toChoose=simplePlayerLeaders.getMarketBonusResources();


        for(int i=0;i<toChoose.size();i++) {
            int finalI = i;

            res.get((toChoose.get(finalI).getResourceNumber())).setLayoutX(-bonusStartingX+200*finalI);
            res.get((toChoose.get(finalI).getResourceNumber())).setLayoutY(bonusY);
            res.get((toChoose.get(finalI).getResourceNumber())).setTranslateZ(-100);

            resourceGroup.getChildren().add(res.get((toChoose.get(finalI).getResourceNumber())));
            res.get(toChoose.get(i).getResourceNumber()).setOnMouseClicked(p->
            {
                ChooseWhiteMarbleConversionEvent event= new ChooseWhiteMarbleConversionEvent(toChoose.get(finalI).getResourceNumber());
                getClient().getServerHandler().sendCommandMessage(new EventMessage(event));
                resourceGroup.getChildren().clear();

            });
        }

    }

}