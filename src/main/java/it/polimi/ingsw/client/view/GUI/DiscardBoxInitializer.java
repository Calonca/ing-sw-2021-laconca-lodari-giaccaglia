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
public class DiscardBoxInitializer extends ResourceMarketViewBuilder implements GUIView {


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

        return new SubScene(root,width,len);

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
        Button button1=new Button();
        Button button2=new Button();
        Button button3=new Button();
        Button button4=new Button();

        ImageView resourceSupply=new ImageView(new Image("assets/punchboard/ResourceSupply.png"));
        resourceSupply.setFitWidth(width);
        resourceSupply.setFitHeight(len);
        discardPane.getChildren().add(resourceSupply);
        sceneButtons.add(button1);
        discardPane.getChildren().add(button1);

        sceneButtons.add(button2);
        discardPane.getChildren().add(button2);

        sceneButtons.add(button3);
        discardPane.getChildren().add(button3);

        sceneButtons.add(button4);
        discardPane.getChildren().add(button4);

        for(int i=0;i<sceneButtons.size();i++)
        {
            sceneButtons.get(i).setLayoutX(width/8+i*width/4-15);
            sceneButtons.get(i).setLayoutY(len/2-10);
        }


        List<Resource> dispensed=new ArrayList<>();
        dispensed.add(Resource.GOLD);
        dispensed.add(Resource.SERVANT);
        dispensed.add(Resource.SHIELD);
        dispensed.add(Resource.STONE);

        selected=javafx.collections.FXCollections.observableArrayList();

        for(Button into : sceneButtons)
            selected.add(0);

    //todo fix
        List<Image> res=new ArrayList<>();
            res.add(new Image("assets/resources/GOLD.png"));
            res.add(new Image("assets/resources/SERVANT.png"));
            res.add(new Image("assets/resources/SHIELD.png"));
            res.add(new Image("assets/resources/STONE.png"));


        for(int i=0;i<sceneButtons.size();i++)
        {
            ImageView tempResize=new ImageView((res.get(i)));
            tempResize.setFitHeight(10);
            tempResize.setFitWidth(10);
            sceneButtons.get(i).setGraphic(tempResize);

        }
        ViewPersonalBoard.getController().bindDispenser(selected,sceneButtons,10);


        selected.addListener((ListChangeListener<Integer>) c -> {
            c.next();
            getClient().getStage().getScene().setCursor(new ImageCursor(res.get(c.getFrom())));
            System.out.println(selected);

        });





    }

    /**
     * Called every time a resource is moved in the personalBoard or when the discardBox is received
     */
    @Override
    public void choosePositions() {

    }

}