package it.polimi.ingsw.client.view.GUI;


import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.GUI.GUIelem.ButtonSelectionModel;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;
import it.polimi.ingsw.network.util.Util;
import it.polimi.ingsw.server.model.Resource;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;
import javafx.util.Pair;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;

/**
 * This scene is composed by a small card selector (2max), a resource selector and a confirmation button
 */
public class SetupPhase extends  it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder implements GUIView {


    private final int LEADERNUMBER=4;

    List<UUID> leadersUUIDs=new ArrayList<>();

    List<Button> sceneLeaders =new ArrayList<>();
    List<Button> sceneResources=new ArrayList<>();

    javafx.collections.ObservableList<Boolean> selectedLeaders;
    javafx.collections.ObservableList<Integer> selectedResources;

    DropShadow effect=new DropShadow();
    double leadersOffsetX=200;
    double resourceSupplyY=70;
    double width=GUI.GUIwidth;
    double len=GUI.GUIlen;
    double leaderSize=len/3+40;
    double cardTilt=1;
    double resourcesOffsetX=-20;
    double leadersSpacing=width/5;

    @FXML
    private AnchorPane setupAnchor;

    SimplePlayerLeaders simplePlayerLeaders = getSimpleModel().getPlayerCache(getClient().getCommonData().getThisPlayerIndex()).getElem(SimplePlayerLeaders.class).orElseThrow();
    List<LeaderCardAsset> leaderCardAssets=simplePlayerLeaders.getPlayerLeaders();


    @Override
    public void run() {
        SubScene root=getRoot();
    /*    root.translateYProperty().set(getClient().getStage().getScene().getHeight());
        Timeline timeline=new Timeline();
        KeyValue kv= new KeyValue(root.translateYProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf= new KeyFrame(Duration.seconds(0.5),kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();*/
        root.setId("SETUP");
        GUI.getRealPane().getChildren().remove(0);
        GUI.getRealPane().getChildren().add(root);

        System.out.println(GUI.getRealPane().getChildren());

    }

