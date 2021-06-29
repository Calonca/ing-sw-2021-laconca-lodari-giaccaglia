package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.abstractview.LobbyViewBuilder;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/**
 * Waiting screen
 */
public class LobbyViewBuilderGUI extends LobbyViewBuilder {

    Text playerNames;
    Text playerText;


    /**
     * While waiting for the match to start, the player can see some information regarding the game status
     */
    @Override
    public void run() {

        Node root=getRoot();
        root.setId("WAITING");
        GUI.getRealPane().getChildren().remove(0);
        GUI.getRealPane().getChildren().add(root);

        System.out.println(GUI.getRealPane().getChildren());

    }

    /**
     * Gets called once, upon entering the match
     * @return a waiting screen subscene
     */
    public SubScene getRoot() {


        AnchorPane createPane = new AnchorPane();

        createPane.setMinSize(500,400);
        Sphere circle1=new Sphere();
        circle1.setRadius(10);
        circle1.setLayoutX(500);
        circle1.setLayoutY(100);
        Group group=new Group();

        TranslateTransition transition = new TranslateTransition(Duration.seconds(1.5),circle1);
        transition.setToX(0);
        transition.setToY(500);
        transition.setAutoReverse(true);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.play();

        group.getChildren().add(circle1);


        Sphere circle2=new Sphere();
        circle2.setRadius(10);
        circle2.setLayoutX(500);
        circle2.setLayoutY(600);


        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(1.5),circle2);
        transition2.setToX(0);
        transition2.setToY(-500);
        transition2.setAutoReverse(true);
        transition2.setCycleCount(Animation.INDEFINITE);
        transition2.play();



        group.getChildren().add(circle2);


        Sphere circle3=new Sphere();
        circle3.setRadius(10);
        circle3.setLayoutX(100);
        circle3.setLayoutY(350);


        TranslateTransition transition3 = new TranslateTransition(Duration.seconds(1.5),circle3);
        transition3.setToX(800);
        transition3.setToY(0);
        transition3.setAutoReverse(true);
        transition3.setCycleCount(Animation.INDEFINITE);
        transition3.play();

        group.getChildren().add(circle3);

        Sphere circle4=new Sphere();
        circle4.setRadius(10);
        circle4.setLayoutX(900);
        circle4.setLayoutY(350);

        TranslateTransition transition4 = new TranslateTransition(Duration.seconds(1.5),circle4);
        transition4.setToX(-800);
        transition4.setToY(0);
        transition4.setAutoReverse(true);
        transition4.setCycleCount(Animation.INDEFINITE);
        transition4.play();

        group.getChildren().add(circle4);

        RotateTransition rotateTransition=  new RotateTransition(Duration.seconds(3),group);
        rotateTransition.setByAngle(720);
        rotateTransition.setAutoReverse(true);
        rotateTransition.setCycleCount(Animation.INDEFINITE);
        rotateTransition.play();
        createPane.getChildren().add(group);
        getClient().getStage().show();
        playerNames=new Text("WAITING FOR DATA");
        playerNames.setLayoutX(100);
        playerNames.setLayoutY(100);
        createPane.getChildren().add(playerNames);

        playerText =new Text();
        playerText.setLayoutY(50);
        playerText.setLayoutX(100);
        createPane.getChildren().add(playerText);
        return new SubScene(createPane,1000,700);

    }


    /**
     * Here the info regarding the game gets updated in real time
     * @param evt
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(CommonData.matchesDataString) || evt.getPropertyName().equals(CommonData.thisMatchData) |! evt.getPropertyName().equals(PlayersInfo.class.getSimpleName()))
            Platform.runLater(()->
            {
                if (!(getCommonData().getAvailableMatchesData().isPresent() && getCommonData().getMatchId().isPresent()))
                    return;
                Map<UUID, Pair<String[], String[]>> availableMatches = getCommonData().getAvailableMatchesData().get();

                UUID matchId = getClient().getCommonData().getMatchId().get();
                String[] players = availableMatches.get(matchId).getKey();

                int leftPlayers = (int)(Arrays.stream(players).filter(player -> player.equals("available slot")).count());
                System.out.println(Arrays.toString(players));

                playerNames.setText(Arrays.toString(players));

                if(leftPlayers!=0)
                    playerText.setText("Waiting for other players to join, " + leftPlayers + " more to start!");
                else
                    playerText.setText("Waiting for your turn");
                getClient().getStage().show();

            });
    }


}