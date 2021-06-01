package it.polimi.ingsw.client.view.GUI;


import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.CLI.IDLEViewBuilder;
import it.polimi.ingsw.client.view.GUI.GUIelem.ButtonSelectionModel;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * The CardShop is generated via a method that passes its subscene. It's composed of a small single card
 * selector and a confirmation button
 */
public class CardShopGUI extends CardShopViewBuilder implements GUIView {


    public AnchorPane cardsAnchor;
    int ROWS=3;
    int COLUMNS=4;
    List<UUID> cardsUUIDs=new ArrayList<>();
    boolean enabled=false;
    List<Button> scenesCardsToChoose=new ArrayList<>();

    javafx.collections.ObservableList<Boolean> selected;



    @Override
    public void run() {
        enabled=true;

    }

    public void addMarket() {
        Node toadd=getRoot();
        toadd.setTranslateX(-400);
        toadd.setTranslateY(107);
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(toadd);
        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());

    }

    public SubScene getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/CardShop.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SubScene(root,300,400);

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
        confirm.setLayoutY(350);
        confirm.setLayoutX(150);
        confirm.setOnAction(p -> {




            //todo find solution





        });
        return confirm;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        ButtonSelectionModel selectionModel=new ButtonSelectionModel();

        GridPane cardsGrid=new GridPane();
        cardsGrid.setLayoutX(0);
        cardsGrid.setLayoutY(0);
        cardsGrid.setPadding(new Insets(5,5,5,5));
        Button tempbut;
        for(int i=0;i<ROWS;i++)
        {
            for(int j=0;j<COLUMNS;j++)
            {
                //todo fix order
                ImageView temp = new ImageView(new Image("assets/leaders/raw/FRONT/Masters of Renaissance_Cards_FRONT_0.png", true));

                tempbut= new Button();
                temp.setFitWidth(50);
                temp.setFitHeight(50);

                tempbut.setGraphic(temp);


                cardsGrid.add(tempbut,i,j);
                scenesCardsToChoose.add(tempbut);

            }
        }
        cardsGrid.setHgap(30);
        cardsGrid.setVgap(30);



        selected=javafx.collections.FXCollections.observableArrayList();
        for (int i=0;i<ROWS*COLUMNS;i++)
            selected.add(false);

        selectionModel.dehighlightTrue(selected,scenesCardsToChoose);

        selectionModel.cardSelector(selected,scenesCardsToChoose,1);

        selected.addListener(new javafx.collections.ListChangeListener<Boolean>() {
            @Override
            public void onChanged(Change<? extends Boolean> c) {
                c.next();
                if(c.getAddedSubList().get(0))
                    selectionModel.highlightTrue(selected,scenesCardsToChoose);
                else
                    selectionModel.dehighlightTrue(selected,scenesCardsToChoose);


            }});






        cardsAnchor.getChildren().add(validationButton());
        cardsAnchor.getChildren().add(cardsGrid);


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