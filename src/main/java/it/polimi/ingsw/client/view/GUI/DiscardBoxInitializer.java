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
import javafx.scene.image.Image;
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

    ResourceButton button1=new ResourceButton();
    ResourceButton button2=new ResourceButton();
    ResourceButton button3=new ResourceButton();
    ResourceButton button4=new ResourceButton();
    javafx.collections.ObservableList<Boolean> selected;

    List<Resource> resourcesToBind=new ArrayList<>();
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


    public void setResourcesToBind(List<Resource> resourcesToBind) {
        this.resourcesToBind = resourcesToBind;
    }


    public void fillDiscardBox()
    {
        Node toadd=getRoot();
        toadd.setTranslateX(-400);
        toadd.setTranslateY(-300);
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(toadd);
        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());
    }

    public void setResources(List<Resource> toPick)
    {
        button1.setResource(toPick.get(0));
        button1.color();
        getClient().getStage().show();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

        button1.setLayoutY(100);
        button1.setLayoutX(100);
        sceneButtons.add(button1);
        discardPane.getChildren().add(button1);

        button2.setLayoutY(50);
        button2.setLayoutX(50);
        sceneButtons.add(button2);
        discardPane.getChildren().add(button2);


        button3.setLayoutY(100);
        button3.setLayoutX(50);
        sceneButtons.add(button3);
        discardPane.getChildren().add(button3);



        button4.setLayoutY(50);
        button4.setLayoutX(100);
        sceneButtons.add(button4);
        discardPane.getChildren().add(button4);



        button3.setResource(Resource.SERVANT);
        button4.setResource(Resource.GOLD);
        button3.color();
        button4.color();

        List<Resource> dispensed=new ArrayList<>();
        dispensed.add(Resource.GOLD);
        dispensed.add(Resource.STONE);
        dispensed.add(Resource.SERVANT);
        dispensed.add(Resource.SHIELD);

        selected=javafx.collections.FXCollections.observableArrayList();

        for(ResourceButton res : sceneButtons)
            selected.add(true);
        for(int i=0;i<sceneButtons.size();i++)
        {
            sceneButtons.get(i).setResource(dispensed.get(i));
            sceneButtons.get(i).color();

        }
        ViewPersonalBoard.getController().setAllowedRes(new int[]{1,0,1,0});
        ViewPersonalBoard.getController().bindDispenser(selected,sceneButtons);

        List<Image> res=new ArrayList<>();
        res.add(new Image("assets/resources/GOLD.png"));
        res.add(new Image("assets/resources/SERVANT.png"));
        res.add(new Image("assets/resources/SHIELD.png"));
        res.add(new Image("assets/resources/STONE.png"));
        selected.addListener(new javafx.collections.ListChangeListener<Boolean>() {
            @Override
            public void onChanged(Change<? extends Boolean> c) {
                c.next();
                if(!c.getAddedSubList().get(0))
                    getClient().getStage().getScene().setCursor(new ImageCursor(res.get(sceneButtons.get(c.getFrom()).getResource().getResourceNumber())));
                else
                    getClient().getStage().getScene().setCursor(ImageCursor.HAND);


            }});





    }

    /**
     * Called every time a resource is moved in the personalBoard or when the discardBox is received
     */
    @Override
    public void choosePositions() {

    }

}