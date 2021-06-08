package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.ChooseWhiteMarbleConversionEvent;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChooseResourceForMarket extends ResourceMarketViewBuilder implements GUIView {

    public AnchorPane chooseMarketAnchor;
    javafx.collections.ObservableList<Integer> selectedResources;
    List<Button> sceneResources=new ArrayList<>();
    int resourcesToChoose=2;
    List<Sphere> sceneMarbles=new ArrayList<>();

    double width=200;
    double len=200;

    @Override
    public void choosePositions() {

    }

    @Override
    public void run() {
        SubScene toadd=getRoot();
        toadd.setTranslateX(-400);
        toadd.setTranslateY(-250);
        toadd.setId("CHOOSEFORMARKET");

        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(toadd);
        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());
    }

    public void moveX(Sphere sphere,double x, Duration duration)
    {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.75), sphere);
        transition.setToX(x);
        transition.setDelay(duration);
        transition.setAutoReverse(false);
        transition.play();
    }

    public SubScene getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/ChooseResourceForMarket.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SubScene(root,200,200);

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        selectedResources =javafx.collections.FXCollections.observableArrayList();

        List<Color> resToCol=new ArrayList<>();
        resToCol.add(Color.YELLOW);
        resToCol.add(Color.PURPLE);
        resToCol.add(Color.BLUE);
        resToCol.add(Color.GREY);

        Sphere marble=new Sphere(5);
        marble.setLayoutY(len/10);
        marble.setLayoutX(10);
        sceneMarbles.add(marble);
        for(int i=0;i<4;i++)
        {
            Button tempButton=new Button();
            tempButton.setLayoutY(len/4+i*len/8+30);
            tempButton.setLayoutX(width/4+width/8-15);
            int finalI = i;
            tempButton.setOnAction(p->
                    {
                        ChooseWhiteMarbleConversionEvent event=new ChooseWhiteMarbleConversionEvent(finalI);
                        getClient().getServerHandler().sendCommandMessage(new EventMessage(event));
                        resourcesToChoose--;
                        if(resourcesToChoose==0)
                            System.out.println("remove me");
                        marble.setMaterial(new PhongMaterial(resToCol.get(finalI)));
                        moveX(marble,200,new Duration(200));
                    });
            sceneResources.add(tempButton);
            chooseMarketAnchor.getChildren().add(tempButton);

        }
        //todo fix
        List<Image> res=new ArrayList<>();
            res.add(new Image("assets/resources/GOLD.png"));
            res.add(new Image("assets/resources/SERVANT.png"));
            res.add(new Image("assets/resources/SHIELD.png"));
            res.add(new Image("assets/resources/STONE.png"));
        chooseMarketAnchor.getChildren().add(marble);
        for(int i=0;i<sceneResources.size();i++)
        {
            ImageView tempImageView=new ImageView(res.get(i));
            tempImageView.setFitHeight(20);
            tempImageView.setFitWidth(20);
            sceneResources.get(i).setGraphic(tempImageView);

        }


    }
}
