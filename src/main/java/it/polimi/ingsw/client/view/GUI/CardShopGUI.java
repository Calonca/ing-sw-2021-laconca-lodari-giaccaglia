package it.polimi.ingsw.client.view.GUI;


import it.polimi.ingsw.client.view.GUI.board.CamState;
import it.polimi.ingsw.client.view.GUI.util.CardSelector;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;
import it.polimi.ingsw.network.assets.leaders.NetworkDevelopmentDiscountLeaderCard;
import it.polimi.ingsw.network.assets.leaders.NetworkMarketLeaderCard;
import it.polimi.ingsw.network.simplemodel.SimpleCardShop;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.client.simplemodel.State.*;
import static it.polimi.ingsw.client.simplemodel.State.CHOOSING_POSITION_FOR_DEVCARD;


public class CardShopGUI extends CardShopViewBuilder {


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
    boolean active=false;
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
        //Todo better wrong event handling
        if (CHOOSING_RESOURCES_FOR_DEVCARD.name().equals(getThisPlayerCache().getCurrentState())) {
            selectResources();
        } else if (CHOOSING_POSITION_FOR_DEVCARD.name().equals(getThisPlayerCache().getCurrentState())) {
            choosePositionForCard();
        } else {
            active=true;
            Playground.getPlayground().changeCamState(CamState.SEE_SHOP);
        }
    }


    public SubScene getRoot() {
        Button validationButton= validationButton();
        Path path;
        ImageView tempImage;
        SimpleCardShop simpleCardShop = getSimpleCardShop();
        cardsAnchor=new AnchorPane();
        cardsAnchor.setMinSize(400,600);
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


                tempImage.setRotate(Math.random() * (cardTilt - -cardTilt + 1) + -1 );

                double cardWidth=(width-cardsVGap*4.5)/COLUMNS;
                tempImage.setFitWidth(cardWidth);

                tempImage.setPreserveRatio(true);
                //ROWS COLUMNS

                ImageView tempStackImage;
                int stackHeight = getSimpleCardShop().getStackHeight(NetworkDevelopmentCardColor.fromInt(j),3-i);
                Effect dropShadow=new DropShadow(BlurType.GAUSSIAN, Color.rgb(0,0,0,0.5),10,0.7,5,5);

                for(int k=1;k<stackHeight;k++)
                {
                    if(k==stackHeight-2)
                        path=simpleCardShop.getSecondCard(NetworkDevelopmentCardColor.fromInt(j),3-i).get().getCardPaths().getKey();
                    else
                        path=simpleCardShop.getCardFront(NetworkDevelopmentCardColor.fromInt(j),3-i).get().getCardPaths().getKey();
                    tempStackImage=new ImageView(new Image(path.toString(), true));
                    tempStackImage.setLayoutX(cardsHGap+(cardsHGap+cardWidth)*j);
                    tempStackImage.setLayoutY(20+(cardsHGap+cardWidth*(741/504.0))*i);
                    tempStackImage.setRotate(Math.random() * ((cardTilt*50 - -cardTilt*50) + 1) + -5);
                    tempStackImage.setFitWidth(cardWidth);
                    tempStackImage.setEffect(dropShadow);
                    tempStackImage.setPreserveRatio(true);

                    cardsAnchor.getChildren().add(tempStackImage);

                }
                cardsAnchor.getChildren().add(tempImage);
                tempImage.setLayoutX(cardsHGap+(cardsHGap+cardWidth)*j);
                tempImage.setLayoutY(cardsHGap+(cardsVGap+cardWidth*(741/504.0))*i);

                scenesCardsToChoose.add(tempImage);
                availableCards.add(simpleCardShop.getCardFront(NetworkDevelopmentCardColor.fromInt(j),3-i).get().getDevelopmentCard().isSelectable());

                ColorAdjust colorAdjust=new ColorAdjust();
                colorAdjust.setBrightness(-0.40);

                if(!simpleCardShop.getCardFront(NetworkDevelopmentCardColor.fromInt(j),3-i).get().getDevelopmentCard().isSelectable())
                    tempImage.setEffect(colorAdjust);

            }
        }



        selectedSceneCards=javafx.collections.FXCollections.observableArrayList();
        for (int i=0;i<ROWS*COLUMNS;i++)
            selectedSceneCards.add(false);



        CardSelector cardSelector=new CardSelector();
        cardSelector.cardSelectorFromImage(selectedSceneCards,scenesCardsToChoose,1);

        selectedSceneCards.addListener((ListChangeListener<Boolean>) c -> {
            c.next();
            getClient().getStage().getScene().setCursor(ImageCursor.HAND);


            if(c.getAddedSubList().get(0))
            {
                //ViewPersonalBoard.getController().highlightTrue(selectedSceneCards,scenesCardsToChoose);
                for (Boolean aBoolean : selectedSceneCards)
                    if (aBoolean)
                    {
                        scenesCardsToChoose.get(c.getFrom()).setLayoutY(scenesCardsToChoose.get(c.getFrom()).getLayoutY()-15);
                        System.out.println(c.getFrom());


                    }
                //scenesCardsToChoose.get(selectedSceneCards.indexOf(aBoolean)).setLayoutX(scenesCardsToChoose.get(selectedSceneCards.indexOf(aBoolean)).getLayoutX()+10);
            }
            else
            {
                scenesCardsToChoose.get(c.getFrom()).setLayoutY(scenesCardsToChoose.get(c.getFrom()).getLayoutY()+15);

                //ViewPersonalBoard.getController().dehighlightTrue(selectedSceneCards,scenesCardsToChoose);

            }




        });


        SimplePlayerLeaders activeLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).orElseThrow();

        ImageView temp=new ImageView(new Image("assets/leaders/raw/FRONT/Masters of Renaissance_Cards_FRONT_0.png"));

        temp.setLayoutX(20+100);
        temp.setLayoutY(len-100);
        temp.setTranslateZ(-100);

        cardsAnchor.getChildren().add(temp);
        List<LeaderCardAsset> activeBonus = activeLeaders.getPlayerLeaders();
        for(int i=0;i<activeBonus.size();i++)
        {

            if(activeBonus.get(i).getNetworkLeaderCard().isLeaderActive())
                if(activeBonus.get(i).getNetworkLeaderCard() instanceof NetworkDevelopmentDiscountLeaderCard)
                {
                    temp=new ImageView(new Image(activeBonus.get(i).getCardPaths().getKey().toString(),false));
                    temp.setFitHeight(100);
                    temp.setPreserveRatio(true);
                    temp.setLayoutX(20+100*i);
                    temp.setLayoutY(len-100);

                    cardsAnchor.getChildren().add(temp);

                }

        }

        cardsAnchor.getChildren().add(validationButton);


        getClient().getStage().show();
        cardsAnchor.setId("cardsPane");

        return new SubScene(cardsAnchor,width,len);

    }

    public static Group chosenCard;
    public static void addChosenCard(){
        SimpleCardShop simpleCardShop = getSimpleModel().getElem(SimpleCardShop.class).orElseThrow();
        Path path = simpleCardShop.getPurchasedCard().orElseThrow().getCardPaths().getKey();
        ImageView imageView = new ImageView(new Image(path.toString(), true));
        if (chosenCard==null) {
            chosenCard = new Group();
            Playground.addNodeToThisPlayer(chosenCard, new Point3D(-500, -100, 0));
        } else {
            chosenCard.getChildren().clear();
        }
        chosenCard.getChildren().add(imageView);
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
     * Get called when choosing resources to buy the card
     */
    @Override
    public void selectResources() {
        //Hide cardShop
        Platform.runLater(()-> {
            Playground.getThisPlayerBoard().setMode(BoardView3D.Mode.SELECT_CARD_SHOP);
            Playground.changeCamState(CamState.SELECT_CARD_SHOP);
        }
        );


    }

    /**
     * Gets called when the player needs to place the development card on his personal board
     */
    @Override
    public void choosePositionForCard() {
        Platform.runLater(()-> Playground.getThisPlayerBoard().setMode(BoardView3D.Mode.CHOOSE_POS_FOR_CARD));
        Playground.changeCamState(CamState.TOP);
    }


}