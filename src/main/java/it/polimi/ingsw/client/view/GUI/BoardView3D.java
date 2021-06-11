package it.polimi.ingsw.client.view.GUI;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class BoardView3D extends it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder implements GUIView{

    Camera camera;
    public AnchorPane boardPane;
    private double anchorX, anchorY;
    //Keep track of current angle for x and y
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    double buttonStartingX=100;
    double width=1800;
    double len=1000;
    //We will update these after drag. Using JavaFX property to bind with object
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    Box toPut;


    @Override
    public void run() {
        Node toAdd=getRoot();
        //todo fix initialization
        toAdd.setId("3DVIEW");

        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(toAdd);

    }

    public SubScene getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Board3d.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SubScene(root,width,len);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PerspectiveCamera testCamera = new PerspectiveCamera();

        Group parent = new Group();

        Rectangle board = new Rectangle(2402, 1717);
        Image boardPic = new Image("assets/board/biggerboard.png");
        board.setFill(new ImagePattern(boardPic));
        board.setTranslateX(-600);
        board.setTranslateY(-700);
        board.setTranslateZ(2000);
        parent.getChildren().add(board);

        //Box

        Sphere shield = new Sphere(50);
        Color color = Color.CYAN;
        setMaterial(shield, color,false);
        Point3D boardTopLeft = board.localToParent(new Point3D(0,0,0));

        shield.setTranslateX(boardTopLeft.getX());
        shield.setTranslateY(boardTopLeft.getY());
        shield.setTranslateZ(boardTopLeft.getZ());
        //Translate translate = new Translate(stoneCoords.getX(),stoneCoords.getY(),stoneCoords.getZ());
        //shield.getTransforms().add(translate);

        parent.getChildren().add(shield);

        AtomicBoolean dragStarted = new AtomicBoolean(false);
        shield.setOnDragDetected((MouseEvent event)-> {
            if (!dragStarted.get()) {
                dragStarted.set(true);
                setMaterial(shield,color,true);
            }
            shield.setMouseTransparent(true);
            board.setMouseTransparent(false);
            shield.setCursor(Cursor.MOVE);
            shield.startFullDrag();
        });

        shield.setOnMouseReleased((MouseEvent event)-> {
            shield.setMouseTransparent(false);
            dragStarted.set(false);
            setMaterial(shield,color,false);
            board.setMouseTransparent(true);
            shield.setCursor(Cursor.DEFAULT);
        });

        board.setOnMouseDragOver((MouseEvent event)->{
            Point3D stoneCoords = event.getPickResult().getIntersectedPoint();
            stoneCoords = board.localToParent(stoneCoords);

            //stoneCoords = boardTopLeft;
            shield.setTranslateX(stoneCoords.getX());
            shield.setTranslateY(stoneCoords.getY());
            shield.setTranslateZ(stoneCoords.getZ());
            //Translate translate = new Translate(stoneCoords.getX(),stoneCoords.getY(),stoneCoords.getZ());
            //shield.getTransforms().add(translate);
        });


        SubScene scene = new SubScene(parent, width, len);
        testCamera.setTranslateZ(-1000);
        scene.setCamera(testCamera);



        Button viewCardShop=new Button();
        viewCardShop.setLayoutX(buttonStartingX);
        viewCardShop.setLayoutY(50);
        parent.getChildren().add(viewCardShop);
        viewCardShop.setText("VIEW CARD SHOP");
        viewCardShop.setOnMouseEntered( p ->
        {
            //todo fix static
            if(ViewPersonalBoard.getController().isMarket()|| ViewPersonalBoard.getController().isCardShopOpen())
            {
                viewCardShop.setDisable(true);
                return;
            }
            else
                viewCardShop.setDisable(false);
            CardShopGUI cs=new CardShopGUI();
            cs.run();
        });

        viewCardShop.setOnMouseExited( p ->
        {
            if(ViewPersonalBoard.getController().isMarket()|| ViewPersonalBoard.getController().isCardShopOpen())
                return;
            ((Pane)getClient().getStage().getScene().getRoot()).getChildren().remove(((Pane)getClient().getStage().getScene().getRoot()).getChildren().size()-1);
        });



        Button viewMarket=new Button();
        viewMarket.setLayoutX(buttonStartingX);
        viewMarket.setLayoutY(100);
        viewMarket.setText("VIEW MARKET");
        viewMarket.setOnMouseEntered( p ->
        {
            if(ViewPersonalBoard.getController().isMarket()|| ViewPersonalBoard.getController().isCardShopOpen())
            {
                viewMarket.setDisable(true);
                return;
            }
            else
                viewMarket.setDisable(false);
            ResourceMarketGUI cs=new ResourceMarketGUI();
            cs.run();
        });

        viewMarket.setOnMouseExited( p ->
        {
            if(ViewPersonalBoard.getController().isMarket()|| ViewPersonalBoard.getController().isCardShopOpen())
                return;
            ((Pane)getClient().getStage().getScene().getRoot()).getChildren().remove(((Pane)getClient().getStage().getScene().getRoot()).getChildren().size()-1);
        });

        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());

        viewMarket.setId("showButton");
        viewCardShop.setId("showButton");
        boardPane.getChildren().add(viewCardShop);
        boardPane.getChildren().add(viewMarket);
        boardPane.getChildren().add(scene);
    }

    @NotNull
    private void setMaterial(Sphere shield, Color color, boolean selected) {
        Color c2 = Color.hsb(color.getHue(),
                0.3,
                1);
        Color c = selected?c2:color;
        PhongMaterial shieldMaterial = new PhongMaterial(c);
        shield.setMaterial(shieldMaterial);
    }
}
