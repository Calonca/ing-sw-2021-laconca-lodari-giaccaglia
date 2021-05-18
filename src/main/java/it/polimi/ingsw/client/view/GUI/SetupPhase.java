package it.polimi.ingsw.client.view.GUI;


import it.polimi.ingsw.client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The user will be asked to insert a nickname and the number of players
 */
public class SetupPhase extends  it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder implements GUIView {

    public Button b1;
    public Button b2;
    public Button b3;
    public Button b4;

    Spinner<Integer> goldSpin;
    Spinner<Integer> slaveSpin;
    Spinner<Integer> stoneSpin;
    Spinner<Integer> shieldSpin;

    public AnchorPane cjlAnchor;

    @FXML
    private Button connectionButton;
    @FXML
    private TextField addressText;
    @FXML
    private TextField portText;

    @Override
    public void run() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/SetupMenu.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);

        getClient().getStage().setScene(scene);
        getClient().getStage().show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        //gggg.setText("pisellogfggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg");
        goldSpin=new Spinner<Integer>(0,3,0);
        goldSpin.setPadding(new Insets(10,10,10,10));
        goldSpin.setBackground(Background.EMPTY);
        goldSpin.styleProperty().set(" -fx-background-color: #B8860B");
        goldSpin.setLayoutY(100);
        goldSpin.setLayoutX(300);
        cjlAnchor.getChildren().add(goldSpin);

        slaveSpin=new Spinner<Integer>(0,3,0);
        slaveSpin.setPadding(new Insets(10,10,10,10));
        slaveSpin.setBackground(Background.EMPTY);
        slaveSpin.styleProperty().set(" -fx-background-color: #9400D3");
        slaveSpin.setLayoutY(100);
        slaveSpin.setLayoutX(100);
        cjlAnchor.getChildren().add(slaveSpin);

        shieldSpin=new Spinner<Integer>(0,3,0);
        shieldSpin.setPadding(new Insets(10,10,10,10));
        shieldSpin.setBackground(Background.EMPTY);
        shieldSpin.styleProperty().set(" -fx-background-color: #0099FF");
        shieldSpin.setLayoutY(50);
        shieldSpin.setLayoutX(300);
        cjlAnchor.getChildren().add(shieldSpin);

        stoneSpin=new Spinner<Integer>(0,3,0);
        stoneSpin.setPadding(new Insets(10,10,10,10));
        stoneSpin.setBackground(Background.EMPTY);
        stoneSpin.styleProperty().set(" -fx-background-color: #DEB887");
        stoneSpin.setLayoutY(50);
        stoneSpin.setLayoutX(100);
        cjlAnchor.getChildren().add(stoneSpin);

        ImageView temp;
        ToggleGroup ciccio=new ToggleGroup();
        temp=new ImageView(new Image("assets/leaders/FRONT/Masters of Renaissance_Cards_FRONT_0.png", true));
        temp.setFitHeight(100);
        temp.setFitWidth(100);
        b1.setGraphic(temp);
        temp=new ImageView(new Image("assets/leaders/FRONT/Masters of Renaissance_Cards_FRONT_0.png", true));
        temp.setFitHeight(100);
        temp.setFitWidth(100);
        b2.setGraphic(temp);
        temp=new ImageView(new Image("assets/leaders/FRONT/Masters of Renaissance_Cards_FRONT_0.png", true));
        temp.setFitHeight(100);
        temp.setFitWidth(100);
        b3.setGraphic(temp);
        temp=new ImageView(new Image("assets/leaders/FRONT/Masters of Renaissance_Cards_FRONT_0.png", true));
        temp.setFitHeight(100);
        temp.setFitWidth(100);
        b4.setGraphic(temp);


        Client.getInstance().getStage().show();

    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}