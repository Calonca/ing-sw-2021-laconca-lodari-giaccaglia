package it.polimi.ingsw.client.view.GUI;


import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.GUI.layout.ResChoiceRowGUI;
import it.polimi.ingsw.client.view.GUI.util.CardSelector;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;
import it.polimi.ingsw.network.util.Util;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * This scene is composed by a small card selector (2max), a resource selector and a confirmation button
 */
public class SetupPhase extends  it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder implements GUIView {


    private final int LEADERNUMBER=4;

    List<UUID> leadersUUIDs=new ArrayList<>();

    List<Button> sceneResources=new ArrayList<>();
    double resourceSize=50;
    ResChoiceRowGUI resourceSelector;
    List<ImageView> resourceImageViews=new ArrayList<>();
    List<ImageView> leaderImageViews =new ArrayList<>();

    javafx.collections.ObservableList<Boolean> selectedLeaders;
    javafx.collections.ObservableList<Integer> selectedResources;

    DropShadow effect=new DropShadow();
    double leadersOffsetX=200;

    double resourceSupplyY=70;
    double resourcesCountLabelOffsetX=250;
    double width=GUI.GUIwidth;
    double len=GUI.GUIlen;
    double leaderSize=len/3+40;
    double cardTilt=1;
    double resourcesOffsetX=-20;
    double leadersSpacing=width/5;


    @FXML
    private AnchorPane setupAnchor;

   // SimplePlayerLeaders simplePlayerLeaders = getSimpleModel().getPlayerCache(getClient().getCommonData().getThisPlayerIndex()).getElem(SimplePlayerLeaders.class).orElseThrow();
  //  List<LeaderCardAsset> leaderCardAssets=simplePlayerLeaders.getPlayerLeaders();


