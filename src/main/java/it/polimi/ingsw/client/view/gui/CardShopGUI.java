package it.polimi.ingsw.client.view.gui;


import it.polimi.ingsw.client.view.gui.board.CamState;
import it.polimi.ingsw.client.view.gui.util.CardSelector;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;
import it.polimi.ingsw.network.assets.leaders.NetworkDevelopmentDiscountLeaderCard;
import it.polimi.ingsw.network.simplemodel.SimpleCardShop;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.client.simplemodel.State.CHOOSING_POSITION_FOR_DEVCARD;
import static it.polimi.ingsw.client.simplemodel.State.CHOOSING_RESOURCES_FOR_DEVCARD;


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
     * This runnable simply enables the correct personal board mode
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
            Playground.changeCamState(CamState.SEE_SHOP);
        }
    }


    /**
     * This method is called once upon initialization and every time the shop refreshes
     * @return the CardShop Subscene
     */
    public SubScene getRoot() {
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
                    tempImage = CardSelector.imageViewFromAsset(path);
                    ColorAdjust colorAdjust=new ColorAdjust();
                    colorAdjust.setBrightness(-0.40);
                    availableCards.add(simpleCardShop.getCardFront(NetworkDevelopmentCardColor.fromInt(j),3-i).get().getDevelopmentCard().isSelectable());
                    if(!simpleCardShop.getCardFront(NetworkDevelopmentCardColor.fromInt(j),3-i).get().getDevelopmentCard().isSelectable())
                        tempImage.setEffect(colorAdjust);
                    else {
                        int finalI = i;
                        int finalJ = j;
                        tempImage.setOnMouseClicked( p ->sendChosenCard(finalJ, 3-finalI) );
                    }
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

                        path=simpleCardShop.getCardFront(NetworkDevelopmentCardColor.fromInt(j),3-i).get().getCardPaths().getKey();
                        tempStackImage=CardSelector.imageViewFromAsset(path);
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


                }




            }
        }



        selectedSceneCards=javafx.collections.FXCollections.observableArrayList();
        for (int i=0;i<ROWS*COLUMNS;i++)
            selectedSceneCards.add(false);





        SimplePlayerLeaders activeLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).orElseThrow();

        ImageView temp;

        List<LeaderCardAsset> activeBonus = activeLeaders.getPlayerLeaders();
        for(int i=0;i<activeBonus.size();i++)
        {

            if(activeBonus.get(i).getNetworkLeaderCard().isLeaderActive())
                if(activeBonus.get(i).getNetworkLeaderCard() instanceof NetworkDevelopmentDiscountLeaderCard)
                {
                    temp=CardSelector.imageViewFromAsset(activeBonus.get(i).getCardPaths().getKey());
                    temp.setFitHeight(100);
                    temp.setLayoutX(20+100*i);
                    temp.setLayoutY(len-100);

                    cardsAnchor.getChildren().add(temp);

                }



        }



        getClient().getStage().show();
        cardsAnchor.setId("cardsPane");

        return new SubScene(cardsAnchor,width,len);

    }

    public static Group chosenCard;

    /**
     * This method is called by the PersonalBoard to acquire the newly bought card
     */
    public static void addChosenCard(){
        SimpleCardShop simpleCardShop = getSimpleModel().getElem(SimpleCardShop.class).orElseThrow();
        Path path = simpleCardShop.getPurchasedCard().orElseThrow().getCardPaths().getKey();
        ImageView imageView = CardSelector.imageViewFromAsset(path);
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





    /**
     * Get called during SELECTING_RESOURCES_FOR_DEVCARD
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
     * Gets called during CHOOSING_POSITION_FOR_DEVCARD
     */
    @Override
    public void choosePositionForCard() {
        Platform.runLater(()-> Playground.getThisPlayerBoard().setMode(BoardView3D.Mode.CHOOSE_POS_FOR_CARD));
        Playground.changeCamState(CamState.TOP);
    }


}