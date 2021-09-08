package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import it.polimi.ingsw.client.view.abstractview.LobbyViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.CreateMatchRequest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This method adds a subscene which lets the player create a match, proposing a selection
 */
public class CreateMatchGUI extends CreateJoinLoadMatchViewBuilder implements GUIView {


    public AnchorPane createPane;
    public javafx.scene.control.Button a;
    public javafx.scene.control.Button b;
    public javafx.scene.control.Button c;
    public Button d;

    double width=500;
    double len=400;

    /**
     * This runnable will append the screen to the scene, removing it after sending the corresponding request
     */
    public void run()
    {
        SubScene root=getRoot();

        root.setId("CREATE");
        GUI.addLast(root);
    }

    public SubScene getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/CreateMatch.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SubScene(root,width,len);

    }

    /**
     * The argument button will be tied to generate a CreateMatchRequest with given inputs
     * @param b is the match creation button
     * @param playerCount is the corresponding player number
     */
    public void bindCreateButton(Button b, int playerCount) {

        b.setText(Integer.toString(playerCount));
        b.setOnAction(p ->
        {

            getClient().getServerHandler().sendCommandMessage(new CreateMatchRequest(playerCount,getCommonData().getThisPlayerNickname()));
            getClient().changeViewBuilder(LobbyViewBuilder.getBuilder(getClient().isCLI()));
            ((Pane)getClient().getStage().getScene().getRoot()).getChildren().remove(1);

        });


    }

    /**
     * Four buttons will be generated, according to given size. Each represent a number of players
     * @param url is ignored
     * @param resourceBundle is ignored
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        bindCreateButton(a,1);
        bindCreateButton(b,2);
        bindCreateButton(c,3);
        bindCreateButton(d,4);
        Effect dropShadow=new DropShadow(BlurType.GAUSSIAN, Color.rgb(0,0,0,0.5),10,0.7,5,5);
        createPane.setEffect(dropShadow);
        createPane.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.DOTTED,CornerRadii.EMPTY,BorderWidths.DEFAULT)));
        createPane.setStyle("-fx-background-color: linear-gradient(to right, #5771f2, #021782)");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
