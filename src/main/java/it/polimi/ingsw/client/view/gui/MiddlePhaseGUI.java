package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.abstractview.MiddlePhaseViewBuilder;
import it.polimi.ingsw.network.simplemodel.SimpleCardShop;
import it.polimi.ingsw.network.simplemodel.SimpleProductions;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MiddlePhaseGUI extends MiddlePhaseViewBuilder implements GUIView {

    public Button cardButton;
    public Button productionButton;
    public Button resourceMarketButton;
    public AnchorPane middleAnchor;

    double middleWidth=500;
    double middleLen=400;

    /**
     * This runnable appends the scene to the current view
     */
    @Override
    public void run() {
        SubScene root=getRoot();
        root.setId("MIDDLE");
        GUI.addLast(root);

    }

    /**
     * This method gets called every MIDDLE_PHASE
     * @return the middle phase subscene
     */
    public SubScene getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/MiddlePhase.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SubScene(root,middleWidth,middleLen);

    }


    /**
     * This method is used to send the corresponding choice's message
     * @param choice is a valid choice
     */
    public void sendChoice(Choice choice) {

        sendMessage(choice);
        GUI.removeLast();

    }

    /**
     * Scene buttons get bound to controller
     * @param url is ignored
     * @param resourceBundle is ignored
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //TODO FIX MARKET REFRESH
        Playground.refreshCardShop();
        Playground.refreshMarket();

        cardButton.setOnAction( p ->
        {
            sendChoice(Choice.CARD_SHOP);
        });
        SimpleCardShop simpleCardShop=getSimpleModel().getElem(SimpleCardShop.class).orElseThrow();

        //TODO FIX
        if(!simpleCardShop.getIsAnyCardPurchasable())
            cardButton.setDisable(true);

        productionButton.setOnAction( e -> sendChoice(Choice.PRODUCTION));

        SimpleProductions simpleProductions=getThisPlayerCache().getElem(SimpleProductions.class).orElseThrow();

        productionButton.setDisable(!simpleProductions.isAnyProductionAvailable());

        resourceMarketButton.setOnAction( e -> sendChoice(Choice.RESOURCE_MARKET));
        middleAnchor.setId("middlePane");


    }


}
