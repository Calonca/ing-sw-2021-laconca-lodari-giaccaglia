package it.polimi.ingsw.client.view.GUI;


import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.CLI.IDLEViewBuilder;
import it.polimi.ingsw.client.view.GUI.GUIelem.ResourceButton;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.server.model.Resource;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * The CardShop is generated via a method that passes its subscene. It's composed of a small single card
 * selector and a confirmation button
 */
public class CardShopGUI extends CardShopViewBuilder implements GUIView {


    Text error=new Text("NOT ALLOWED RIGHT NOW.");
    Text errorChoice=new Text("SELECT AT LEAST ONE CARD");

    public AnchorPane cardsAnchor;
    int ROWS=3;
    int COLUMNS=4;
    List<UUID> cardsUUIDs=new ArrayList<>();
    boolean enabled=false;
    List<ResourceButton> scenePaymentButtons=new ArrayList<>();
    List<Button> scenesCardsToChoose=new ArrayList<>();
    javafx.collections.ObservableList<Boolean> scenePaidButtons;

    javafx.collections.ObservableList<Boolean> selectedSceneCards;



    @Override
    public void run() {
        ViewPersonalBoard.getController().isCardShopOpen(true);
    }

    public void addMarket() {
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().remove(this);

        Node toAdd=getRoot();
        toAdd.setTranslateX(-380);
        toAdd.setTranslateY(20);
        toAdd.setId("CARDSHOP");
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(toAdd);
        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());

    }

    public void addDiscardBox() {
        Node toadd=getRoot();
        toadd.setTranslateX(-195);
        toadd.setTranslateY(107);
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(toadd);
        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());

    }

    public SubScene getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/CardShop.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SubScene(root,430,800);

    }

    /**
     *
     * @param selected is a boolean array used to represent selection
     * @param scenesLeadersToChoose is a button array for the user to execute selection
     */




    /**
     * given a color and a position, the method will build one accordingly
     * @param color is a css color string
     * @param y is an int position
     * @param x is an int position
     * @return a spinner according to parameters
     */

    /**
     * service method
     * @return a button according to parameters
     */
    public Button validationButton()
    {
        Button confirm=new Button();
        confirm.setText("CONFIRM");
        confirm.setLayoutY(720);
        confirm.setLayoutX(300);
        confirm.setOnAction(p -> {





            if(!ViewPersonalBoard.getController().isCardShopOpen())
            {
                    error.setOpacity(1);
                    return;

            }
            int selectedCards=0;
            error.setOpacity(0);
            for(Boolean prod : selectedSceneCards)
            {
                if(prod)
                {
                    selectedCards++;
                }
            }
            if(selectedCards==0)
            {
                errorChoice.setOpacity(1);
                return;
            }
            scenePaymentButtons.get(0).setResource(Resource.GOLD);
            ViewPersonalBoard.getController().bindForPayment(scenePaymentButtons,scenePaidButtons);




        });
        return confirm;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

        GridPane cardsGrid=new GridPane();
        cardsGrid.setLayoutX(0);
        cardsGrid.setLayoutY(0);
        Button tempBut;
        for(int i=0;i<ROWS;i++)
        {
            for(int j=0;j<COLUMNS;j++)
            {
                //todo fix order
                ImageView tempImage = new ImageView(new Image("assets/leaders/raw/FRONT/Masters of Renaissance_Cards_FRONT_0.png", true));
                tempBut= new Button();

                tempImage.setFitWidth(115);
                tempImage.setFitHeight(165);

                tempBut.setGraphic(tempImage);
                tempBut.setStyle("-fx-border-color: transparent");


                cardsGrid.add(tempBut,i,j);
                scenesCardsToChoose.add(tempBut);

            }
        }



        selectedSceneCards=javafx.collections.FXCollections.observableArrayList();
        for (int i=0;i<ROWS*COLUMNS;i++)
            selectedSceneCards.add(false);

        ViewPersonalBoard.getController().dehighlightTrue(selectedSceneCards,scenesCardsToChoose);


        ViewPersonalBoard.getController().cardSelector(selectedSceneCards,scenesCardsToChoose,1);

        selectedSceneCards.addListener(new javafx.collections.ListChangeListener<Boolean>() {
            @Override
            public void onChanged(Change<? extends Boolean> c) {
                c.next();
                if(c.getAddedSubList().get(0))
                {
                    ViewPersonalBoard.getController().highlightTrue(selectedSceneCards,scenesCardsToChoose);
                    getClient().getStage().getScene().setCursor(ImageCursor.HAND);
                    for (Boolean aBoolean : selectedSceneCards)
                        if (aBoolean)
                            break;

                }
                else
                    {
                    ViewPersonalBoard.getController().dehighlightTrue(selectedSceneCards,scenesCardsToChoose);
                    getClient().getStage().getScene().setCursor(ImageCursor.HAND);

                }




            }});


        scenePaidButtons=javafx.collections.FXCollections.observableArrayList();
        ResourceButton tempContainer;
        for(int i=0;i<5;i++)
        {
            tempContainer=new ResourceButton();
            tempContainer.setLayoutY(720);
            tempContainer.setLayoutX(100+40*i);
            cardsAnchor.getChildren().add(tempContainer);
            scenePaymentButtons.add(tempContainer);
            tempContainer.setResource(Resource.EMPTY);
            scenePaidButtons.add(true);
        }

        scenePaidButtons.addListener(new javafx.collections.ListChangeListener<Boolean>() {
            @Override
            public void onChanged(Change<? extends Boolean> c) {
                c.next();
                if(!c.getAddedSubList().get(0))
                {
                    for(Boolean bol : scenePaidButtons)
                        if(bol)
                            return;
                    getClient().getStage().getScene().setCursor(new ImageCursor(new Image("assets/leaders/raw/FRONT/Masters of Renaissance_Cards_FRONT_0.png")));
                    ViewPersonalBoard.getController().setMoving(true);
                }
                else
                {
                    getClient().getStage().getScene().setCursor(ImageCursor.HAND);

                    for(Boolean bol : scenePaidButtons)
                        if(bol)
                            return;
                    getClient().getStage().getScene().setCursor(new ImageCursor(new Image("assets/leaders/raw/FRONT/Masters of Renaissance_Cards_FRONT_0.png")));
                    ViewPersonalBoard.getController().setMoving(true);
                    //ViewPersonalBoard.getController().deHighlightFalse(selectedResources,sceneResources);
                }


            }});


        cardsAnchor.getChildren().add(validationButton());
        cardsAnchor.getChildren().add(cardsGrid);

        error.setOpacity(0);
        error.setLayoutX(150);
        error.setLayoutY(320);

        cardsAnchor.getChildren().add(error);
        errorChoice.setOpacity(0);
        errorChoice.setLayoutX(150);
        errorChoice.setLayoutY(340);
        cardsAnchor.getChildren().add(errorChoice);

        getClient().getStage().show();

    }


    /**
     * Get called when choosing resources to buy the card
     */
    @Override
    public void choseResources() {

    }

    /**
     * Gets called when the player needs to place the development card on his personal board
     */
    @Override
    public void choosePositionForCard() {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(State.IDLE.name()))
            getClient().changeViewBuilder(new IDLEViewBuilder());
        else{
            System.out.println("Setup received: "+evt.getPropertyName()+ JsonUtility.serialize(evt.getNewValue()));
        }
    }
}