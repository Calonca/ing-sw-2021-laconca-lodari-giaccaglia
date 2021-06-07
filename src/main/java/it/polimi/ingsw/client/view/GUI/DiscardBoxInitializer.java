package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.GUI.GUIelem.ResourceButton;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.SimpleDiscardBox;
import it.polimi.ingsw.server.model.Resource;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * The market is generated via a subscene that is attached to the main scene
 */
public class DiscardBoxInitializer extends ResourceMarketViewBuilder implements GUIView {


    javafx.collections.ObservableList<Boolean> selected;

    List<ResourceButton> sceneButtons=new ArrayList<>();
    public AnchorPane discardPane;
    double width=200;
    double len=200;

    /**
     * This runnable will convert the received discardbox for resource choosing
     */
    @Override
    public void run() {

        SimpleDiscardBox sd = getClient().getSimpleModel().getPlayerCache(getClient().getCommonData().getThisPlayerIndex()).getElem(SimpleDiscardBox.class).orElseThrow();
        Map<Integer, Pair<ResourceAsset, Integer>> discardBoxMap = sd.getResourceMap();
        int[] temp=new int[]{0,0,0,0};
        int startpos=-4;
        for(int i=0;i<4;i++)
        {
            //todo fix stone
            temp[i]+=discardBoxMap.get(startpos).getValue();
            startpos++;
        }
        ViewPersonalBoard.getController().setAllowedRes(temp);
        fillDiscardBox();
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


    /**
     * This method will append the discard box to the current view
     */
    public void fillDiscardBox()
    {
        SubScene toadd=getRoot();
        toadd.setTranslateX(-48);
        toadd.setTranslateY(-300);
        toadd.setId("DISCARD");

        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(toadd);
        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        ResourceButton button1=new ResourceButton();
        ResourceButton button2=new ResourceButton();
        ResourceButton button3=new ResourceButton();
        ResourceButton button4=new ResourceButton();

        button1.setLayoutY(len/2);
        button1.setLayoutX(width/2);
        sceneButtons.add(button1);
        discardPane.getChildren().add(button1);

        button2.setLayoutY(len/4);
        button2.setLayoutX(width/4);
        sceneButtons.add(button2);
        discardPane.getChildren().add(button2);


        button3.setLayoutY(len/2);
        button3.setLayoutX(width/4);
        sceneButtons.add(button3);
        discardPane.getChildren().add(button3);



        button4.setLayoutY(len/4);
        button4.setLayoutX(width/2);
        sceneButtons.add(button4);
        discardPane.getChildren().add(button4);




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