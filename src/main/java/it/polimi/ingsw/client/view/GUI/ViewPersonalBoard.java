package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.GUI.GUIelem.ButtonSelectionModel;
import it.polimi.ingsw.client.view.GUI.GUIelem.ResourceButton;
import it.polimi.ingsw.server.model.Resource;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewPersonalBoard extends it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder implements GUIView
{
    public AnchorPane menuPane;
    Text error=new Text("NOT ALLOWED RIGHT NOW");
    Text errorChoice=new Text("SELECT AT LEAST ONE PRODUCTION");

    Button faithbut;
    int tempfaith=0;
    List<ResourceButton> sceneResources=new ArrayList<>();
    List<Button> productions=new ArrayList<>();
    javafx.collections.ObservableList<Boolean> productionSelection;
    List<Boolean> selected=new ArrayList<>();



    private static ButtonSelectionModel boardController=new ButtonSelectionModel();


    public static ButtonSelectionModel getController()
    {
        return boardController;
    }
    @Override
    public void run() {
        getClient().getStage().setResizable(true);

        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().remove(0);
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(getRoot());
        getClient().getStage().setHeight(800);
        getClient().getStage().setWidth(1200);
        getClient().getStage().setResizable(false);

        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());
        CardShopGUI cardshop=new CardShopGUI();
        cardshop.addMarket();
        ResourceMarketGUI market=new ResourceMarketGUI();
        market.addMatrix();
        DiscardBoxInitializer disc= new DiscardBoxInitializer();
        disc.fillDiscardBox();




    }

    public SubScene getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/ViewPersonalBoard.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SubScene(root,1200,800);


    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public void moveFaithX(boolean foward){
        if(foward)
            faithbut.setLayoutX(faithbut.getLayoutX()+41.5);
        else
            faithbut.setLayoutX(faithbut.getLayoutX()-40);
    }

    public void moveFaithY(boolean downward){
        if(downward)
            faithbut.setLayoutY(faithbut.getLayoutY()+35);
        else
            faithbut.setLayoutY(faithbut.getLayoutY()-35);


    }

    public void moveFaith() {
        if (tempfaith<2)
        {
            tempfaith++;
            moveFaithX(true);
        } else
        if (tempfaith<4)
        {
            tempfaith++;
            moveFaithY(false);
        }
        else if (tempfaith < 9)
            {
                tempfaith++;
                moveFaithX(true);
            }
        else if (tempfaith < 11)
        {
            tempfaith++;
            moveFaithY(true);
        }
        else if (tempfaith< 16)
        {
            tempfaith++;
            moveFaithX(true);
        }
        else if (tempfaith<18)
        {
            tempfaith++;
            moveFaithY(false);
        } else
            moveFaithX(true);
    }

    public ButtonSelectionModel getBoardController() {
        return boardController;
    }


    public void initializeDeposit()
    {
    ResourceButton tempbut= new ResourceButton();
        tempbut.setResource(Resource.EMPTY);
        tempbut.setLayoutY(500);
        tempbut.setLayoutX(450);
        tempbut.color();
        menuPane.getChildren().add(tempbut);
        sceneResources.add(tempbut);



        for(int i=0;i<2;i++)
    {
        tempbut= new ResourceButton();
        tempbut.setResource(Resource.GOLD);
        tempbut.setLayoutY(545);
        tempbut.setLayoutX(430+30*i);
        tempbut.color();
        menuPane.getChildren().add(tempbut);
        sceneResources.add(tempbut);

    }

        for(int i=0;i<3;i++)
    {
        tempbut= new ResourceButton();
        tempbut.setResource(Resource.EMPTY);
        tempbut.setLayoutY(585);
        tempbut.setLayoutX(420+30*i);
        tempbut.color();
        menuPane.getChildren().add(tempbut);
        sceneResources.add(tempbut);

    }}

    public void initializeFaithTrack()
    {
        faithbut=new Button();
        faithbut.setLayoutX(380);
        faithbut.setLayoutY(380);
        faithbut.setOnAction( p ->
        {
            moveFaith();
        });

        ImageView tempImageView=new ImageView(new Image("assets/punchboard/croce.png", true));
        tempImageView.setFitHeight(20);
        tempImageView.setFitWidth(20);

        faithbut.setGraphic(tempImageView);
        menuPane.getChildren().add(faithbut);
        }

    public void validationButton()
    {
        Button coverCardShop=new Button();
        coverCardShop.setLayoutX(550);
        coverCardShop.setLayoutY(750);
        coverCardShop.setGraphic(new Label("PRODUCE"));
        coverCardShop.setOnAction(p ->
        {
            if(!ViewPersonalBoard.getController().isProduction())
            {
                error.setOpacity(1);
                return;

            }
            int prodcount=0;
            for(Boolean prod : productionSelection)
            {
                if(prod)
                {
                        prodcount++;
                }

            }
            if(prodcount==0)
            {
                ;
                errorChoice.setOpacity(1);
                return;

            }
            error.setOpacity(0);
            errorChoice.setOpacity(0);
        });
        menuPane.getChildren().add(coverCardShop);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ImageView tempImageView;
        tempImageView=new ImageView(new Image("assets/board/smallerboard.png", true));

        tempImageView.setFitHeight(500);
        tempImageView.setFitWidth(850);
        tempImageView.setLayoutX(345);
        tempImageView.setLayoutY(280);

        menuPane.getChildren().add(tempImageView);
        StackPane.setAlignment(tempImageView, Pos.BOTTOM_CENTER);

        initializeDeposit();

        initializeFaithTrack();

        validationButton();
        for(int i=0;i<sceneResources.size();i++)
            selected.add(false);
        boardController.initializeFalseOnEmpty(selected,sceneResources);
        //boardController.disableFalseEnableTrue(selected,sceneResources);
        boardController.bindToMove(selected,sceneResources);







        Button productionButton;

        for(int i=0;i<3;i++)
        {
        productionButton=new Button();
        productionButton.setLayoutX(695+168*i);
        productionButton.setLayoutY(550);
        ImageView topo=new ImageView(new Image("assets/devCards/raw/FRONT/Masters of Renaissance_Cards_FRONT_BLUE_2.png", true));
        topo.setFitWidth(100);
        topo.setFitHeight(100);
        productionButton.setGraphic(topo);
        menuPane.getChildren().add(productionButton);
        productions.add(productionButton);
        }



        Button button=new Button();
        button.setGraphic(new Label("PRODUCTION"));
        button.setLayoutX(800);
        button.setLayoutY(100);
        button.setOnAction(p -> getController().setProduction(true));

        menuPane.getChildren().add(button);
        button=new Button();
        button.setGraphic(new Label("DEVELOP"));
        button.setLayoutX(900);
        button.setLayoutY(150);
        button.setOnAction(p -> getController().setDevelop(true));

        menuPane.getChildren().add(button);
        button=new Button();
        button.setGraphic(new Label("MARKET PLAY"));
        button.setLayoutX(950);
        button.setLayoutY(100);
        button.setOnAction(p -> getController().setMarket(true));

        menuPane.getChildren().add(button);
        error.setOpacity(0);
        error.setLayoutX(550);
        error.setLayoutY(740);
        menuPane.getChildren().add(error);
        errorChoice.setOpacity(0);
        errorChoice.setLayoutX(550);
        errorChoice.setLayoutY(730);
        menuPane.getChildren().add(errorChoice);

        productionSelection=javafx.collections.FXCollections.observableArrayList();

        for(int i=0; i<productions.size();i++)
            productionSelection.add(false);


        getBoardController().cardSelector(productionSelection,productions,3);


        productionSelection.addListener(new javafx.collections.ListChangeListener<Boolean>() {
            @Override
            public void onChanged(Change<? extends Boolean> c) {
                c.next();
                if(c.getAddedSubList().get(0))
                    ViewPersonalBoard.getController().highlightTrue(productionSelection,productions);
                else
                    ViewPersonalBoard.getController().dehighlightTrue(productionSelection,productions);


            }});
        getClient().getStage().show();
    }
}
