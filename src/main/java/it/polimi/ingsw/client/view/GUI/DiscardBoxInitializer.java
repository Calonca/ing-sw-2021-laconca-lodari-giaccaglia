package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.GUI.GUIelem.ButtonSelectionModel;
import it.polimi.ingsw.client.view.GUI.GUIelem.ResourceButton;
import it.polimi.ingsw.client.view.GUI.GUIelem.ResourceSphere;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.server.model.Resource;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.spi.ResourceBundleProvider;

/**
 * The market is generated via a subscene that is attached to the main scene
 */
public class DiscardBoxInitializer extends ResourceMarketViewBuilder implements GUIView {


    List<ResourceButton> sceneButtons=new ArrayList<>();
    public AnchorPane discardPane;

    @Override
    public void run() {


    }

    public SubScene getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/DiscardBox.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SubScene(root,200,200);

    }

    public void fillDiscardBox()
    {
        Node toadd=getRoot();
        toadd.setTranslateX(-400);
        toadd.setTranslateY(-300);
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(toadd);
        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        ResourceButton but=new ResourceButton();
        but.setResource(Resource.SERVANT);
        but.color();
        but.setLayoutY(100);
        but.setLayoutX(100);
        sceneButtons.add(but);
        discardPane.getChildren().add(but);
        but= new ResourceButton();
        but.setResource(Resource.EMPTY);
        but.color();
        but.setLayoutY(50);
        but.setLayoutX(50);
        sceneButtons.add(but);
        discardPane.getChildren().add(but);
        but= new ResourceButton();
        but.setResource(Resource.EMPTY);
        but.color();
        but.setLayoutY(100);
        but.setLayoutX(50);
        sceneButtons.add(but);
        discardPane.getChildren().add(but);
        but= new ResourceButton();
        but.setResource(Resource.EMPTY);
        but.color();
        but.setLayoutY(50);
        but.setLayoutX(100);
        sceneButtons.add(but);
        discardPane.getChildren().add(but);

        ViewPersonalBoard.getController().bindToTransfer(sceneButtons);





    }

    /**
     * Called every time a resource is moved in the personalBoard or when the discardBox is received
     */
    @Override
    public void choosePositions() {

    }

}