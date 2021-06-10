package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.abstractview.InitialOrFinalPhaseViewBuilder;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.InitialOrFinalPhaseEvent;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class InitialOrFinalPhaseGUI extends InitialOrFinalPhaseViewBuilder implements GUIView {
    public AnchorPane leaderPane;
    public Button c;
    public Button leaderOne;
    public Button leaderTwo;
    public Button discardButton;
    public Button skipButton;

    public double width=800;
    public double len=600;
    double paddingBetweenCards=50;
    double cardsY=40;
    double cardTilt=1;
    double buttonsDistanceFromBottom=50;
    double cardLen=len*(3.0/4);
    List<UUID> leadersUUIDs=new ArrayList<>();
    double cardsStartingX=70;
    List<Button> sceneLeaders=new ArrayList<>();




    public InitialOrFinalPhaseGUI(boolean isInitial) {
        super(isInitial);
    }

    public InitialOrFinalPhaseGUI() {
        super();
    }

    /**
     * This runnable will append the leader choice scene to the current view
     */
    @Override
    public void run()
    {
        Node toAdd=getRoot();
        toAdd.setId("LEADER");
        if(((Pane)getClient().getStage().getScene().getRoot()).getChildren().size()<4)
            ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(toAdd);
        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());
    }



    public void bindPicturesFromAssets(SimplePlayerLeaders simplePlayerLeaders)
    {
        Path path;
        for(int i=0; i<sceneLeaders.size();i++)
        {
            if(!simplePlayerLeaders.getPlayerLeaders().get(i).getNetworkLeaderCard().isLeaderActive())
            {
                if(simplePlayerLeaders.getPlayerLeaders().get(i).getNetworkLeaderCard().isPlayable())
                    path= simplePlayerLeaders.getPlayerLeaders().get(i).getCardPaths().getKey();
                else
                    path= simplePlayerLeaders.getPlayerLeaders().get(i).getInactiveCardPaths().getKey();
                ImageView tempImage = new ImageView(new Image(path.toString(), true));
                tempImage.setFitHeight(cardLen);
                tempImage.setPreserveRatio(true);
                sceneLeaders.get(i).setGraphic(tempImage);
            }
        }

    }

    public SubScene getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/InitialOrFinalPhase.fxml"));
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
        Path path;

        for (LeaderCardAsset leaderCardAsset : leaderCardAssets) {
            if(!leaderCardAsset.getNetworkLeaderCard().isLeaderActive())
            {
                path = leaderCardAsset.getCardPaths().getKey();
                leadersUUIDs.add(leaderCardAsset.getCardId());
                ImageView temp = new ImageView(new Image(path.toString(), true));
                temp.setPreserveRatio(true);
                temp.setFitHeight(cardLen);
                resultList.add(temp);}
            }
        return resultList;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        javafx.collections.ObservableList<Boolean> selectedLeaders;
        selectedLeaders=javafx.collections.FXCollections.observableArrayList();

        int currentSize=((Pane)getClient().getStage().getScene().getRoot()).getChildren().size();

        SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).orElseThrow();

        Button leaderBut;
        for(int i=0;i<getSetupLeaderIcons().size();i++)
        {
            leaderBut= new Button();
            leaderBut.setLayoutY(cardsY);
            leaderBut.setLayoutX(cardsStartingX+i*(cardLen*(462.0/709)+paddingBetweenCards));
            leaderBut.setGraphic(getSetupLeaderIcons().get(i));
            leaderBut.setId("leaderButton");
            leaderBut.setRotate(Math.random() * (cardTilt - -cardTilt + 1) + -1 );
            leaderPane.getChildren().add(leaderBut);
            sceneLeaders.add(leaderBut);
        }

        for(int i=0; i<getSetupLeaderIcons().size();i++)
            selectedLeaders.add(false);


        ViewPersonalBoard.getController().cardSelector(selectedLeaders,sceneLeaders,1);

        List<Button> controlButtons=new ArrayList<>();


        Button playButton=new Button();
        playButton.setText("PLAY");
        playButton.setOnAction(p -> {
            for(Boolean b : selectedLeaders)
                if(b)
                {
                    if(!simplePlayerLeaders.getPlayerLeaders().get(selectedLeaders.indexOf(b)).getNetworkLeaderCard().isPlayable())
                    {
                        Text text=new Text("YOU CAN'T PLAY THAT!");
                        leaderPane.getChildren().add(text);
                        text.setLayoutX(width/2);
                        text.setLayoutY(len-60);
                        break;

                    }
                    InitialOrFinalPhaseEvent activate = new InitialOrFinalPhaseEvent(1,simplePlayerLeaders.getPlayerLeaders().get(selectedLeaders.indexOf(b)).getCardId());
                    getClient().getServerHandler().sendCommandMessage(new EventMessage(activate));
                    ((Pane)getClient().getStage().getScene().getRoot()).getChildren().remove(currentSize);

                }


        });

        Button discardButton=new Button();
        discardButton.setText("DISCARD");
        discardButton.setOnAction(p -> {

            for(Boolean b : selectedLeaders)
                if(b)
                {

                  ((Pane)getClient().getStage().getScene().getRoot()).getChildren().remove(currentSize);
                  InitialOrFinalPhaseEvent discard = new InitialOrFinalPhaseEvent(0,simplePlayerLeaders.getPlayerLeaders().get(selectedLeaders.indexOf(b)).getCardId());
                  getClient().getServerHandler().sendCommandMessage(new EventMessage(discard));
                }


        });
        Button skipButton=new Button();
        skipButton.setText("SKIP");
        skipButton.setOnAction(p -> {
            ((Pane)getClient().getStage().getScene().getRoot()).getChildren().remove(currentSize);
            getClient().getServerHandler().sendCommandMessage(new EventMessage(new InitialOrFinalPhaseEvent(2)));
        });

        controlButtons.add(playButton);
        controlButtons.add(discardButton);
        controlButtons.add(skipButton);

        for(int i=0;i<2;i++)
        {
            controlButtons.get(i).setLayoutX((i+1)*width/3);
            controlButtons.get(i).setLayoutY(len-buttonsDistanceFromBottom);
            leaderPane.getChildren().add(controlButtons.get(i));
        }

        skipButton.setLayoutX(width-50);
        skipButton.setLayoutY(len-50);
        leaderPane.getChildren().add(skipButton);
        selectedLeaders.addListener((ListChangeListener<Boolean>) c -> {
            c.next();
            if(c.getAddedSubList().get(0))
            {
                sceneLeaders.get(c.getFrom()).setLayoutY(sceneLeaders.get(c.getFrom()).getLayoutY()-30);

            }
            else
                sceneLeaders.get(c.getFrom()).setLayoutY(sceneLeaders.get(c.getFrom()).getLayoutY()+30);


        });

        leaderPane.setId("pane");

    }
}
