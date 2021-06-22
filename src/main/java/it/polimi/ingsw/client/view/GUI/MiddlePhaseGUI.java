package it.polimi.ingsw.client.view.GUI;
import it.polimi.ingsw.client.view.abstractview.MiddlePhaseViewBuilder;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import it.polimi.ingsw.network.simplemodel.SimpleCardShop;
import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;
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

    /**
     * This runnable appends the scene to the current view
     */
    @Override
    public void run() {
        SubScene root=getRoot();
        root.setId("MIDDLE");
        GUI.getRealPane().getChildren().add(root);
        BoardView3D.getBoard().refreshMarket();
        System.out.println(GUI.getRealPane().getChildren());
        SimpleFaithTrack faithTrack =  getThisPlayerCache().getElem(SimpleFaithTrack.class).orElseThrow();
        System.out.println(faithTrack.getPlayerPosition());
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




        cardButton.setOnAction( p ->
        {

            sendChoice(Choice.CARD_SHOP);
        });
        SimpleCardShop simpleCardShop=getSimpleModel().getElem(SimpleCardShop.class).orElseThrow();
        System.out.println(simpleCardShop.getIsAnyCardPurchasable());
        if(!simpleCardShop.getIsAnyCardPurchasable())
            cardButton.setDisable(true);

        productionButton.setOnAction( e ->
                sendChoice(Choice.PRODUCTION));

        resourceMarketButton.setOnAction( e ->
        {

            sendChoice(Choice.RESOURCE_MARKET);

        });

        middleAnchor.setId("pane");
    }
}
