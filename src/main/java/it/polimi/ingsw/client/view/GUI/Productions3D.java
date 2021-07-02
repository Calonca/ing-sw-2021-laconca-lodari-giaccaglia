package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.PlayerCache;
import it.polimi.ingsw.client.view.GUI.util.CardSelector;
import it.polimi.ingsw.client.view.GUI.util.NodeAdder;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ProductionViewBuilder;
import it.polimi.ingsw.network.assets.CardAsset;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.simplemodel.SimpleCardCells;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class contains all information regarding all the possible productions
 */
public class Productions3D implements PropertyChangeListener {

    private final BoardView3D view3D;
    private Group prodGroup;

    public Productions3D(BoardView3D view3D) {
        this.view3D = view3D;
    }

    /**
     * Method to initialize or refresh the productions
     */
    public void updateProds() {
        Group parent = view3D.parent;
        BoardView3D.Mode mode = view3D.mode;
        PlayerCache cache = view3D.getCache();
        List<Node> prodList = new ArrayList<>();

        if (prodGroup == null)
        {
            prodGroup = new Group();
            NodeAdder.addNodeToParent(parent, view3D.boardRec, prodGroup, new Point3D(0,0,0));
        }

        SimpleCardCells simpleCardCells = cache.getElem(SimpleCardCells.class).orElseThrow();

       //System.out.println(JsonUtility.serialize(simpleCardCells));
        Button basicButton = new Button();
        basicButton.setText("Basic");
        basicButton.setId("basic");
        basicButton.setLayoutX(620);
        basicButton.setLayoutY(1000);
        basicButton.setTranslateZ(-20);
        basicButton.setPrefHeight(80);
        basicButton.setPrefWidth(200);
        basicButton.setStyle("-fx-font-size:20");

        basicButton.setOpacity(mode.equals(BoardView3D.Mode.CHOOSE_PRODUCTION)?1:0);

        basicButton.setOnMouseClicked(p->{
            if (mode.equals(BoardView3D.Mode.CHOOSE_PRODUCTION) && simpleCardCells.isProductionAtPositionAvailable(0).orElse(false)) {
                ProductionViewBuilder.sendChosenProduction(0);
            }
        });

        prodList.add(basicButton);

        Button productionButton = new Button();
        productionButton.setText("Produce");
        basicButton.setId("produce");
        productionButton.setLayoutX(500);
        productionButton.setLayoutY(1550);
        productionButton.setTranslateZ(-15);
        productionButton.setPrefHeight(80);
        productionButton.setPrefWidth(200);
        productionButton.setStyle("-fx-font-size:30");

        productionButton.setOpacity(mode.equals(BoardView3D.Mode.CHOOSE_PRODUCTION)?1:0);

        productionButton.setOnAction(e -> {
            if (mode.equals(BoardView3D.Mode.CHOOSE_PRODUCTION))
                ProductionViewBuilder.sendProduce();
            System.out.println(cache.getCurrentState());
            productionButton.setOpacity(0);
            basicButton.setOpacity(0);
        });


        prodList.add(productionButton);

        //setProdAvailable(simpleCardCells,0, basic);
        Map<Integer, Optional<DevelopmentCardAsset>> frontCards=simpleCardCells.getDevCardsOnTop();

        for (Map.Entry<Integer, Optional<DevelopmentCardAsset>> entry : frontCards.entrySet()) {
            Integer key = entry.getKey();
            Optional<DevelopmentCardAsset> value = entry.getValue();
            ImagePattern tempImage;
            Path path;
            Rectangle rectangle = new Rectangle(462, 698);
            if (value.isEmpty()) {
                tempImage = new ImagePattern(new Image("assets/devCards/grayed out/BACK/Masters of Renaissance__Cards_BACK_BLUE_1.png"));
                rectangle.setOpacity(1);
            } else {
                path = simpleCardCells.getDevCardsCells().get(key).orElseThrow()
                        .get(simpleCardCells.getDevCardsCells().get(key).orElseThrow().size()-1).getCardPaths().getKey();
                tempImage = CardSelector.imagePatternFromAsset(path);
            }

            setProdAvailable(simpleCardCells,key, rectangle);

            rectangle.setLayoutX(400 + 250 * key);
            rectangle.setLayoutY(0);
            rectangle.setOnMouseClicked(p -> {
                System.out.println("Card id is: "+value.map(CardAsset::getCardId));
                if (mode.equals(BoardView3D.Mode.CHOOSE_POS_FOR_CARD) && (true || simpleCardCells.isSpotAvailable(key))) {
                    CardShopViewBuilder.sendCardPlacementPosition(key);
                } else if (mode.equals(BoardView3D.Mode.CHOOSE_PRODUCTION) && ( true || simpleCardCells.isProductionAtPositionAvailable(key).orElse(false)))
                    ProductionViewBuilder.sendChosenProduction(key);
            });
            rectangle.setFill(tempImage);


            NodeAdder.shiftAndAddToList(prodList, rectangle, new Point3D(20 + 220 * key, 700, -20));
        }

         Map<Integer,LeaderCardAsset> activeBonus = simpleCardCells.getActiveProductionLeaders();

        activeBonus.forEach((key, value) -> {
            Rectangle temp;
            temp = new Rectangle(462, 698);

            temp.setFill(CardSelector.imagePatternFromAsset(value.getCardPaths().getKey()));
            setProdAvailable(simpleCardCells,key, temp);
            System.out.println(value.getCardId());
            temp.setOnMouseClicked(p -> {
                System.out.println(key);
                System.out.println(value.getCardId());
                if (mode.equals(BoardView3D.Mode.CHOOSE_POS_FOR_CARD) && (true || simpleCardCells.isSpotAvailable(key))) {
                    CardShopViewBuilder.sendCardPlacementPosition(key);
                } else if (mode.equals(BoardView3D.Mode.CHOOSE_PRODUCTION) && (true || simpleCardCells.isProductionAtPositionAvailable(key).orElse(false)))
                    ProductionViewBuilder.sendChosenProduction(key);
            });
            NodeAdder.shiftAndAddToList(prodList, temp, new Point3D(420 + (470 * key), 700, -20));
        });

        prodGroup.getChildren().setAll(prodList);
    }

    /**
     * This method is used to validate a production selection
     * @param simpleCardCells is the player's simplecardcells
     * @param prodPos is the production to validate's position
     * @param cell is the representation of the production
     */
    private void setProdAvailable(SimpleCardCells simpleCardCells, int prodPos, Rectangle cell) {

        BoardView3D.Mode mode = view3D.mode;
        if (mode.equals(BoardView3D.Mode.CHOOSE_POS_FOR_CARD))
            setSelectedColor(cell,simpleCardCells.isSpotAvailable(prodPos));
        else if (mode.equals(BoardView3D.Mode.CHOOSE_PRODUCTION))
            setSelectedColor(cell,simpleCardCells.getSimpleProductions().isProductionAtPositionAvailable(prodPos).orElse(false));
        else setSelectedColor(cell,false);
    }

    /**
     * This method is used to assign a color to a rectangle representing a card
     * @param cell is the card representation
     * @param enabled is the card validation result
     */
    private void setSelectedColor(Rectangle cell, boolean enabled) {
        double v = enabled?+0.40:0;
        ColorAdjust colorAdjust=new ColorAdjust();
        colorAdjust.setBrightness(+v);
        cell.setEffect(colorAdjust);
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //Event is a state
        if (evt.getPropertyName().equals(evt.getNewValue())&&view3D.mode.equals(BoardView3D.Mode.BACKGROUND))
            Platform.runLater(this::updateProds);
    }
}
