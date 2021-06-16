package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.GUI.GUIelem.MatchRow;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.JoinMatchRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.*;
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


    double gridInsets=50;
    double VGap=35;
    double HGap=50;
    double width=GUI.GUIwidth;
    double len= GUI.GUIlen;
    public double tileWidth=width/3;
    public double tileheight=len/10;


    /**
     * This runnable will make the Match Lobby slide in from below, and then remove the first connection screen
     */
    @Override
    public void run() {


        SubScene root=getRoot();
        root.setId("LOBBY");

        GUI.getRealPane().getChildren().remove(0);

        GUI.addLast(root);
        getClient().getStage().getScene().getStylesheets().add("assets/application.css");

        getClient().getStage().show();


        System.out.println((GUI.getRealPane().getChildren()));

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

        return new SubScene(root,GUI.GUIwidth,GUI.GUIlen);

    }


    /**
     * Screen refresh when new match is created
     * @param evt is not null
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(CommonData.matchesDataString))
            Platform.runLater(()->
            {

                if(!created)
                {
                    run();
                }
            });

    }

    /**
     * Adapter for matches data
     * @return list of easily displayable data
     */
    public List<MatchRow> dataToRow() {
        List<MatchRow> templist=new ArrayList<>();
        getClient().getCommonData().getMatchesData().ifPresent((data)-> data.forEach((key, value) -> templist.add(new MatchRow(key.getKey(), Arrays.toString(value.getKey())))));
        return templist;
    }

    /**
     * First tile has a bonus slider and sends a different request to the server.
     * @return the Create slider. This is a service method
     */
    public AnchorPane creationTile(){
        AnchorPane createPane=new AnchorPane();
        createPane.setPrefHeight(tileheight);
        createPane.setPrefWidth(tileWidth);

        Button creationBut=new Button();
        creationBut.setLayoutY(tileheight/2.0);
        creationBut.setLayoutX(tileWidth/2);
        creationBut.setOnAction( p ->
        {
            created=true;
            getClient().changeViewBuilder(new CreateMatchGUI());

        });

        creationBut.setText("CREATE");
        createPane.setId("tile");
        createPane.getChildren().add(creationBut);
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
        joinPane.setPrefWidth(tileWidth+250);

        Label templabel=new Label(matchRow.getPeople());
        templabel.setMaxSize(tileWidth,40);
        templabel.setLayoutY(10);
        templabel.setLayoutX(10);

        joinPane.getChildren().add(templabel);

        Button joinMatchButton=new Button();
        joinMatchButton.setLayoutY(tileheight/2);
        joinMatchButton.setLayoutX(tileWidth/2);
        joinMatchButton.setOnAction( p ->
        {
           getClient().getServerHandler().sendCommandMessage(new JoinMatchRequest(matchRow.getKey(),getClient().getCommonData().getCurrentNick()));
           getClient().changeViewBuilder(new MatchToStart());
        });

        joinMatchButton.setText("JOIN");
        joinPane.setId("tile");
        joinPane.getChildren().add(joinMatchButton);

        return joinPane;
    }

    public AnchorPane emptyTile()
    {

        AnchorPane joinPane=new AnchorPane();

        joinPane.setPrefHeight(tileheight);
        joinPane.setPrefWidth(tileWidth+200);


        joinPane.setId("tile");

        Button joinMatchButton=new Button();
        joinMatchButton.setLayoutY(tileheight/2);
        joinMatchButton.setLayoutX(tileWidth/2);

        joinMatchButton.setGraphic(new Label("EMPTY"));
        joinMatchButton.setDisable(true);
        joinPane.getChildren().add(joinMatchButton);

        return joinPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

        GridPane grid=new GridPane();
        grid.setPadding(new Insets(gridInsets,gridInsets,gridInsets,gridInsets));
        grid.setHgap(HGap);
        grid.setVgap(VGap);
        grid.setMinHeight(len);
        grid.setMinWidth(width);

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
            grid.add(matchToTile(temp.get(0)),column,row);
            column++;
            temp.remove(0);
            i++;

        }

        while(i<10)
        {
            if(column==2)
            {
                column=0;
                row++;
            }
            grid.add(emptyTile(),column,row);
            column++;
            i++;

        }


        ScrollPane scrollPane= new ScrollPane();
        scrollPane.setContent(grid);
        grid.setStyle("    -fx-background-color: linear-gradient(to right, #ADD8E6, #5771f2);");
        cjlPane.getChildren().remove(guiMatchesData);
        cjlPane.getChildren().add(scrollPane);
        grid.setId("background");

        Button test=new Button();
        test.setGraphic(new Label("PERSONAL BOARD"));
        test.setOnAction( t -> getClient().changeViewBuilder(new BoardView3D()));
     //w   grid.add(test,2,2);


    }
}
