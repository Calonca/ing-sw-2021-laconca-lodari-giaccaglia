package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.abstractview.ConnectToServerViewBuilder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConnectToServer extends ConnectToServerViewBuilder implements GUIView {
    @FXML
    private StackPane connectionPane;
    @FXML
    private Button connectionButton;
    @FXML
    private TextField addressText;
    @FXML
    private TextField portText;
    @FXML
    private TextField nickText;
    private Text errortext;

    @Override
    public void run() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/ConnectToServerScene.fxml"));
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

    //Add buttons here that call client.changeViewBuilder(new *****, this);

    public void handleButton()
    {

        if(!nickText.getCharacters().toString().isEmpty())
            if(isIPAddr(addressText.getCharacters().toString()))
                if(Integer.parseInt(portText.getCharacters().toString())<65536)
                {
                    Client.getInstance().getCommonData().setCurrentnick(nickText.getCharacters().toString());
                    Client.getInstance().setServerConnection(addressText.getCharacters().toString(),Integer.parseInt(portText.getCharacters().toString()));
                    Client.getInstance().run();
                    return;
                }


        errortext.setOpacity(200);


        Client.getInstance().getStage().show();

    }

    public void handleButton2()
    {
        Client.getInstance().getCommonData().setCurrentnick("DUMMY");
        Client.getInstance().setServerConnection("127.0.0.1",7890);
        Client.getInstance().run();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        errortext = new Text("INSERISCI I DATI CORRETTI!");
        errortext.setFont(Font.font(null, FontWeight.BOLD, 10));
        StackPane.setAlignment(errortext, Pos.TOP_CENTER);
        StackPane.setMargin(errortext, new Insets(40, 10, 10, 10));

        errortext.setOpacity(0);

        connectionPane.getChildren().add(errortext);

        StackPane.setAlignment(errortext, Pos.TOP_CENTER);
        StackPane.setMargin(errortext, new Insets(40, 10, 10, 10));
        //connectionButton.setOnAction(e -> Client.getInstance().changeViewBuilder(new CreateJoinLoadMatch(), null));
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(CommonData.matchesDataString))
            Platform.runLater(()->
                Client.getInstance().changeViewBuilder(new CreateJoinLoadMatch())
            );

    }
}
