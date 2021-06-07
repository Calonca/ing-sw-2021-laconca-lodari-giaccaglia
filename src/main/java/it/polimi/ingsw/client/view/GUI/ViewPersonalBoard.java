package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.GUI.GUIelem.ButtonSelectionModel;
import it.polimi.ingsw.client.view.GUI.GUIelem.ResourceButton;
import it.polimi.ingsw.server.model.Resource;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewPersonalBoard extends it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder implements GUIView
{
    public AnchorPane menuPane;
    Text error=new Text("NOT ALLOWED RIGHT NOW");
    Text errorChoice=new Text("SELECT AT LEAST ONE PRODUCTION");
    GridPane wareHouseGrid=new GridPane();
    Button faithBut;
    int tempFaith =0;
    List<ResourceButton> sceneResources=new ArrayList<>();
    List<Integer[]> sceneProductionCosts=new ArrayList<>();

    List<ResourceButton> standardProductionButtons=new ArrayList<>();
    List<ResourceButton> standardProductionButtonsOut=new ArrayList<>();
    List<ResourceButton> wareHouseButtons=new ArrayList<>();

    List<Button> controlButtons=new ArrayList<>();


    List<Button> productions=new ArrayList<>();
    int[] productionIn=new int[]{1,0,1,0,0,0,1};
    int[] productionOut=new int[]{1,0,1,0,0,0,1};

    javafx.collections.ObservableList<Boolean> productionSelection;
    javafx.collections.ObservableList<Boolean> selectedStandardProductions;
    javafx.collections.ObservableList<Boolean> selectedStandardProductionsOut;
    javafx.collections.ObservableList<Boolean> selectedResources;



    private static ButtonSelectionModel boardController=new ButtonSelectionModel();


    public static ButtonSelectionModel getController()
    {
        return boardController;
    }
    @Override
    public void run() {
        getClient().getStage().setResizable(true);
        SubScene root=getRoot();
        root.setId("BOARD");
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().remove(0);
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(root);
        getClient().getStage().setHeight(800);
        getClient().getStage().setWidth(1200);
        getClient().getStage().setResizable(false);

        CardShopGUI cardshop=new CardShopGUI(true);
        cardshop.addMarket();
        ResourceMarketGUI market=new ResourceMarketGUI();
        market.addMatrix();
        ChooseResourceForMarket choose=new ChooseResourceForMarket();
        choose.run();






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
            faithBut.setLayoutX(faithBut.getLayoutX()+41.5);
        else
            faithBut.setLayoutX(faithBut.getLayoutX()-40);
    }

    public void moveFaithY(boolean downward){
        if(downward)
            faithBut.setLayoutY(faithBut.getLayoutY()+35);
        else
            faithBut.setLayoutY(faithBut.getLayoutY()-35);


    }

    public void moveFaith() {
        if (tempFaith <2)
        {
            tempFaith++;
            moveFaithX(true);
        } else
        if (tempFaith <4)
        {
            tempFaith++;
            moveFaithY(false);
        }
        else if (tempFaith < 9)
            {
                tempFaith++;
                moveFaithX(true);
            }
        else if (tempFaith < 11)
        {
            tempFaith++;
            moveFaithY(true);
        }
        else if (tempFaith < 16)
        {
            tempFaith++;
            moveFaithX(true);
        }
        else if (tempFaith <18)
        {
            tempFaith++;
            moveFaithY(false);
        } else
            moveFaithX(true);
    }

    public ButtonSelectionModel getBoardController() {
        return boardController;
    }


    public void initializeDepositAndWareHouse()
    {
    ResourceButton tempbut= new ResourceButton();
        tempbut.setResource(Resource.EMPTY);
        tempbut.setLayoutY(500);
        tempbut.setLayoutX(540);
        tempbut.color();
        menuPane.getChildren().add(tempbut);
        sceneResources.add(tempbut);



        for(int i=0;i<2;i++)
        {
        tempbut= new ResourceButton();
        tempbut.setResource(Resource.GOLD);
        tempbut.setLayoutY(545);
        tempbut.setLayoutX(520+20*i);
        tempbut.color();
        menuPane.getChildren().add(tempbut);
        sceneResources.add(tempbut);

        }

        for(int i=0;i<3;i++)
        {
        tempbut= new ResourceButton();
        tempbut.setResource(Resource.EMPTY);
        tempbut.setLayoutY(585);
        tempbut.setLayoutX(510+20*i);
        tempbut.color();
        menuPane.getChildren().add(tempbut);
        sceneResources.add(tempbut);

        }
        for(int i=0;i<5;i++)
        {
            ResourceButton wareHouseButton=new ResourceButton();
            wareHouseButton.setResource(Resource.EMPTY);
            wareHouseGrid.add(wareHouseButton,i,0);

            wareHouseButtons.add(wareHouseButton);
        }

        wareHouseGrid.setMaxHeight(100);
        wareHouseGrid.setMaxWidth(100);
        wareHouseGrid.setLayoutX(480);
        wareHouseGrid.setLayoutY(650);
        menuPane.getChildren().add(wareHouseGrid);

    }

    public void initializeFaithTrack()
    {
        faithBut =new Button();
        faithBut.setLayoutX(480);
        faithBut.setLayoutY(380);
        faithBut.setOnAction(p ->
        {
            moveFaith();
        });

        ImageView tempImageView=new ImageView(new Image("assets/punchboard/croce.png", true));
        tempImageView.setFitHeight(20);
        tempImageView.setFitWidth(20);

        faithBut.setGraphic(tempImageView);
        menuPane.getChildren().add(faithBut);
        }

    public void validationButton()
    {
        Button coverCardShop=new Button();
        coverCardShop.setLayoutX(550);
        coverCardShop.setLayoutY(750);
        coverCardShop.setGraphic(new Label("PRODUCE"));
        coverCardShop.setOnAction(p ->
        {

            error.setOpacity(0);
            errorChoice.setOpacity(0);

            for(ResourceButton but : standardProductionButtons)
                if(but.getResource()==Resource.TOCHOOSE)
                    return;
            //stockWareHouse();
        });
        menuPane.getChildren().add(coverCardShop);

    }

    public void stockWareHouse()
    {
        List<Resource> temp=new ArrayList<>();
        temp.add(standardProductionButtonsOut.get(0).getResource());
        while(temp!=null)
        {
            for(int i=0;i<wareHouseButtons.size();i++)
            {
                if(wareHouseButtons.get(i).getResource()==Resource.EMPTY)
                {
                    wareHouseButtons.get(i).setResource(temp.get(0));
                    wareHouseButtons.get(i).color();
                    temp.remove(0);
                }
            }
        }
    }

    public void addDevelopmentCards()
    {
        Button productionButton;

        for(int i=0;i<3;i++)
        {
            productionButton=new Button();
            productionButton.setLayoutX(740+150*i);
            productionButton.setLayoutY(550);
            ImageView devCardImage=new ImageView(new Image("assets/devCards/raw/FRONT/Masters of Renaissance_Cards_FRONT_BLUE_2.png", true));
            devCardImage.setFitWidth(100);
            devCardImage.setFitHeight(100);
            productionButton.setGraphic(devCardImage);
            menuPane.getChildren().add(productionButton);
            productions.add(productionButton);
        }
    }

    public void bindForStandardProduction()
    {

        Button productionButton=new Button();
        productionButton.setGraphic(new Label("PRODUCTION"));
        productionButton.setLayoutX(1100);
        productionButton.setLayoutY(50);
        productionButton.setOnAction(p -> getController().setProduction(true));
        menuPane.getChildren().add(productionButton);
        controlButtons.add(productionButton);

        selectedStandardProductions=javafx.collections.FXCollections.observableArrayList();
        selectedStandardProductionsOut=javafx.collections.FXCollections.observableArrayList();

        ResourceButton prodBut=new ResourceButton();
        prodBut.setResource(Resource.TOCHOOSE);
        prodBut.setLayoutX(650);
        prodBut.setLayoutY(650);
        prodBut.color();
        standardProductionButtons.add(prodBut);
        selectedStandardProductions.add(false);
        menuPane.getChildren().add(prodBut);

        prodBut=new ResourceButton();
        prodBut.setResource(Resource.TOCHOOSE);
        prodBut.color();
        prodBut.setLayoutX(650);
        prodBut.setLayoutY(630);
        standardProductionButtons.add(prodBut);
        menuPane.getChildren().add(prodBut);
        selectedStandardProductions.add(false);


        prodBut=new ResourceButton();
        prodBut.setResource(Resource.TOCHOOSE);
        prodBut.setMinWidth(30);
        prodBut.setMinWidth(30);

        prodBut.color();
        prodBut.setLayoutX(695);
        prodBut.setLayoutY(640);
        standardProductionButtonsOut.add(prodBut);
        selectedStandardProductionsOut.add(false);
        menuPane.getChildren().add(prodBut);


        getController().setProductionIn(selectedStandardProductions,standardProductionButtons);
        getController().selectChoiceFromDispenser(standardProductionButtonsOut,selectedStandardProductionsOut);

        selectedStandardProductions.addListener(new javafx.collections.ListChangeListener<Boolean>() {
            @Override
            public void onChanged(Change<? extends Boolean> c) {
                c.next();
                if(getController().isCardShopOpen())
                {
                    getClient().getStage().getScene().setCursor(ImageCursor.HAND);

                }

                if(!c.getAddedSubList().get(0))
                {


                }
                else
                {
                    getClient().getStage().getScene().setCursor(ImageCursor.HAND);

                    getController().deHighlightFalse(selectedResources,sceneResources);
                }


            }});

        selectedStandardProductionsOut.addListener(new javafx.collections.ListChangeListener<Boolean>() {
            @Override
            public void onChanged(Change<? extends Boolean> c) {
                c.next();
                if(!c.getAddedSubList().get(0))
                {

                }
                else
                {
                    getClient().getStage().getScene().setCursor(ImageCursor.HAND);

                    getController().deHighlightFalse(selectedResources,sceneResources);
                }


            }});

        Button developmentButton=new Button();
        developmentButton.setGraphic(new Label("DEVELOP"));
        developmentButton.setLayoutX(1100);
        developmentButton.setLayoutY(150);
        developmentButton.setOnAction(p -> getController().isCardShopOpen(true));
        menuPane.getChildren().add(developmentButton);
        controlButtons.add(developmentButton);

        Button marketButton=new Button();
        marketButton.setGraphic(new Label("MARKET PLAY"));
        marketButton.setLayoutX(1100);
        marketButton.setLayoutY(100);
        marketButton.setOnAction(p -> getController().setMarket(true));
        menuPane.getChildren().add(marketButton);
        controlButtons.add(marketButton);

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
                {
                    if(ViewPersonalBoard.getController().isMoving())
                    {
                            getClient().getStage().getScene().setCursor(ImageCursor.HAND);
                            ViewPersonalBoard.getController().setMoving(false);
                            productionSelection.set(c.getFrom(),false);
                    }
                    ViewPersonalBoard.getController().highlightTrue(productionSelection,productions);

                }
                else
                {
                    ViewPersonalBoard.getController().dehighlightTrue(productionSelection,productions);

                }

            }});


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

      //  ImageView tempImageView;
      //  tempImageView=new ImageView(new Image("assets/board/smallerboard.png", true));
      //  tempImageView.setFitHeight(500);
      //  tempImageView.setFitWidth(750);
      //  tempImageView.setLayoutX(445);
      //  tempImageView.setLayoutY(280);

      //  menuPane.getChildren().add(tempImageView);
      //  StackPane.setAlignment(tempImageView, Pos.BOTTOM_CENTER);

      //  initializeDepositAndWarehouse();

      //  initializeFaithTrack();

      //  validationButton();

        selectedResources =javafx.collections.FXCollections.observableArrayList();

        for(int i=0;i<sceneResources.size();i++)
            selectedResources.add(false);


        boardController.initializeFalseOnEmpty(selectedResources,sceneResources);
        //boardController.disableFalseEnableTrue(selected,sceneResources);
        boardController.bindToMove(selectedResources,sceneResources);



        List<Image> res=new ArrayList<>();
        res.add(new Image("assets/resources/GOLD.png"));
        res.add(new Image("assets/resources/SERVANT.png"));
        res.add(new Image("assets/resources/SHIELD.png"));
        res.add(new Image("assets/resources/STONE.png"));
        selectedResources.addListener(new javafx.collections.ListChangeListener<Boolean>() {
            @Override
            public void onChanged(Change<? extends Boolean> c) {
                c.next();
                if(!c.getAddedSubList().get(0))
                    getClient().getStage().getScene().setCursor(new ImageCursor(res.get(sceneResources.get(c.getFrom()).getResource().getResourceNumber())));
                else
                    getClient().getStage().getScene().setCursor(ImageCursor.HAND);


            }});








        //bindForStandardProduction();
        getClient().getStage().show();
    }
}
