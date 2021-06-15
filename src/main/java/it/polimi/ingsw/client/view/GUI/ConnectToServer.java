package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.abstractview.ConnectToServerViewBuilder;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * During this phase, the player will be asked to connect to a game server. It's composed by three text forms
 * and a confirmation button
 */
public class ConnectToServer extends ConnectToServerViewBuilder implements GUIView {

    public Text error;
    public AnchorPane connectionPane;



    private TextField addressText;
    private TextField portText;
    private TextField nickText;
    double width=GUI.GUIwidth;
    double len= GUI.GUIlen;


    @Override
    public void run() {



        SubScene root=getRoot();
        root.setId("CONNECT");

        root.translateYProperty().set(GUI.getRealPane().getHeight());
        Timeline timeline=new Timeline();
        KeyValue kv= new KeyValue(root.translateYProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf= new KeyFrame(Duration.seconds(0.5),kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        GUI.getRealPane().getChildren().remove(0);

        GUI.addLast(root);

        getClient().getStage().getScene().getStylesheets().add("assets/application.css");
        getClient().getStage().show();
        System.out.println((GUI.getRealPane().getChildren()));



    }


    /**
     * The first scene needs a parent, starting from the second this method will only return SubScene
     * @return the first game scene
     */
    public SubScene getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/ConnectToServerScene.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SubScene(root,width,len);

    }
    //Add buttons here that call client.changeViewBuilder(new *****, this);

    public Button validationButton()
    {
        Button button=new Button();
        button.setLayoutX(width/2);
        button.setLayoutY(len/2);
        button.setText("CONNECT");
        button.setOnAction(p->
        {
            if(!nickText.getCharacters().toString().isEmpty())
                if(isIPAddr(addressText.getCharacters().toString()))
                    if(Integer.parseInt(portText.getCharacters().toString())<65536)
                    {
                        getClient().getCommonData().setCurrentNick(nickText.getCharacters().toString());
                        getClient().setServerConnection(addressText.getCharacters().toString(),Integer.parseInt(portText.getCharacters().toString()));
                        getClient().run();
                        return;
                    }
            error.setOpacity(200);

        });
        return button;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {





        nickText=new TextField();
        nickText.setLayoutX(width/2-25);
        nickText.setLayoutY(len/2+len/8-20);
        nickText.setPromptText("nickname");


        portText=new TextField();
        portText.setLayoutX(width/2-25);
        portText.setLayoutY(len/2+len/8+10);
        portText.setPromptText("port");
        portText.setText(Integer.toString(getClient().getPort()));

        addressText=new TextField();
        String ip = getClient().getIp();
        if (ip!=null)
            addressText.setText(ip);
        addressText.setPromptText("ip address");
        addressText.setLayoutX(width/2-25);
        addressText.setLayoutY(len/2+len/8+40);


        connectionPane.getChildren().add(nickText);
        connectionPane.getChildren().add(addressText);
        connectionPane.getChildren().add(portText);
        connectionPane.getChildren().add(validationButton());
        connectionPane.setId("background");




    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(CommonData.matchesDataString))
            Platform.runLater(()->
                    getClient().changeViewBuilder(new CreateJoinLoadMatch()));

    }
}