    public SubScene getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/SetupMenu.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SubScene(root,width,len);

    }

    public List<ImageView> getSetupLeaderIcons() {
        SimplePlayerLeaders simplePlayerLeaders = getSimpleModel().getPlayerCache(getClient().getCommonData().getThisPlayerIndex()).getElem(SimplePlayerLeaders.class).orElseThrow();
        List<LeaderCardAsset> leaderCardAssets=simplePlayerLeaders.getPlayerLeaders();
        List<ImageView> resultList=new ArrayList<>();

        for (LeaderCardAsset leaderCardAsset : leaderCardAssets) {
            Path path = leaderCardAsset.getCardPaths().getKey();
            leadersUUIDs.add(leaderCardAsset.getCardId());
            ImageView temp = new ImageView(new Image(path.toString(), true));
            temp.setPreserveRatio(true);
            temp.setFitHeight(leaderSize);
            resultList.add(temp);
        }
        return resultList;

    }

    /**
     * service method
     * @return a button according to parameters
     */
    public Button validationButton()
    {
        Button confirm=new Button();
        confirm.setText("CONFIRM");
        confirm.setLayoutY(len/2);
        confirm.setLayoutX(width/2);
        confirm.setEffect(effect);
        confirm.setOnAction(p -> {

            SetupPhaseEvent event = new SetupPhaseEvent(Util.resourcesToChooseOnSetup(getCommonData().getThisPlayerIndex()),2,getClient().getCommonData().getThisPlayerIndex());

            for(int i = 0; i< sceneLeaders.size(); i++)
                if(selectedLeaders.get(i))
                    event.addChosenLeader(leadersUUIDs.get(i));

            List<Integer> converter=new ArrayList<>();
            for(int i=0;i<selectedResources.size();i++)
                if (selectedResources.get(i) ==1)
                    converter.add(i);
                else if(selectedResources.get(i) ==2)
                {
                    converter.add(i);
                    converter.add(i);
                }
            if (converter.size()>0)
                event.addResource(new Pair<>(0,converter.get(0)));
            if (converter.size()>1)
                event.addResource(new Pair<>(1,converter.get(1)));
            //System.out.println(event);
            getClient().getServerHandler().sendCommandMessage(new EventMessage(event));




        });
        return confirm;
    }

    public void spawnResourceSupply()
    {
        ImageView resourceSupply=new ImageView(new Image("assets/punchboard/ResourceSupply.png"));
        resourceSupply.setFitWidth(width*2/3);
        resourceSupply.setPreserveRatio(true);
        resourceSupply.setLayoutX(width/6);
        resourceSupply.setLayoutY(resourceSupplyY);
        resourceSupply.setEffect(effect);
        setupAnchor.getChildren().add(resourceSupply);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {


        spawnResourceSupply();

        Label leaders=new Label("SELECT TWO LEADERS");
        leaders.setLayoutX(width/2+100);
        leaders.setLayoutY(len/2);
        setupAnchor.getChildren().add(leaders);

        Button leaderBut;
        for(int i=0;i<getSetupLeaderIcons().size();i++)
        {
            leaderBut= new Button();
            leaderBut.setEffect(SetupPhase.this.effect);
            leaderBut.setLayoutY(len-leaderSize-10);
            leaderBut.setLayoutX(leadersOffsetX+leadersSpacing*i);
            leaderBut.setGraphic(getSetupLeaderIcons().get(i));
            leaderBut.setId("leaderButton");
            leaderBut.setRotate(Math.random() * (cardTilt - -cardTilt + 1) + -1 );
            setupAnchor.getChildren().add(leaderBut);
            sceneLeaders.add(leaderBut);
        }


        selectedLeaders=javafx.collections.FXCollections.observableArrayList();
        for (int i=0;i<LEADERNUMBER;i++)
            selectedLeaders.add(false);


        ViewPersonalBoard.getController().cardSelector(selectedLeaders, sceneLeaders,2);

        selectedLeaders.addListener((ListChangeListener<Boolean>) c -> {
            c.next();
            if(c.getAddedSubList().get(0))
                sceneLeaders.get(c.getFrom()).setLayoutY(sceneLeaders.get(c.getFrom()).getLayoutY()-30);
            else
                sceneLeaders.get(c.getFrom()).setLayoutY(sceneLeaders.get(c.getFrom()).getLayoutY()+30);


        });

        selectedResources =javafx.collections.FXCollections.observableArrayList();

        List<Image> res=new ArrayList<>();
        res.add(new Image("assets/resources/GOLD.png"));
        res.add(new Image("assets/resources/SERVANT.png"));
        res.add(new Image("assets/resources/SHIELD.png"));
        res.add(new Image("assets/resources/STONE.png"));

        Button resbut;
        for(int i=0;i<4;i++)
        {
            resbut= new Button();
            resbut.setLayoutY(resourceSupplyY+width/16);
            resbut.setLayoutX(width/6+ width/12 + (i)*width/6 + resourcesOffsetX);
            ImageView tempImage=new ImageView(res.get(i));
            tempImage.setPreserveRatio(true);
            tempImage.setFitWidth(50);
            resbut.setId("resourceButton");
            resbut.setGraphic(tempImage);
            setupAnchor.getChildren().add(resbut);
            sceneResources.add(resbut);
            selectedResources.add(0);

        }

        double resourcesX=width/2;
        ViewPersonalBoard.getController().setAllowedRes(new int[]{3,3,3,3});
        ViewPersonalBoard.getController().bindDispenser(selectedResources,sceneResources,Util.resourcesToChooseOnSetup(getClient().getCommonData().getThisPlayerIndex()));
        selectedResources.addListener((ListChangeListener<Integer>) c -> {
            c.next();
            ImageView tempImage=new ImageView(res.get(c.getFrom()));
            tempImage.setLayoutX(resourcesX);
            tempImage.setLayoutY(len/2 - 50);
            setupAnchor.getChildren().add(tempImage);
            //getClient().getStage().getScene().setCursor(new ImageCursor(Color.getRes().get(c.getFrom())));
        });
        setupAnchor.getChildren().add(validationButton());
        setupAnchor.setId("background");
        getClient().getStage().show();

    }


}