package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.GUI.util.CardSelector;
import it.polimi.ingsw.client.view.abstractview.InitialOrFinalPhaseViewBuilder;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.InitialOrFinalPhaseEvent;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class InitialOrFinalPhaseGUI extends InitialOrFinalPhaseViewBuilder implements GUIView {
    public AnchorPane leaderPane;
    public Button c;


    public double width=800;
    public double len=600;
    double paddingBetweenCards=50;
    double cardsY=40;
    double cardTilt=1;
    double buttonsDistanceFromBottom=50;
    double cardLen=len*(3.0/4);
    List<UUID> leadersUUIDs=new ArrayList<>();
    double cardsStartingX=70;
    List<ImageView> sceneLeadersImageView =new ArrayList<>();


    /**
     * @param isInitial is true if the state it's INITIAL_PHASE
     */
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
        Node root=getRoot();
        root.setId("LEADER");
        GUI.addLast(root);
        Playground.getThisPlayerBoard().setMode(BoardView3D.Mode.BACKGROUND);
        Playground.refreshCardShop();
        System.out.println(GUI.getRealPane().getChildren());
    }


    /**
     * @return a Subscene, this gets called every initial and final phase, if they occur
     */
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

    /**
     * Helper method to centralize the image acquisition
     * @return a list of LeaderAsset ImageViews
     */
    public List<ImageView> getInitialLeaderIcons() {
        SimplePlayerLeaders simplePlayerLeaders = getSimpleModel().getPlayerCache(getClient().getCommonData().getThisPlayerIndex()).getElem(SimplePlayerLeaders.class).orElseThrow();
        List<LeaderCardAsset> leaderCardAssets=simplePlayerLeaders.getPlayerLeaders();
        List<ImageView> resultList=new ArrayList<>();

        for (LeaderCardAsset leaderCardAsset : leaderCardAssets) {

                leadersUUIDs.add(leaderCardAsset.getCardId());
                ImageView temp = CardSelector.imageViewFromAsset(leaderCardAsset.getCardPaths().getKey());
                temp.setFitHeight(cardLen);
                resultList.add(temp);

        }
        return resultList;

    }

    /**
     * This method adds the initial resources to choose from, as well as the initial leaders
     * @param url is ignored
     * @param resourceBundle is ignored
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        javafx.collections.ObservableList<Boolean> selectedLeaders;
        selectedLeaders=javafx.collections.FXCollections.observableArrayList();


        SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).orElseThrow();
        Effect dropShadow=new DropShadow(BlurType.GAUSSIAN, Color.rgb(0,0,0,0.5),10,0.7,5,5);

        List<Boolean> leaderPlayability=new ArrayList<>();
        ImageView leaderBut;
        for(int i=0;i<getInitialLeaderIcons().size();i++)
        {
            leaderBut= getInitialLeaderIcons().get(i);
            leaderBut.setLayoutY(cardsY);
            leaderBut.setLayoutX(cardsStartingX+i*(cardLen*(462.0/709)+paddingBetweenCards));
            leaderBut.setEffect(dropShadow);
            leaderBut.setRotate(Math.random() * (cardTilt - -cardTilt + 1) + -1 );
            if(!simplePlayerLeaders.getPlayerLeaders().get(i).getNetworkLeaderCard().isLeaderActive())
                leaderPane.getChildren().add(leaderBut);
            sceneLeadersImageView.add(leaderBut);
        }

        for(int i=0; i<getInitialLeaderIcons().size();i++)
            selectedLeaders.add(false);



        CardSelector cardSelector=new CardSelector();
        cardSelector.cardSelectorFromImage(selectedLeaders, sceneLeadersImageView,1);

        List<Button> controlButtons=new ArrayList<>();


        Button playButton=new Button();
        playButton.setText("PLAY");
        playButton.setOnAction(p -> {
            for(Boolean b : selectedLeaders)
                if(b)
                {

                    InitialOrFinalPhaseEvent activate = new InitialOrFinalPhaseEvent(1,simplePlayerLeaders.getPlayerLeaders().get(selectedLeaders.indexOf(b)).getCardId());
                    getClient().getServerHandler().sendCommandMessage(new EventMessage(activate));
                    GUI.removeLast();

                }


        });

        Button discardButton=new Button();
        discardButton.setText("DISCARD");
        discardButton.setOnAction(p -> {

            for(Boolean b : selectedLeaders)
                if(b)
                {

                    InitialOrFinalPhaseEvent discard = new InitialOrFinalPhaseEvent(0,simplePlayerLeaders.getPlayerLeaders().get(selectedLeaders.indexOf(b)).getCardId());
                    getClient().getServerHandler().sendCommandMessage(new EventMessage(discard));
                    GUI.removeLast();
                }


        });
        Button skipButton=new Button();
        skipButton.setText("SKIP");
        skipButton.setOnAction(p -> {
            getClient().getServerHandler().sendCommandMessage(new EventMessage(new InitialOrFinalPhaseEvent(2)));
            GUI.removeLast();

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
                sceneLeadersImageView.get(c.getFrom()).setLayoutY(sceneLeadersImageView.get(c.getFrom()).getLayoutY()-30);
                if(simplePlayerLeaders.getPlayerLeaders().get(c.getFrom()).getNetworkLeaderCard().isPlayable())
                    playButton.setDisable(false);
                discardButton.setDisable(false);
            }
            else
            {
                sceneLeadersImageView.get(c.getFrom()).setLayoutY(sceneLeadersImageView.get(c.getFrom()).getLayoutY()+30);
                playButton.setDisable(true);
                discardButton.setDisable(true);
            }

        });

        playButton.setDisable(true);
        discardButton.setDisable(true);

        leaderPane.setId("initialPane");

    }
}
