package it.polimi.ingsw.client.view.GUI;


import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.GUI.GUIelem.ButtonSelectionModel;
import it.polimi.ingsw.client.view.GUI.GUIelem.ResourceButton;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;
import it.polimi.ingsw.network.util.Util;
import it.polimi.ingsw.server.model.Resource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * This scene is composed by a small card selector (2max), a resource selector and a confirmation button
 */
public class SetupPhase extends  it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder implements GUIView {

    /**
     * By changin LEADERNUMBER the number of received leaders to show can be easily changed
     */
    private final int LEADERNUMBER=4;

    List<UUID> leadersUUIDs=new ArrayList<>();

    List<Button> sceneLeaders =new ArrayList<>();
    List<Button> sceneResources=new ArrayList<>();

    javafx.collections.ObservableList<Boolean> selectedLeaders;
    javafx.collections.ObservableList<Integer> selectedResources;

    List<Color> colors=new ArrayList<>();
    List<String> colorsToRes=new ArrayList<>();


    @FXML
    private AnchorPane cjlAnchor;


    @Override
    public void run() {
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().remove(0);
        SubScene root=getRoot();
        root.setId("SETUP");

        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(root);
        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());

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

        return new SubScene(root,1000,700);

    }

    /**
     *
     * @param selected is a boolean array used to represent selection
     * @param scenesLeadersToChoose is a button array for the user to execute selection
     */




    /**
     * given a color and a position, the method will build one accordingly
     * @param color is a css color string
     * @param y is an int position
     * @param x is an int position
     * @return a spinner according to parameters
     */

    /**
     * service method
     * @return a button according to parameters
     */
    public Button validationButton()
    {
        Button confirm=new Button();
        confirm.setText("CONFIRM");
        confirm.setLayoutY(800);
        confirm.setLayoutX(450);
        confirm.setOnAction(p -> {

               SetupPhaseEvent event = new SetupPhaseEvent(Util.resourcesToChooseOnSetup(getCommonData().getThisPlayerIndex()),2,getClient().getCommonData().getThisPlayerIndex());
            for(int i = 0; i< sceneLeaders.size(); i++)
                if(!selectedLeaders.get(i))
                    event.addDiscardedLeader(leadersUUIDs.get(i));
            getClient().getServerHandler().sendCommandMessage(new EventMessage(event));



        });
        return confirm;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        ButtonSelectionModel selectionModel=new ButtonSelectionModel();
        int y=200;
        int x=120;

        colorsToRes.add(" -fx-background-color: #B8860B");
        colorsToRes.add(" -fx-background-color: #9400D3");
        colorsToRes.add(" -fx-background-color: #0099FF");
        colorsToRes.add(" -fx-background-color: #DEB887");


        Label resNumber=new Label(Integer.toString(Util.resourcesToChooseOnSetup(getCommonData().getThisPlayerIndex())));
        resNumber.setLayoutX(300);
        resNumber.setLayoutY(200);
        cjlAnchor.getChildren().add(resNumber);

        Label leaders=new Label("SELEZIONA DUE LEADER");
        leaders.setLayoutX(300);
        leaders.setLayoutY(500);
        cjlAnchor.getChildren().add(leaders);


        /**
         * Buttons are built according to leadernumber parameter
         */

        SimplePlayerLeaders simplePlayerLeaders = getSimpleModel().getPlayerCache(getClient().getCommonData().getThisPlayerIndex()).getElem(SimplePlayerLeaders.class).orElseThrow();
        List<LeaderCardAsset> leaderCardAssets=simplePlayerLeaders.getPlayerLeaders();

        Button tempbut;
        for(int i=0;i<LEADERNUMBER;i++)
        {
            leadersUUIDs.add(leaderCardAssets.get(i).getCardId());
            Path path= Paths.get(leaderCardAssets.get(i).getCardPaths().getKey().toString().substring(1));
            System.out.println(leaderCardAssets.get(i).getCardPaths().getKey().toString().toString());
            ImageView temp = new ImageView(new Image("assets/leaders/raw/FRONT/Masters of Renaissance_Cards_FRONT_2.png", true));
//new Image(ViewPersonalBoard.class.getResource(leaderCardAssets.get(i).getCardPaths().getKey().toString()).toString()
            tempbut= new Button();
            tempbut.setLayoutY(600);
            tempbut.setLayoutX(100+200*i);
            tempbut.setGraphic(temp);

            temp.setFitHeight(200);
            temp.setFitWidth(200);
            cjlAnchor.getChildren().add(tempbut);
            sceneLeaders.add(tempbut);

        }


        selectedLeaders=javafx.collections.FXCollections.observableArrayList();
        for (int i=0;i<LEADERNUMBER;i++)
            selectedLeaders.add(false);


        selectionModel.cardSelector(selectedLeaders, sceneLeaders,2);

        selectedLeaders.addListener(new javafx.collections.ListChangeListener<Boolean>() {
            @Override
            public void onChanged(Change<? extends Boolean> c) {
                c.next();
                if(c.getAddedSubList().get(0))
                    sceneLeaders.get(c.getFrom()).setLayoutY(sceneLeaders.get(c.getFrom()).getLayoutY()-30);
                else
                    sceneLeaders.get(c.getFrom()).setLayoutY(sceneLeaders.get(c.getFrom()).getLayoutY()+30);


            }});

        selectedResources =javafx.collections.FXCollections.observableArrayList();
        for (int i=0;i<4;i++)
            selectedResources.add(0);



        colors.add(Color.GOLD);
        colors.add(Color.PURPLE);
        colors.add(Color.BLUE);
        colors.add(Color.GREY);

        ResourceButton resbut;
        for(int i=0;i<4;i++)
        {
            resbut= new ResourceButton();
            resbut.setLayoutY(200);
            resbut.setLayoutX(100+200*i);
            resbut.setResource(Resource.fromInt(i));


            resbut.color();
            cjlAnchor.getChildren().add(resbut);
            sceneResources.add(resbut);

        }

        selectionModel.resourceSelector(selectedResources,sceneResources,2);

        selectedResources.addListener(new javafx.collections.ListChangeListener<Integer>() {
            @Override
            public void onChanged(Change<? extends Integer> c) {
                c.next();
                Circle temp=new Circle();
                temp.setLayoutX(430+25* selectedResources.stream().mapToInt(i -> i).sum());
                temp.setLayoutY(400);
                temp.setRadius(20);
                temp.setFill(colors.get(c.getFrom()));
                cjlAnchor.getChildren().add(temp);
                getClient().getStage().show();


            }});
        cjlAnchor.getChildren().add(validationButton());
        getClient().getStage().show();

    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(State.IDLE.name()))
            getClient().changeViewBuilder(new IDLEViewBuilder());
        else{
            SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).orElseThrow();
            List<LeaderCardAsset> leaderCardAssets=simplePlayerLeaders.getPlayerLeaders();
            System.out.println(leaderCardAssets);

        }
    }
}