package it.polimi.ingsw.client.view.GUI;


import it.polimi.ingsw.client.Client;


import it.polimi.ingsw.client.json.Deserializator;
import it.polimi.ingsw.network.assets.CardAssetsContainer;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;
import it.polimi.ingsw.server.model.Resource;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The user will be asked to insert a nickname and the number of players
 */
public class SetupPhase extends  it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder implements GUIView {

    /**
     * By changin LEADERNUMBER the number of received leaders to show can be easily changed
     */
    private final int LEADERNUMBER=4;
    List<Button> sceneButtons=new ArrayList<>();
    boolean[] selected;
    List<String> colorsToRes=new ArrayList<>();
    List<Spinner<Integer>> sceneSpinners= new ArrayList<>();

    @FXML
    private AnchorPane cjlAnchor;


    @Override
    public void run() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/SetupMenu.fxml"));
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

    /**
     *
     * @param selected is a boolean array used to represent selection
     * @param sceneButtons is a button array for the user to execute selection
     */
    public void bindForSelection(boolean[] selected,List<Button> sceneButtons)
    {
        ImageView temp;

        //SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        //List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();


        for (Button sceneButton : sceneButtons) {


        //sceneButton.setGraphic(temp);
        sceneButton.setOnAction(e ->
        {
            if(!selected[sceneButtons.indexOf(sceneButton)])
            {
                sceneButton.setLayoutY(sceneButton.getLayoutY()-30);
                selected[sceneButtons.indexOf(sceneButton)]=true;
            }
            else
            {
                sceneButton.setLayoutY(sceneButton.getLayoutY()+30);

                selected[sceneButtons.indexOf(sceneButton)]=false;
            }
            Client.getInstance().getStage().show();


        });

    }}

    /**
     * given a color and a position, the method will build one accordingly
     * @param color is a css color string
     * @param y is an int position
     * @param x is an int position
     * @return a spinner according to parameters
     */
    public Spinner<Integer> colorAndAnimate(String color ,int y, int x)
    {
        Spinner<Integer> spin=new Spinner<>(0,3,0);
        spin.setPadding(new Insets(10,10,10,10));
        spin.setBackground(Background.EMPTY);
        spin.styleProperty().set(color);
        spin.setLayoutY(y);
        spin.setLayoutX(x);
        spin.setOnMouseClicked( p ->
        {
            Circle circle2=new Circle();
            circle2.setRadius(10);
            circle2.setLayoutX(x);
            circle2.setLayoutY(y);

            FillTransition filltransition2= new FillTransition(Duration.seconds(1.5),circle2,Color.GOLD,Color.PURPLE);
            filltransition2.setCycleCount(Animation.INDEFINITE);
            filltransition2.setAutoReverse(true);
            filltransition2.play();

            TranslateTransition transition2 = new TranslateTransition(Duration.seconds(1.5),circle2);
            transition2.setToX(0);
            transition2.setToY(-200);
            transition2.setAutoReverse(false);
            transition2.play();
            cjlAnchor.getChildren().add(circle2);

            Client.getInstance().getStage().show();
        });

        return spin;
    }

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

            for(int i=0;i<sceneSpinners.size();i++)
                System.out.println(sceneSpinners.get(i).getValue() + Resource.fromInt(i).toString());
            for(int i=0;i<sceneButtons.size();i++)
                System.out.println((selected[i]+ sceneButtons.get(i).toString()));
            Client.getInstance().changeViewBuilder(new MarketMatrix());

        });
        return confirm;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        int y=200;
        int x=120;

        colorsToRes.add(" -fx-background-color: #B8860B");
        colorsToRes.add(" -fx-background-color: #0099FF");
        colorsToRes.add(" -fx-background-color: #DEB887");
        colorsToRes.add(" -fx-background-color: #9400D3");

        /**
         * Spinner are built accordig to resource colors
         */
        //TODO ADD GETCOLOR TO RESOURCE
        Spinner<Integer> colorToResSpin;
        for (String colorsToRe : colorsToRes) {
            colorToResSpin = colorAndAnimate(colorsToRe, y, x);
            cjlAnchor.getChildren().add(colorToResSpin);
            sceneSpinners.add(colorToResSpin);
            x += 200;
        }


        /**
         * Buttons are built according to leadernumber parameter
         */

        CardAssetsContainer.setCardAssetsContainer(Deserializator.networkDevCardsAssetsDeserialization());
        SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        List<LeaderCardAsset> leaderpics=simplePlayerLeaders.getPlayerLeaders();

        Button tempbut;
        for(int i=0;i<LEADERNUMBER;i++)
        {
            ImageView temp = new ImageView(new Image("assets/leaders/raw/FRONT/Masters of Renaissance_Cards_FRONT_0.png", true));

            tempbut= new Button();
            tempbut.setLayoutY(600);
            tempbut.setLayoutX(100+200*i);
            tempbut.setGraphic(temp);
            temp.setFitHeight(200);
            temp.setFitWidth(200);
            cjlAnchor.getChildren().add(tempbut);
            sceneButtons.add(tempbut);

        }

        selected=new boolean[LEADERNUMBER];
        Arrays.fill(selected, false);



        //TODO MAKE METHOD WHICH TAKES RUNNABLE, BOOLEAN ARRAY AND BUTTON ARRAY TO BIND
       bindForSelection(selected,sceneButtons);



       cjlAnchor.getChildren().add(validationButton());
       Client.getInstance().getStage().show();

    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}