package it.polimi.ingsw.client.view.GUI;

import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.GUI.GUIelem.MatchRow;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import it.polimi.ingsw.server.messages.clienttoserver.CreateMatchRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * The user will be asked if they want to join a match of their choosing or create one.
 */
public class CreateJoinLoadMatch extends CreateJoinLoadMatchViewBuilder implements GUIView {

    @FXML
    private TableView<MatchRow> guiMatchesData;

    @FXML
    private Button joinLoadButton;
    @FXML
    private Button createButton;
    @FXML
    private Slider playerCount;
    @FXML
    private StackPane cjlPane;

    @Override
    public void run() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/CreateJoinLoadMatch.fxml"));
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



    public void handleJoin(){
        ObservableList<MatchRow> selected;
        selected=guiMatchesData.getSelectionModel().getSelectedItems();
        if(selected.size()!=0)
            System.out.println(selected.get(0).getKey());

    }

    public void handleCreate(){
        Client.getInstance().getServerHandler().sendCommandMessage(new CreateMatchRequest((int) playerCount.getValue(),Client.getInstance().getCommonData().getCurrentnick()));
        Client.getInstance().changeViewBuilder(new CreateMatch());
    }

    public void clickedColumn(MouseEvent event)
    {

        //TODO SELECT WHOLE ROW (NOT NECESSARY IT WOULD WORK ANYWAY BUT ITS UGLY)
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        TableColumn<MatchRow,Integer> players= new TableColumn<MatchRow,Integer>("MATCH ID");
        TableColumn<MatchRow,String> nicknames= new TableColumn<MatchRow,String>("NICKNAMES");
        TableColumn<MatchRow,UUID> UUIDs= new TableColumn<MatchRow,UUID>("UUID");


        final ObservableList<MatchRow> data= FXCollections.observableArrayList(new MatchRow(3,"mimmo",UUID.randomUUID()),new MatchRow(3,"toni",UUID.randomUUID()));

        nicknames.setCellValueFactory(new PropertyValueFactory<MatchRow,String>("people"));
        players.setCellValueFactory(new PropertyValueFactory<MatchRow,Integer>("number"));
        UUIDs.setCellValueFactory(new PropertyValueFactory<MatchRow,UUID>("key"));

        guiMatchesData.getColumns().add(nicknames);
        guiMatchesData.getColumns().add(players);

        guiMatchesData.setItems(data);


        guiMatchesData.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        guiMatchesData.getSelectionModel().setCellSelectionEnabled(true);
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(
                Client.getInstance().getCommonData().getMatchesData().orElse(null)));

    }
}
