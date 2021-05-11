package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.GUI.GUIelem.MatchRow;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import it.polimi.ingsw.server.controller.Match;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * The user will be asked to join a match of their choosing.
 */
public class JoinLoadMatch extends CreateJoinLoadMatchViewBuilder implements GUIView {

    @FXML
    private TableView<MatchRow> guiMatchesData;

    @Override
    public void run() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/JoinLoadMatchScreenScene.fxml"));
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
        ////TODO ADD OBSERVER FOR CONNECTION
        ObservableList<MatchRow> selected;
        selected=guiMatchesData.getSelectionModel().getSelectedItems();
        if(selected.size()!=0)
            System.out.println(selected.get(0).getNumber());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        TableColumn<MatchRow,Integer> players= new TableColumn<MatchRow,Integer>("MATCH ID");
        TableColumn<MatchRow,String> nicknames= new TableColumn<MatchRow,String>("NICKNAMES");

        final ObservableList<MatchRow> data= FXCollections.observableArrayList(new MatchRow(3,"mimmo"),new MatchRow(3,"toni"));

        nicknames.setCellValueFactory(new PropertyValueFactory<MatchRow,String>("people"));
        players.setCellValueFactory(new PropertyValueFactory<MatchRow,Integer>("number"));

        guiMatchesData.getColumns().add(nicknames);
        guiMatchesData.getColumns().add(players);

        guiMatchesData.setItems(data);


        guiMatchesData.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        guiMatchesData.getSelectionModel().setCellSelectionEnabled(true);
    }




    public void backButton(){
        Client.getInstance().changeViewBuilder(new CreateJoinLoadMatch());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
