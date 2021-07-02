package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import it.polimi.ingsw.client.view.abstractview.LobbyViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.JoinMatchRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * The user will be asked if they want to join a match of their choosing or create one.
 */
public class CreateJoinLoadMatchGUI extends CreateJoinLoadMatchViewBuilder implements GUIView {

    @FXML
    public TableView<MatchRow> guiMatchesData;
    public StackPane cjlPane;
    boolean created=false;


    double gridInsets=50;
    double VGap=35;
    double HGap=50;
    double width=GUI.GUIwidth;
    double len= GUI.GUIlen;
    int maxcolumn=2;
    public double tileWidth=(-100+width)/(maxcolumn+1);
    public double tileheight=len/7;


    /**
     * Private class used as an adaptor to convert matchesData
     */
    public static class MatchRow {
        Map.Entry<UUID, Pair<String[], String[]>> uuidPair;
        boolean isSaved;
        public MatchRow(Map.Entry<UUID, Pair<String[], String[]>> uuidPair,boolean isSaved) {
            this.uuidPair = uuidPair;
            this.isSaved=isSaved;
        }

        public UUID getId()
        { return  uuidPair.getKey();}

        public Map.Entry<UUID, Pair<String[], String[]>> getUuidPair() {
            return uuidPair;
        }

        public String getKey() {
            return CreateJoinLoadMatchViewBuilder.idAndNames(uuidPair).getKey();
        }

        public String getPeople() {
            return    ""+getKey()+
                    "\n"+CreateJoinLoadMatchViewBuilder.idAndNames(uuidPair).getValue();
        }
    }

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


    /**
     * This method is called once upon initialization, and every time the view refreshes
     * @return
     */
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
        (getClient().getCommonData().getAvailableMatchesData().orElse(new HashMap<>()).entrySet()).forEach(e -> templist.add(new MatchRow(e, false)));
        (getClient().getCommonData().getSavedMatchesData().orElse(new HashMap<>()).entrySet()).forEach(e -> templist.add(new MatchRow(e, true)));

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

        Button creationButton=new Button();
        creationButton.setLayoutY(tileheight/2.0);
        creationButton.setLayoutX(tileWidth/2);
        creationButton.setOnAction( p ->
        {
            created=true;
            getClient().changeViewBuilder(new CreateMatchGUI());
        });

        creationButton.setText("CREATE");

        Effect dropShadow=new DropShadow(BlurType.GAUSSIAN,Color.rgb(0,0,0,0.5),10,0.7,5,5);
        createPane.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.DOTTED,CornerRadii.EMPTY,BorderWidths.DEFAULT)));
        createPane.setStyle(" -fx-background-color: linear-gradient(to right, #5771f2, #021782);");
        createPane.setEffect(dropShadow);
        createPane.getChildren().add(creationButton);
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
        templabel.setStyle("-fx-text-fill:WHITE; -fx-font-size: 18;");
        templabel.setMaxSize(tileWidth,200);
        templabel.setLayoutY(10);
        templabel.setLayoutX(tileWidth/2);

        joinPane.getChildren().add(templabel);

        Button joinMatchButton=new Button();
        joinMatchButton.setLayoutY(tileheight/2);
        joinMatchButton.setLayoutX(tileWidth/2);
        joinMatchButton.setOnAction( p ->
        {
           getClient().getServerHandler().sendCommandMessage(new JoinMatchRequest(matchRow.getId(),getClient().getCommonData().getThisPlayerNickname()));
           getClient().changeViewBuilder(LobbyViewBuilder.getBuilder(getClient().isCLI()));
        });



        Effect dropShadow=new DropShadow(BlurType.GAUSSIAN,Color.rgb(0,0,0,0.5),10,0.7,5,5);
        if(matchRow.isSaved)
            joinMatchButton.setText("LOAD");
        else
            joinMatchButton.setText("JOIN");
        joinPane.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.DOTTED,CornerRadii.EMPTY,BorderWidths.DEFAULT)));
        joinPane.setStyle(" -fx-background-color: linear-gradient(to right, #5771f2, #021782);");
        joinPane.setEffect(dropShadow);

        joinPane.getChildren().add(joinMatchButton);

        return joinPane;
    }


    /**
     * This method adds a grid to the Pane
     * @param url is ignored
     * @param resourceBundle is ignored
     */
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



        while(!temp.isEmpty())
        {

            //todo improve
            if(column==maxcolumn)
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
        grid.setStyle("    -fx-background-color: linear-gradient(to right, #ADD8E6, #5771f2);");
        cjlPane.getChildren().remove(guiMatchesData);
        cjlPane.getChildren().add(scrollPane);
        grid.setId("background2");


     //w   grid.add(test,2,2);


    }
}
