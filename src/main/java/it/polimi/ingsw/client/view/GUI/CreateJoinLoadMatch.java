package it.polimi.ingsw.client.view.GUI;

import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.GUI.GUIelem.MatchRow;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
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
    public double tiledim;
    boolean created=false;
    TableColumn<MatchRow,String> nicknames;
    TableColumn<MatchRow,UUID> UUIDs;

    private Slider playerCount;
    public int tileheight=70;

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





    public void clickedColumn(MouseEvent event)
    {
        //TODO SELECT WHOLE ROW (NOT NECESSARY IT WOULD WORK ANYWAY BUT ITS UGLY)
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(CommonData.matchesDataString))
            Platform.runLater(()->
            {
                /*
                if(!created)
                {
                //todo clean this up
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
                }*/
            });

    }

    public List<MatchRow> dataToRow() {
        List<MatchRow> templist=new ArrayList<>();
        Client.getInstance().getCommonData().getMatchesData().ifPresent((data)->{
            data.forEach((key, value) -> templist.add(new MatchRow(key, Arrays.toString(value))));
        });
        return templist;
    }

    /**
     * First tile has a bonus slider and sends a different request to the server.
     * @return the Create slider. This is a service method
     */
    public AnchorPane creationTile(){
        AnchorPane temppane=new AnchorPane();
        temppane.setPrefHeight(tileheight);
        temppane.setPrefWidth(tiledim+150);

        Button but=new Button();
        but.setLayoutY(tileheight-20);
        but.setLayoutX(30);
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
        playerCount.setMaxWidth(tiledim);
        playerCount.setBlockIncrement(4);
        playerCount.setMinorTickCount(0);
        playerCount.setMajorTickUnit(1);
        playerCount.setShowTickLabels(true);
        playerCount.setShowTickMarks(true);
        playerCount.setSnapToTicks(true);

        temppane.getChildren().add(playerCount);
        playerCount.setLayoutX(0);
        playerCount.setLayoutY(0);
        temppane.setStyle("-fx-background-color: #DEB887");
        temppane.getChildren().add(but);

        return temppane;
    }

    /**
     * Converts MatchRow to a suitable type to be added to a GridPane
     * @param matchRow is not null
     * @return the corresponding AnchorPane
     */
    public AnchorPane matchToTile(MatchRow matchRow)
    {

        AnchorPane temppane=new AnchorPane();

        temppane.setPrefHeight(tileheight);
        temppane.setPrefWidth(tiledim+150);

        Label templabel=new Label(matchRow.getPeople());
        templabel.setMaxSize(tiledim,40);
        templabel.setLayoutY(10);
        templabel.setLayoutX(10);

        temppane.getChildren().add(templabel);

        Button but=new Button();
        but.setLayoutY(tileheight-20);
        but.setLayoutX(40);
        but.setOnAction( p ->
        {
            System.out.println(matchRow.getKey()+Client.getInstance().getCommonData().getCurrentnick());
            Client.getInstance().getServerHandler().sendCommandMessage(new JoinMatchRequest(matchRow.getKey(),Client.getInstance().getCommonData().getCurrentnick()));
            Client.getInstance().changeViewBuilder(new CreateMatch());
        });

        but.setGraphic(new Label("JOIN"));
        temppane.setStyle("-fx-background-color: #DEB887");
        temppane.getChildren().add(but);

        return temppane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
       // double rowdim=( Math.sqrt(dataToRow().size())+1);
        tiledim= 264;

        nicknames= new TableColumn<MatchRow,String>("NICKNAMES");
        UUIDs= new TableColumn<MatchRow,UUID>("UUID");

        final ObservableList<MatchRow> data= FXCollections.observableArrayList(dataToRow());

        nicknames.setCellValueFactory(new PropertyValueFactory<MatchRow,String>("people"));
        UUIDs.setCellValueFactory(new PropertyValueFactory<MatchRow,UUID>("key"));

        guiMatchesData.getColumns().add(nicknames);
        guiMatchesData.getColumns().add(UUIDs);
        //tableview init code
        guiMatchesData.setItems(data);



        GridPane grid=new GridPane();
        grid.setPadding(new Insets(50,50,50,50));
        grid.setHgap(50);
        grid.setVgap(35);

        grid.add(creationTile(),0,0);


        List<MatchRow> temp=dataToRow();
        int row=0;
        int column=1;
        temp.add(new MatchRow(UUID.randomUUID(),"lorenzo"));
        temp.add(new MatchRow(UUID.randomUUID(),"lorenzo"));
        temp.add(new MatchRow(UUID.randomUUID(),"lorenzo,gigi,cantalupo,porpora"));
        temp.add(new MatchRow(UUID.randomUUID(),"lorenzo"));
        temp.add(new MatchRow(UUID.randomUUID(),"lorenzo"));
        temp.add(new MatchRow(UUID.randomUUID(),"lorenzo"));
        temp.add(new MatchRow(UUID.randomUUID(),"lorenzo le magnifique"));
        temp.add(new MatchRow(UUID.randomUUID(),"lorenzo"));
        temp.add(new MatchRow(UUID.randomUUID(),"lorenzo"));
        temp.add(new MatchRow(UUID.randomUUID(),"lorenzo"));
        temp.add(new MatchRow(UUID.randomUUID(),"lorenzo il magnifico"));
        temp.add(new MatchRow(UUID.randomUUID(),"lorenzo"));



        while(!temp.isEmpty())
        {
            if(column==2)
            {
                column=0;
                row++;
            }
            grid.add(matchToTile(temp.get(0)),column,row);
            column++;
            temp.remove(0);

        }


        ScrollPane scrollPane= new ScrollPane();
        scrollPane.setContent(grid);
        cjlPane.getChildren().remove(guiMatchesData);
        cjlPane.getChildren().add(scrollPane);

        guiMatchesData.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        guiMatchesData.getSelectionModel().setCellSelectionEnabled(true);
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(
                Client.getInstance().getCommonData().getMatchesData().orElse(null)));




    }
}
