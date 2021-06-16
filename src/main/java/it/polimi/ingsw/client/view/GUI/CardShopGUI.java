package it.polimi.ingsw.client.view.GUI;


import com.sun.scenario.effect.Effect;
import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.CLI.IDLEViewBuilder;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;
import it.polimi.ingsw.network.simplemodel.SimpleCardShop;
import it.polimi.ingsw.server.model.Resource;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;


public class CardShopGUI extends CardShopViewBuilder implements GUIView {


    Text error=new Text("NOT ALLOWED RIGHT NOW.");
    Text errorChoice=new Text("SELECT ONE CARD");

    public AnchorPane cardsAnchor;
    int purchasableCount=0;
    int ROWS=3;
    int COLUMNS=4;
    double cardsHGap=20;
    double cardsVGap=20;
    double len=800;
    double width=640;
    double cardTilt=0.1;
    double marketTranslateX=-570;
    double marketTranslateY=110;
    List<ImageView> scenesCardsToChoose=new ArrayList<>();

    List<Boolean> availableCards=new ArrayList<>();

    javafx.collections.ObservableList<Boolean> selectedSceneCards;

    public CardShopGUI(boolean viewing) {
        super(viewing);
    }

    /**
     * Do not remove
     */
    public CardShopGUI() {
        super(false);
    }

    /**
     * This runnable simply enables the card shop to be used by the player
     */
    @Override
    public void run() {

        Node root=getRoot();
        root.setTranslateX(-570);
        root.setTranslateY(110);
        root.setId("CARDSHOP");
        GUI.addLast(root);
        System.out.println(GUI.getRealPane().getChildren());
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
     * Service method
     * @return a button according to parameters
     */
    public Button validationButton()
    {

        Button confirm=new Button();
        confirm.setText("CONFIRM");
        confirm.setLayoutY(len-80);
        confirm.setLayoutX(width/2);
        confirm.setOnAction(p -> {


            int temp=0;



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

            System.out.println("temp is" + temp + "temp%4 is" + temp%4);
            if(temp<4)
                sendChosenCard(temp%4, 3);
            else if(temp<8)
                sendChosenCard(temp%4, 2);
            else sendChosenCard(temp%4, 1);





        });
        return confirm;
    }

    /**
     * The cardShop gets initialized as soon as the board.
     * @param url is ignored
     * @param resourceBundle is ignored
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {




        Button validationButton= validationButton();
        Path path;
        ImageView tempImage;
        SimpleCardShop simpleCardShop = getSimpleCardShop();
        for(int i=0;i<ROWS;i++)
        {
            for(int j=0;j<COLUMNS;j++)
            {
                if (simpleCardShop.getCardFront(NetworkDevelopmentCardColor.fromInt(j),3-i).isPresent())
                {
                    path=simpleCardShop.getCardFront(NetworkDevelopmentCardColor.fromInt(j),3-i).get().getCardPaths().getKey();
                    tempImage = new ImageView(new Image(path.toString(), true));
                }
                else
                    tempImage = new ImageView(new Image("assets/devCards/grayed out/BACK/Masters of Renaissance__Cards_BACK_BLUE_1.png"));
                //todo remove grid to remove buttons


                tempImage.setId("developmentCardButton");
                tempImage.setRotate(Math.random() * (cardTilt - -cardTilt + 1) + -1 );

                double cardWidth=(width-90)/COLUMNS;
                tempImage.setFitWidth(cardWidth);

                tempImage.setPreserveRatio(true);
                //ROWS COLUMNS

                ImageView tempStackImage;
                int stackHeight = getSimpleCardShop().getStackHeight(NetworkDevelopmentCardColor.fromInt(j),3-i);

                for(int k=1;k<stackHeight;k++)
                {
                    path=simpleCardShop.getCardFront(NetworkDevelopmentCardColor.fromInt(j),3-i).get().getCardPaths().getKey();
                    tempStackImage=new ImageView(new Image(path.toString(), true));
                    tempStackImage.setLayoutX(20+(cardsHGap+cardWidth)*j);
                    tempStackImage.setLayoutY(20+(cardsVGap+cardWidth*(741/504.0))*i);
                    tempStackImage.setRotate(Math.random() * ((5 - -5) + 1) + -5);
                    tempStackImage.setFitWidth(cardWidth);

                    tempStackImage.setPreserveRatio(true);

                    cardsAnchor.getChildren().add(tempStackImage);

                }
                cardsAnchor.getChildren().add(tempImage);
                tempImage.setLayoutX(20+(cardsHGap+cardWidth)*j);
                tempImage.setLayoutY(20+(cardsVGap+cardWidth*(741/504.0))*i);

                scenesCardsToChoose.add(tempImage);
                availableCards.add(simpleCardShop.getCardFront(NetworkDevelopmentCardColor.fromInt(j),3-i).get().getDevelopmentCard().isSelectable());

            }
        }

        ViewPersonalBoard.getController().isCardShopOpen(simpleCardShop.getIsAnyCardPurchasable());

        selectedSceneCards=javafx.collections.FXCollections.observableArrayList();
        for (int i=0;i<ROWS*COLUMNS;i++)
            selectedSceneCards.add(false);



        ViewPersonalBoard.getController().cardSelectorFromImage(selectedSceneCards,scenesCardsToChoose,1);

        selectedSceneCards.addListener((ListChangeListener<Boolean>) c -> {
            c.next();
            getClient().getStage().getScene().setCursor(ImageCursor.HAND);


            if(c.getAddedSubList().get(0))
            {
                //ViewPersonalBoard.getController().highlightTrue(selectedSceneCards,scenesCardsToChoose);
                for (Boolean aBoolean : selectedSceneCards)
                    if (aBoolean)
                    {
                        validationButton.setDisable(!availableCards.get(c.getFrom()));
                        scenesCardsToChoose.get(c.getFrom()).setLayoutY(scenesCardsToChoose.get(c.getFrom()).getLayoutY()-15);
                        System.out.println(c.getFrom());


                    }
                            //scenesCardsToChoose.get(selectedSceneCards.indexOf(aBoolean)).setLayoutX(scenesCardsToChoose.get(selectedSceneCards.indexOf(aBoolean)).getLayoutX()+10);
            }
            else
                {
                    validationButton.setDisable(false);
                    scenesCardsToChoose.get(c.getFrom()).setLayoutY(scenesCardsToChoose.get(c.getFrom()).getLayoutY()+15);

                //ViewPersonalBoard.getController().dehighlightTrue(selectedSceneCards,scenesCardsToChoose);

            }




        });



        cardsAnchor.getChildren().add(validationButton);


        getClient().getStage().show();
        cardsAnchor.setId("pane");

    }


    /**
     * Get called when choosing resources to buy the card
     */
    @Override
    public void selectResources() {
        //Hide cardShop
        BoardView3D.getInstance().setMode(BoardView3D.Mode.SELECT_CARD_SHOP);
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
    }
}