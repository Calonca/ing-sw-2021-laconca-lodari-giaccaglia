package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.GUI.board.CamState;
import it.polimi.ingsw.client.view.GUI.util.DragAndDropHandler;
import it.polimi.ingsw.client.view.GUI.util.ResourceGUI;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.SimpleWarehouseLeadersDepot;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BoardView3D extends it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder implements GUIView{

    public AnchorPane boardPane;
    double buttonStartingX=100;
    double width=1800;
    double len=1000;
    private static CamState camState = CamState.TOP;

    @Override
    public void run() {
        Node root=getRoot();
        //todo fix initialization
        root.setId("3DVIEW");

        GUI.getRealPane().getChildren().add(root);

        System.out.println(GUI.getRealPane().getChildren());

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
        PerspectiveCamera camera = new PerspectiveCamera();

        Group parent = new Group();

        final int boardWidth=2402;
        final int boardHeight=1717;
        Rectangle board = new Rectangle(boardWidth, boardHeight);
        Image boardPic = new Image("assets/board/biggerboard.png");
        board.setFill(new ImagePattern(boardPic));
        board.setTranslateX(-300);
        board.setTranslateY(-320);
        board.setTranslateZ(1500);
        parent.getChildren().add(board);

        int shift = 200;
        int rNum = 0;
        Shape3D stone = addAndGetShape(parent, board, ResourceGUI.STONE,new Point3D(shift*rNum,shift*rNum,0));
        rNum++;
        Shape3D gold = addAndGetShape(parent, board, ResourceGUI.GOLD,new Point3D(shift*rNum,shift*rNum,0));
        rNum++;
        Shape3D servant = addAndGetShape(parent, board, ResourceGUI.SERVANT,new Point3D(shift*rNum,shift*rNum,0));

        DragAndDropHandler ddHandler = new DragAndDropHandler();
        ddHandler.addShape(ResourceGUI.GOLD,gold,()->{},true);
        ddHandler.addShape(ResourceGUI.SERVANT,servant,()->{},true);

        ddHandler.startDragAndDrop(parent,board, stone,ResourceGUI.STONE.generateShape());

        wareBuilder(parent,board);

        getClient().getStage().getScene().setOnKeyPressed(e-> {
            KeyCode pressed = e.getCode();
            camState.animateWithKeyCode(camera,pressed);
        });

        SubScene scene = new SubScene(parent, width, len);
        scene.setCamera(camera);


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
            else {
                viewCardShop.setDisable(false);
            }
            CardShopGUI cs=new CardShopGUI();
            cs.run();
        });

        viewCardShop.setOnMouseExited( p ->
        {
            if(ViewPersonalBoard.getController().isMarket()|| ViewPersonalBoard.getController().isCardShopOpen()) {
                return;
            }
            GUI.removeLast();

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
            else {
                viewMarket.setDisable(false);
            }
            ResourceMarketGUI cs=new ResourceMarketGUI();
            cs.run();
        });

        viewMarket.setOnMouseExited( p ->
        {
            if(ViewPersonalBoard.getController().isMarket()|| ViewPersonalBoard.getController().isCardShopOpen()) {
                return;
            }
            GUI.removeLast();
        });


        viewMarket.setId("showButton");
        viewCardShop.setId("showButton");
        boardPane.getChildren().add(viewCardShop);
        boardPane.getChildren().add(viewMarket);
        boardPane.getChildren().add(scene);
    }

    private void wareBuilder(Group parent, Rectangle board){
        DragAndDropHandler dropHandler = new DragAndDropHandler();
        final double xToStart = 100;
        final double yToStart = 800;
        final double wareWidth = 500;
        final double lineHeight = 150;
        SimpleWarehouseLeadersDepot simpleWarehouseLeadersDepot = getThisPlayerCache().getElem(SimpleWarehouseLeadersDepot.class).orElseThrow();

        int lineN = 0;
        for (Map.Entry<Integer, List<Pair<ResourceAsset, Boolean>>> line: simpleWarehouseLeadersDepot.getDepots().entrySet()){
            int finalLineN = lineN;
            AtomicInteger nInLine = new AtomicInteger();
            line.getValue().forEach(e->{
                double x  = xToStart+(wareWidth/(1+line.getValue().size())*(1+nInLine.get()));
                Point3D shift = new Point3D(x,yToStart+lineHeight* finalLineN,0);
                nInLine.getAndIncrement();
                ResourceGUI resourceGUI = ResourceGUI.fromAsset(e.getKey());
                Shape3D testShape = addAndGetShape(parent,board,resourceGUI,shift);
                dropHandler.addShape(resourceGUI,testShape,()->{},true);
                testShape.setOnMouseEntered((MouseEvent event)-> {
                    //dropHandler.startDragAndDrop(parent,board,testShape, resourceGUI.generateShape());
                    System.out.println("Entered in line "+line.getKey()+", position "+nInLine.get());
                });
            });
            lineN++;
        }
        //dropHandler.startDragAndDropOnEach(parent,board);
    }

    @NotNull
    private Shape3D addAndGetShape(Group parent, Rectangle board, ResourceGUI res,Point3D shift) {
        Shape3D stoneMesh = res.generateShape();

        Point3D boardTopLeft = board.localToParent(new Point3D(0,0,0));
        stoneMesh.setTranslateX(boardTopLeft.getX()+shift.getX());
        stoneMesh.setTranslateY(boardTopLeft.getY()+shift.getY());
        stoneMesh.setTranslateZ(boardTopLeft.getZ()+shift.getZ());
        Rotate rotate1 = new Rotate(270   ,new Point3D(1,0,0));
        Rotate rotate2 = new Rotate(270   ,new Point3D(0,1,0));
        stoneMesh.getTransforms().addAll(rotate1,rotate2);
        parent.getChildren().add(stoneMesh);
        return stoneMesh;
    }


    public static void setCamState(CamState camState) {
        BoardView3D.camState = camState;
    }




}


