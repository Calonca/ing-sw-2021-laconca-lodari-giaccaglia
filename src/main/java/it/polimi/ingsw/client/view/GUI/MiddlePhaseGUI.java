package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.abstractview.MiddlePhaseViewBuilder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    public AnchorPane mfAnchor;

    @Override
    public void run() {
        ((Pane) getClient().getStage().getScene().getRoot()).getChildren().remove(0);
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(getRoot());
    }

    public Parent getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/SetupMenu.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return root;

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Todo use MiddlePhaseViewBuilder.sendMessage(Choice.PRODUCTION);
        // MiddlePhaseViewBuilder.sendMessage(Choice.RESOURCE_MARKET);
        // MiddlePhaseViewBuilder.sendMessage(Choice.CARD_SHOP);
        // to send the messages to the client
        cardButton.setOnAction( e -> sendMessage(Choice.CARD_SHOP));
        productionButton.setOnAction( e -> sendMessage(Choice.PRODUCTION));
        resourceMarketButton.setOnAction( e -> sendMessage(Choice.RESOURCE_MARKET));
        ImageView temp = new ImageView(new Image("assets/punchboard/MarketBoard.png", true));
        temp.setFitWidth(100);
        resourceMarketButton.setGraphic(temp);
        temp = new ImageView(new Image("assets/devCards/raw/FRONT/Masters of Renaissance_Cards_FRONT_BLUE_1.png", true));
        temp.setFitWidth(100);
        cardButton.setGraphic(temp);
        temp = new ImageView(new Image("assets/board/smallerboard.png", true));
        temp.setFitWidth(100);
        productionButton.setGraphic(temp);

    }
}
