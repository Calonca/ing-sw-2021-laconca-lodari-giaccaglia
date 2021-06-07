package it.polimi.ingsw.client.view.GUI;


import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.CLI.IDLEViewBuilder;
import it.polimi.ingsw.client.view.GUI.GUIelem.ResourceButton;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.simplemodel.SimpleCardShop;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;
import it.polimi.ingsw.server.model.Resource;
import javafx.fxml.FXMLLoader;
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
import java.nio.file.Path;
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
    boolean enabled=false;
    double len=800;
    double width=430;

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


    public SubScene getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/CardShop.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SubScene(root,width,len);

    }





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


            int temp=0;


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
                    temp=selectedSceneCards.indexOf(prod);
                    break;

                }
            }
            if(selectedCards==0)
            {
                errorChoice.setOpacity(1);
                return;
            }
            SimpleCardShop simpleCardShop = getSimpleCardShop();
            for(int i=0; i<simpleCardShop.getCardFront(NetworkDevelopmentCardColor.BLUE,1).get().getDevelopmentCard().getCostList().size();i++)
                for (int j=0;j<simpleCardShop.getCardFront(NetworkDevelopmentCardColor.BLUE,1).get().getDevelopmentCard().getCostList().get(0).getValue();j++)
                    scenePaymentButtons.get(i).setResource(Resource.fromInt(i));

            sendChosenCard(temp%4, temp/4);
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
        Button sceneCard;
        int k=0;

        Path path;
        ImageView tempImage;
        SimpleCardShop simpleCardShop = getSimpleCardShop();
        for(int i=0;i<ROWS;i++)
        {
            for(int j=0;j<COLUMNS;j++)
            {
                if (simpleCardShop.getCardFront(NetworkDevelopmentCardColor.fromInt(j),i+1).isPresent())
                {
                    System.out.println(i+1);
                    path=simpleCardShop.getCardFront(NetworkDevelopmentCardColor.fromInt(j),i+1).get().getCardPaths().getKey();
                    System.out.println(simpleCardShop.getCardFront(NetworkDevelopmentCardColor.fromInt(j),i+1).get().getDevelopmentCard().getLevel());
                    tempImage = new ImageView(new Image(path.toString(), true));
                }
                else
                    tempImage = new ImageView(new Image("assets/devCards/grayed out/BACK/Masters of Renaissance__Cards_BACK_BLUE_1.png"));


                sceneCard = new Button();

                tempImage.setFitWidth((width-70)/4);
                tempImage.setFitHeight((len-130)/4);

                sceneCard.setGraphic(tempImage);
                sceneCard.setStyle("-fx-border-color: transparent");
                //ROWS COLUMNS
                cardsGrid.add(sceneCard,j,i);
                scenesCardsToChoose.add(sceneCard);

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
                getClient().getStage().getScene().setCursor(ImageCursor.HAND);

                if(c.getAddedSubList().get(0))
                {
                    ViewPersonalBoard.getController().highlightTrue(selectedSceneCards,scenesCardsToChoose);
                    for (Boolean aBoolean : selectedSceneCards)
                        if (aBoolean)
                            break;

                }
                else
                    {
                    ViewPersonalBoard.getController().dehighlightTrue(selectedSceneCards,scenesCardsToChoose);

                }




            }});


        scenePaidButtons=javafx.collections.FXCollections.observableArrayList();
        ResourceButton tempContainer;
        for(int i=0;i<5;i++)
        {
            tempContainer=new ResourceButton();
            tempContainer.setLayoutY(len-80);
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
        error.setLayoutX(width/3);
        error.setLayoutY(len-40);

        cardsAnchor.getChildren().add(error);
        errorChoice.setOpacity(0);
        errorChoice.setLayoutX(width/3);
        errorChoice.setLayoutY(len-60);
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