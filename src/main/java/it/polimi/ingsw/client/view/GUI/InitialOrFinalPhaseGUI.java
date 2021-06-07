package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.abstractview.InitialOrFinalPhaseViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.InitialOrFinalPhaseEvent;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InitialOrFinalPhaseGUI extends InitialOrFinalPhaseViewBuilder implements GUIView {
    public AnchorPane leaderPane;
    public Button c;
    public Button leaderOne;
    public Button leaderTwo;
    public Button playButton;
    public Button discardButton;
    public Button skipButton;

    public InitialOrFinalPhaseGUI(boolean isInitial) {
        super(isInitial);
    }

    public InitialOrFinalPhaseGUI() {
        super();
    }
    @Override
    public void run()
    {
        Node toAdd=getRoot();

        toAdd.setId("LEADER");
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(toAdd);
        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());
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

        return new SubScene(root,500,400);

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<Button> sceneLeaders=new ArrayList<>();

        sceneLeaders.add(leaderOne);
        sceneLeaders.add(leaderTwo);

        javafx.collections.ObservableList<Boolean> selectedLeaders;
        selectedLeaders=javafx.collections.FXCollections.observableArrayList();

        selectedLeaders.add(false);
        selectedLeaders.add(false);

        ViewPersonalBoard.getController().cardSelector(selectedLeaders,sceneLeaders,1);

        skipButton.setOnAction(p -> {
            ((Pane)getClient().getStage().getScene().getRoot()).getChildren().remove(4);
            getClient().getServerHandler().sendCommandMessage(new EventMessage(new InitialOrFinalPhaseEvent(2)));
        });
        SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).orElseThrow();

        for(int i=0; i<sceneLeaders.size();i++)
        {
            Path path= simplePlayerLeaders.getPlayerLeaders().get(i).getCardPaths().getKey();
            ImageView temp = new ImageView(new Image(path.toString(), true));
//new Image(ViewPersonalBoard.class.getResource(leaderCardAssets.get(i).getCardPaths().getKey().toString()).toString()

            temp.setFitHeight(100);
            temp.setFitWidth(70);
            sceneLeaders.get(i).setGraphic(temp);

        }
        playButton.setOnAction(p -> {
            ((Pane)getClient().getStage().getScene().getRoot()).getChildren().remove(4);
            for(Boolean b : selectedLeaders)
                if(b)
                {
                   InitialOrFinalPhaseEvent activate = new InitialOrFinalPhaseEvent(1,simplePlayerLeaders.getPlayerLeaders().get(selectedLeaders.indexOf(b)).getCardId());
                   getClient().getServerHandler().sendCommandMessage(new EventMessage(activate));
                }


        });

        discardButton.setOnAction(p -> {
            ((Pane)getClient().getStage().getScene().getRoot()).getChildren().remove(4);
            for(Boolean b : selectedLeaders)
                if(b)
                {
                  InitialOrFinalPhaseEvent discard = new InitialOrFinalPhaseEvent(0,simplePlayerLeaders.getPlayerLeaders().get(selectedLeaders.indexOf(b)).getCardId());
                  getClient().getServerHandler().sendCommandMessage(new EventMessage(discard));
                }


        });

    }
}
