package it.polimi.ingsw.client.view.GUI;


import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.CLI.IDLEViewBuilder;
import it.polimi.ingsw.client.view.GUI.GUIelem.ButtonSelectionModel;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * The user will be asked to insert a nickname and the number of players
 */
public class CardShopGUI extends CardShopViewBuilder implements GUIView {


    public GridPane cardsGrid;
    int ROWS=3;
    int COLUMNS=4;
    List<UUID> cardsUUIDs=new ArrayList<>();

    List<Button> scenesCardsToChoose=new ArrayList<>();

    javafx.collections.ObservableList<Boolean> selected;



    @Override
    public void run() {
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().remove(0);
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(getRoot());
        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());

    }

    public Parent getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/CardShop.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return root;

    }

    /**
     *
     * @param selected is a boolean array used to represent selection
     * @param scenesLeadersToChoose is a button array for the user to execute selection
     */




    /**
     * given a color and a position, the method will build one accordingly
     * @param color is a css color string
     * @param y is an int position
     * @param x is an int position
     * @return a spinner according to parameters
     */

    /**
     * service method
     * @return a button according to parameters
     */
    public Button validationButton()
    {
        Button confirm=new Button();
        confirm.setText("CONFIRM");
        confirm.setLayoutY(800);
        confirm.setLayoutX(450);
        confirm.setOnAction(p -> {

            List<UUID> discardedLeaders=new ArrayList<>();
            for(int i=0;i<scenesCardsToChoose.size();i++)
                if(selected.get(i))
                    System.out.println(scenesCardsToChoose.get(i));

        });
        return confirm;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        ButtonSelectionModel selectionModel=new ButtonSelectionModel();


        Button tempbut;
        for(int i=0;i<ROWS;i++)
        {
            for(int j=0;j<COLUMNS;j++)
            {
                ImageView temp = new ImageView(new Image("assets/leaders/raw/FRONT/Masters of Renaissance_Cards_FRONT_0.png", true));

                tempbut= new Button();
                temp.setFitWidth(20);
                temp.setFitHeight(20);

                tempbut.setGraphic(temp);


                cardsGrid.add(tempbut,i,j);
                scenesCardsToChoose.add(tempbut);

            }
        }
        cardsGrid.setHgap(30);
        cardsGrid.setVgap(30);

        cardsGrid.add(new Label("topo"),3,3);


        selected=javafx.collections.FXCollections.observableArrayList();
        for (int i=0;i<ROWS*COLUMNS;i++)
            selected.add(false);


        selectionModel.cardSelector(selected,scenesCardsToChoose,1);

        selected.addListener(new javafx.collections.ListChangeListener<Boolean>() {
            @Override
            public void onChanged(Change<? extends Boolean> c) {
                c.next();
                if(c.getAddedSubList().get(0))
                    scenesCardsToChoose.get(c.getFrom()).setLayoutY(scenesCardsToChoose.get(c.getFrom()).getLayoutY()-30);
                else
                    scenesCardsToChoose.get(c.getFrom()).setLayoutY(scenesCardsToChoose.get(c.getFrom()).getLayoutY()+30);


            }});






        cardsGrid.add(validationButton(),3,3);
        getClient().getStage().show();

    }


    /**
     * Get called when choosing resources to buy the card
     */
    @Override
    public void choseResources() {

    }

    /**
     * Gets called when the player needs to place the development card on his personal board
     */
    @Override
    public void choosePositionForCard() {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(State.IDLE.name()))
            getClient().changeViewBuilder(new IDLEViewBuilder());
        else{
            System.out.println("Setup received: "+evt.getPropertyName()+ JsonUtility.serialize(evt.getNewValue()));
        }
    }
}