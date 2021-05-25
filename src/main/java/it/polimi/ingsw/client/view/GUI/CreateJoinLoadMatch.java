package it.polimi.ingsw.client.view.GUI;

import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.GUI.GUIelem.MatchRow;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.CreateMatchRequest;
import it.polimi.ingsw.network.messages.clienttoserver.JoinMatchRequest;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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
    public TableView<MatchRow> guiMatchesData;
    public StackPane cjlPane;

    boolean created=false;
    TableColumn<MatchRow,String> nicknames;
    TableColumn<MatchRow,UUID> UUIDs;
    @FXML
    private Button joinLoadButton;
    @FXML
    private Button createButton;
    @FXML
    private Slider playerCount;


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
            Client.getInstance().getServerHandler().sendCommandMessage(new JoinMatchRequest(selected.get(0).getKey(),Client.getInstance().getCommonData().getCurrentnick()));
            Client.getInstance().changeViewBuilder(new CreateMatch());

        }

    }

    public void handleCreate(){
        int a= (int) playerCount.getValue();
        System.out.println(a+Client.getInstance().getCommonData().getCurrentnick());
        Client.getInstance().getServerHandler().sendCommandMessage(new CreateMatchRequest(a,Client.getInstance().getCommonData().getCurrentnick()));
        created=true;
        Client.getInstance().changeViewBuilder(new CreateMatch());
    }

    public void clickedColumn(MouseEvent event)
    {
        //TODO SELECT WHOLE ROW (NOT NECESSARY IT WOULD WORK ANYWAY BUT ITS UGLY)
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(CommonData.matchesDataString))
            Platform.runLater(()->
            {
                if(!created)
                {
                //todo fix this may be ugly
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
            });

    }

    public List<MatchRow> dataToRow() {
        List<MatchRow> templist=new ArrayList<>();
        Client.getInstance().getCommonData().getMatchesData().ifPresent((data)->{
            data.forEach((key, value) -> templist.add(new MatchRow(key, Arrays.toString(value))));
        });
        return templist;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        nicknames= new TableColumn<MatchRow,String>("NICKNAMES");
        UUIDs= new TableColumn<MatchRow,UUID>("UUID");

        final ObservableList<MatchRow> data= FXCollections.observableArrayList(dataToRow());

        nicknames.setCellValueFactory(new PropertyValueFactory<MatchRow,String>("people"));
        UUIDs.setCellValueFactory(new PropertyValueFactory<MatchRow,UUID>("key"));

        guiMatchesData.getColumns().add(nicknames);
        guiMatchesData.getColumns().add(UUIDs);

        guiMatchesData.setItems(data);

        AnchorPane temppane=new AnchorPane();
        temppane.setPrefHeight(200);
        temppane.setPrefWidth(200);
        Label templabel=new Label("players");
        templabel.setLayoutY(10);
        templabel.setLayoutX(50);
        temppane.getChildren().add(templabel);
        Button but=new Button();
        but.setLayoutY(100);
        but.setLayoutX(100);
        but.setOnAction( p ->
        {
            int a= (int) playerCount.getValue();
            System.out.println(a+Client.getInstance().getCommonData().getCurrentnick());
            Client.getInstance().getServerHandler().sendCommandMessage(new CreateMatchRequest(a,Client.getInstance().getCommonData().getCurrentnick()));
            created=true;
            Client.getInstance().changeViewBuilder(new CreateMatch());
        });

        but.setGraphic(new Label("CREATE"));
        playerCount=new Slider(1,4,1);

        playerCount.setBlockIncrement(4);
        playerCount.setMinorTickCount(0);
        playerCount.setMajorTickUnit(1);
        playerCount.setShowTickLabels(true);
        playerCount.setShowTickMarks(true);
        playerCount.setSnapToTicks(true);

        temppane.getChildren().add(playerCount);
        playerCount.setLayoutX(200);
        playerCount.setLayoutY(200);
        temppane.setStyle("-fx-background-color: #DEB887");
        temppane.getChildren().add(but);


        GridPane ciccio=new GridPane();
        ciccio.setPadding(new Insets(10,10,10,10));
        ciccio.add(temppane,0,0);

        cjlPane.getChildren().remove(guiMatchesData);
        cjlPane.getChildren().add(ciccio);
        guiMatchesData.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        guiMatchesData.getSelectionModel().setCellSelectionEnabled(true);
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(
                Client.getInstance().getCommonData().getMatchesData().orElse(null)));



    }
}
