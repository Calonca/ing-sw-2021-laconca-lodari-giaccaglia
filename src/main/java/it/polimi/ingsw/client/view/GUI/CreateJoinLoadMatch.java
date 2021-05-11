package it.polimi.ingsw.client.view.GUI;

import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.GUI.GUIelem.MatchRow;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import it.polimi.ingsw.network.assets.devcards.DevelopmentCardColor;
import it.polimi.ingsw.network.messages.clienttoserver.CreateMatchRequest;
import it.polimi.ingsw.network.messages.clienttoserver.JoinMatchRequest;
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
import java.util.*;

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
        {
            System.out.println(selected.get(0).getKey()+Client.getInstance().getCommonData().getCurrentnick());
            //TODO CANNOT SERIALIZE fix
            Client.getInstance().getServerHandler().sendCommandMessage(new JoinMatchRequest(selected.get(0).getKey(),Client.getInstance().getCommonData().getCurrentnick()));
            Client.getInstance().changeViewBuilder(new CreateMatch());

        }

    }

    public void handleCreate(){
        int a= (int) playerCount.getValue();
        //TODO CANNOT SERIALIZE fix
        System.out.println(a+Client.getInstance().getCommonData().getCurrentnick());
        Client.getInstance().getServerHandler().sendCommandMessage(new CreateMatchRequest(a,Client.getInstance().getCommonData().getCurrentnick()));
        Client.getInstance().changeViewBuilder(new CreateMatch());
    }

    public void clickedColumn(MouseEvent event)
    {

        //TODO SELECT WHOLE ROW (NOT NECESSARY IT WOULD WORK ANYWAY BUT ITS UGLY)
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public List<MatchRow> dataToRow() {
        List<MatchRow> templist=new ArrayList<MatchRow>();
        int k=0;
        if(Client.getInstance().getCommonData().getMatchesData().isPresent())
            for(UUID key : Client.getInstance().getCommonData().getMatchesData().get().keySet())
            {

                templist.add(new MatchRow(k,Client.getInstance().getCommonData().getMatchesData().get().get(key)[0],key));
            }
        templist.add(new MatchRow(1,"test",UUID.randomUUID()));

        return templist;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        TableColumn<MatchRow,Integer> players= new TableColumn<MatchRow,Integer>("MATCH ID");
        TableColumn<MatchRow,String> nicknames= new TableColumn<MatchRow,String>("NICKNAMES");
        TableColumn<MatchRow,UUID> UUIDs= new TableColumn<MatchRow,UUID>("UUID");


        final ObservableList<MatchRow> data= FXCollections.observableArrayList(dataToRow());

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
