package it.polimi.ingsw.client.view.GUI;
import javafx.scene.control.Button;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.SimpleDiscardBox;
import it.polimi.ingsw.server.model.Resource;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * The market is generated via a subscene that is attached to the main scene
 */
public class DiscardBoxInitializer extends ResourceMarketViewBuilder  {


    javafx.collections.ObservableList<Integer> selected;
    Button cicic;
    List<Button> sceneButtons=new ArrayList<>();
    public AnchorPane discardPane;
    double width=200;
    double len=width/4;

    /**
     * This runnable will convert the received discardbox for resource choosing
     */
    @Override
    public void run() {

        GUI.addLast(fillDispenser());
    }



    /**
     * This method will append the discard box to the current view
     */
    public  ImageView fillDispenser()
    {

        AnchorPane discardPane=new AnchorPane();
        discardPane.setMinSize(width,len);
        ImageView resourceSupply=new ImageView(new Image("assets/punchboard/ResourceSupply.png"));
        resourceSupply.setFitWidth(width);
        resourceSupply.setFitHeight(len);
        discardPane.getChildren().add(resourceSupply);

        for(int i=0;i<sceneButtons.size();i++)
        {
            sceneButtons.get(i).setLayoutX(width/8+i*width/4-15);
            sceneButtons.get(i).setLayoutY(len/2-10);
        }


        return resourceSupply;

    }




    /**
     * Called every time a resource is moved in the personalBoard or when the discardBox is received
     */
    @Override
    public void choosePositions() {

    }

}