    /**
     * This method appends the Setup Phase Subscene to the current view
     */
    @Override
    public void run() {
        SubScene root=getRoot();

        root.translateYProperty().set(GUI.getRealPane().getHeight());
        Timeline timeline=new Timeline();
        KeyValue kv= new KeyValue(root.translateYProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf= new KeyFrame(Duration.seconds(0.5),kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        root.setId("SETUP");
        GUI.getRealPane().getChildren().remove(0);
        GUI.addLast(root);

        System.out.println(GUI.getRealPane().getChildren());

    }


    /**
     * This method is called once during setup phase initialization or in case of invalid input
     * @return the Setup Phase subscene
     */
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


    /**
     * This is a helper method used to centralize the card acquisition
     * @return the corresponding List of ImageViews
     */
    public List<ImageView> getSetupLeaderIcons() {
        SimplePlayerLeaders simplePlayerLeaders = getSimpleModel().getPlayerCache(getClient().getCommonData().getThisPlayerIndex()).getElem(SimplePlayerLeaders.class).orElseThrow();
        List<LeaderCardAsset> leaderCardAssets=simplePlayerLeaders.getPlayerLeaders();
        List<ImageView> resultList=new ArrayList<>();

        for (LeaderCardAsset leaderCardAsset : leaderCardAssets) {
            leadersUUIDs.add(leaderCardAsset.getCardId());

            ImageView temp = CardSelector.imageViewFromAsset(leaderCardAsset.getCardPaths().getKey());
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

            int resToChoose = Util.resourcesToChooseOnSetup(CommonData.getThisPlayerIndex());
            SetupPhaseEvent event = new SetupPhaseEvent(resToChoose,2, CommonData.getThisPlayerIndex());

            for(int i = 0; i< leaderImageViews.size(); i++)
                if(selectedLeaders.get(i))
                    event.addChosenLeader(leadersUUIDs.get(i));

            System.out.println(resToChoose);

            if (resToChoose>0)
                event.addResource(new Pair<>(0,resourceSelector.getChosenOutputRes().get(0)));
            if (resToChoose>1)
                event.addResource(new Pair<>(1,resourceSelector.getChosenOutputRes().get(1)));

            System.out.println(JsonUtility.serialize(event));
            setupAnchor.getChildren().clear();
            getClient().getServerHandler().sendCommandMessage(new EventMessage(event));

        });
        return confirm;
    }

    /**
     * Service method used to center an image
     */
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


    /**
     * This method initializes four resource buttons to choose from and four leaders, that are togglable, and a confirmation button
     * @param url is ignored
     * @param resourceBundle is ignored
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {



        Label leaders=new Label("SELECT TWO LEADERS");
        leaders.setLayoutX(width/2+100);
        leaders.setLayoutY(len/2);
        setupAnchor.getChildren().add(leaders);


        spawnResourceSupply();

        Effect dropShadow=new DropShadow(BlurType.GAUSSIAN, Color.rgb(0,0,0,0.5),10,0.7,5,5);


        ImageView leaderBut;
        for(int i=0;i<getSetupLeaderIcons().size();i++)
        {
            leaderBut=getSetupLeaderIcons().get(i);
            leaderBut.setLayoutX(leadersOffsetX+leadersSpacing*i);
            leaderBut.setLayoutY(len-leaderSize-10);
            leaderBut.setEffect(dropShadow);
            leaderBut.setRotate(Math.random() * (cardTilt - -cardTilt + 1) + -1 );
            setupAnchor.getChildren().add(leaderBut);
            leaderImageViews.add(leaderBut);
        }


        selectedLeaders=javafx.collections.FXCollections.observableArrayList();
        for (int i=0;i<getSetupLeaderIcons().size();i++)
            selectedLeaders.add(false);

        CardSelector cardSelector=new CardSelector();
        cardSelector.cardSelectorFromImage(selectedLeaders,leaderImageViews,2);

        //ViewPersonalBoard.getController().cardSelector(selectedLeaders, sceneLeaders,2);
        selectedLeaders.addListener((ListChangeListener<Boolean>) c -> {
            c.next();
            if(c.getAddedSubList().get(0))
                leaderImageViews.get(c.getFrom()).setLayoutY(leaderImageViews.get(c.getFrom()).getLayoutY()-30);
            else
                leaderImageViews.get(c.getFrom()).setLayoutY(leaderImageViews.get(c.getFrom()).getLayoutY()+30);


        });

        selectedResources =javafx.collections.FXCollections.observableArrayList();

        resourceImageViews=new ArrayList<>();
        resourceImageViews.add(new ImageView(new Image("assets/resources/GOLD.png")));
        resourceImageViews.add(new ImageView(new Image("assets/resources/SERVANT.png")));
        resourceImageViews.add(new ImageView(new Image("assets/resources/SHIELD.png")));
        resourceImageViews.add(new ImageView(new Image("assets/resources/STONE.png")));


        for(int i=0;i<resourceImageViews.size();i++)
        {
            ImageView tempImage=resourceImageViews.get(i);
            tempImage.setPreserveRatio(true);
            tempImage.setLayoutY(resourceSupplyY+width/16);
            tempImage.setLayoutX(width/6+ width/12 + (i)*width/6 + resourcesOffsetX);
            tempImage.setFitWidth(resourceSize);
            tempImage.setEffect(dropShadow);
            setupAnchor.getChildren().add(tempImage);
            selectedResources.add(0);

        }


        int resourcesCount=Util.resourcesToChooseOnSetup(CommonData.getThisPlayerIndex());


        List<ResourceAsset> resourcesToChoose =new ArrayList<>();
        for(int i=0;i<resourcesCount;i++)
            resourcesToChoose.add(ResourceAsset.TO_CHOOSE);

        resourceSelector=new ResChoiceRowGUI(0, new ArrayList<>(),resourcesToChoose);


        Group group=resourceSelector.getRowGroup();
        group.setScaleX(0.4);
        group.setScaleY(0.4);
        group.setScaleZ(0.4);

        group.setLayoutX(1700);
        group.setLayoutY(100);

        setupAnchor.getChildren().add(group);

        for(int i=0;i<resourceImageViews.size();i++) {
            int finalI = i;
            resourceImageViews.get(i).setOnMouseClicked(p->
                    {
                        if(resourceSelector.getPointedResource().isEmpty())
                            return;
                        resourceSelector.setNextInputPos(0,ResourceAsset.fromInt(finalI));
                        System.out.println("CHOOSIN");
                    });
        }

        if(resourcesCount!=0)
        {
            Label resources=new Label("SELECT"+  resourcesCount +"RESOURCES");
            resources.setLayoutX(width/2+resourcesCountLabelOffsetX);
            resources.setLayoutY(len/2);
            setupAnchor.getChildren().add(resources);
        }


        setupAnchor.getChildren().add(validationButton());
        setupAnchor.setId("background3");
        getClient().getStage().show();

    }




}