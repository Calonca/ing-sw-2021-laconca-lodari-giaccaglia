package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.abstractview.MiddlePhaseViewBuilder;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MiddlePhaseGUI extends MiddlePhaseViewBuilder implements GUIView {
    public Button cardButton;
    public Button productionButton;
    public Button resourceMarketButton;

    @Override
    public void run() {
        SubScene toadd=getRoot();
        toadd.setId("MIDDLE");
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(toadd);
        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());

    }

    public SubScene getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/MiddlePhase.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SubScene(root,500,400);

    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(State.CHOOSING_POSITION_FOR_RESOURCES.name()))
        {
            DiscardBoxInitializer disc=new DiscardBoxInitializer();
            Platform.runLater(disc);
        }
    }

    public void sendChoice(Choice choice) {

        sendMessage(choice);
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().remove(3);


    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Todo use MiddlePhaseViewBuilder.sendMessage(Choice.PRODUCTION);
        // MiddlePhaseViewBuilder.sendMessage(Choice.RESOURCE_MARKET);
        // MiddlePhaseViewBuilder.sendMessage(Choice.CARD_SHOP);
        // to send the messages to the client
        cardButton.setOnAction( e ->
        {

            sendChoice(Choice.CARD_SHOP);
            ViewPersonalBoard.getController().isCardShopOpen(true);

        });
        productionButton.setOnAction( e ->
        {

            sendChoice(Choice.PRODUCTION);
            ViewPersonalBoard.getController().isProduction(true);

        });
        resourceMarketButton.setOnAction( e ->
        {

            sendChoice(Choice.RESOURCE_MARKET);
            ViewPersonalBoard.getController().isMarket(true);

        });


    }
}
