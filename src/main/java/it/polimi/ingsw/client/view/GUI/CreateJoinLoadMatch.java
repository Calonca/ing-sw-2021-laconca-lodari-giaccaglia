package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.GUI.GUIelem.MatchRow;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.CreateMatchRequest;
import it.polimi.ingsw.network.messages.clienttoserver.JoinMatchRequest;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

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

    boolean selected=false;
    private Slider playerCount;

    public double tilelen=264;
    public int tileheight=70;

    @Override
    public void run() {


        SubScene root=getRoot();
        root.translateYProperty().set(getClient().getStage().getScene().getHeight());
        Timeline timeline=new Timeline();
        KeyValue kv= new KeyValue(root.translateYProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf= new KeyFrame(Duration.seconds(0.5),kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
        root.setId("LOBBY");
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().remove(0);
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(root);

        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());

    }


    public SubScene getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/CreateJoinLoadMatch.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SubScene(root,1000,700);

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
        getClient().getCommonData().getMatchesData().ifPresent((data)->{
            data.forEach((key, value) -> templist.add(new MatchRow(key, Arrays.toString(value))));
        });
        return templist;
    }

    /**
     * First tile has a bonus slider and sends a different request to the server.
     * @return the Create slider. This is a service method
     */
    public AnchorPane creationTile(){
        AnchorPane createPane=new AnchorPane();
        createPane.setPrefHeight(tileheight);
        createPane.setPrefWidth(tilelen+150);

        Button but=new Button();
        but.setLayoutY(tileheight-20);
        but.setLayoutX(tilelen/2);
        but.setOnAction( p ->
        {
           // int playerCountValue= (int) playerCount.getValue();
            //getClient().getServerHandler().sendCommandMessage(new CreateMatchRequest(playerCountValue,getCommonData().getCurrentnick()));
            created=true;
            getClient().changeViewBuilder(new CreateMatchGUI());

        });

        but.setGraphic(new Label("CREATE"));
     /*   playerCount=new Slider(1,4,1);
        playerCount.setMaxWidth(tiledim);
        playerCount.setBlockIncrement(4);
        playerCount.setMinorTickCount(0);
        playerCount.setMajorTickUnit(1);
        playerCount.setShowTickLabels(true);
        playerCount.setShowTickMarks(true);
        playerCount.setSnapToTicks(true);
*/

        createPane.setStyle("-fx-background-color: #DEB887");
        createPane.getChildren().add(but);

        return createPane;
    }

    /**
     * Converts MatchRow to a suitable type to be added to a GridPane
     * @param matchRow is not null
     * @return the corresponding AnchorPane
     */
    public AnchorPane matchToTile(MatchRow matchRow)
    {

        AnchorPane joinPane=new AnchorPane();

        joinPane.setPrefHeight(tileheight);
        joinPane.setPrefWidth(tilelen+150);

        Label templabel=new Label(matchRow.getPeople());
        templabel.setMaxSize(tilelen,40);
        templabel.setLayoutY(10);
        templabel.setLayoutX(10);

        joinPane.getChildren().add(templabel);

        Button but=new Button();
        but.setLayoutY(tileheight-20);
        but.setLayoutX(tilelen/2);
        but.setOnAction( p ->
        {
           getClient().getServerHandler().sendCommandMessage(new JoinMatchRequest(matchRow.getKey(),getClient().getCommonData().getCurrentnick()));
           getClient().changeViewBuilder(new MatchToStart());
        });

        but.setGraphic(new Label("JOIN"));
        joinPane.setStyle("-fx-background-color: #DEB887");
        joinPane.getChildren().add(but);

        return joinPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
       // double rowdim=( Math.sqrt(dataToRow().size())+1);

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


        int i=0;
        while(!temp.isEmpty())
        {
            if(column==2)
            {
                column=0;
                row++;
            }
            grid.add(matchToTile(temp.get(i)),column,row);
            column++;
            temp.remove(0);
            i++;

        }


        ScrollPane scrollPane= new ScrollPane();
        scrollPane.setContent(grid);
        cjlPane.getChildren().remove(guiMatchesData);
        cjlPane.getChildren().add(scrollPane);

        Button test=new Button();
        test.setGraphic(new Label("PERSONAL BOARD"));
        test.setOnAction( t -> getClient().changeViewBuilder(new BoardView3D()));
        grid.add(test,2,2);
        guiMatchesData.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        guiMatchesData.getSelectionModel().setCellSelectionEnabled(true);
      //  System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(
        //       getClient().getCommonData().getMatchesData().orElse(null)));




    }
}
