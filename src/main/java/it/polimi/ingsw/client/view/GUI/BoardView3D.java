package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody.Mode;
import it.polimi.ingsw.client.view.CLI.layout.GridElem;
import it.polimi.ingsw.client.view.CLI.layout.SizedBox;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.GUI.board.CamState;
import it.polimi.ingsw.client.view.GUI.board.Text3d;
import it.polimi.ingsw.client.view.GUI.util.DragAndDropData;
import it.polimi.ingsw.client.view.GUI.util.DragAndDropHandler;
import it.polimi.ingsw.client.view.GUI.util.ResourceGUI;
import it.polimi.ingsw.client.view.abstractview.ProductionViewBuilder;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.SimpleDiscardBox;
import it.polimi.ingsw.network.simplemodel.SimpleWarehouseLeadersDepot;
import it.polimi.ingsw.server.controller.SessionController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BoardView3D extends it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder implements GUIView{

    public enum Mode{

        MOVING_RES(),
        SELECT_CARD_SHOP(),
        SELECT_RES_FOR_PROD(),
        CHOOSE_POS_FOR_CARD(),
        CHOOSE_PRODUCTION()

    }

    public AnchorPane boardPane;

    public Mode mode;
    double buttonStartingX=100;
    double width=1800;
    double len=1000;
    private static CamState camState = CamState.TOP;
    public static final boolean moveFreely = true;
    private static BoardView3D single_instance = null;

    public static BoardView3D getInstance()
    {
        if (Objects.isNull(single_instance))
            return new BoardView3D();//Todo Initialize board
        return single_instance;
    }

    @Override
    public void run() {
        Node root=getRoot();
        //todo fix initialization
        root.setId("3DVIEW");

        GUI.getRealPane().getChildren().add(root);

        System.out.println(GUI.getRealPane().getChildren());

    }

    public void setMode(Mode mode){
        this.mode = mode;
        //Then the function initializes the board.
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

        DragAndDropHandler ddHandler = new DragAndDropHandler();
        wareBuilder(parent,board,ddHandler);
        discardBuilder(parent,board,ddHandler);
        ddHandler.startDragAndDropOnEach(parent,board);

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
        boardPane.getChildren().add(viewMarket);
        //boardPane.getChildren().add(parent);
        boardPane.getChildren().add(scene);
    }

    private void discardBuilder(Group parent, Rectangle board,DragAndDropHandler dropHandler){
        Group discardBoxGroup = new Group();
        //Box background = new Box(350,900,2);
        //Translate t = new Translate();
        //t.setX(20);
        //t.setY(200);
        //t.setZ(0);
        //background.getTransforms().add(t);
        //background.setMouseTransparent(true);
        //discardBoxGroup.getChildren().add(background);
        Point3D initialPos = new Point3D(800,800,0);
        final double wareWidth = 500;
        final double lineHeight = 150;
        SimpleDiscardBox simpleDiscardBox = getThisPlayerCache().getElem(SimpleDiscardBox.class).orElseThrow();
        int addedLines = 0;
                        //Pos       //Res           //NOfRes
        for (Map.Entry<Integer, Pair<ResourceAsset, Integer>> line : simpleDiscardBox.getResourceMap().entrySet()) {
            Group lineGroup = new Group();
            Text text = Text3d.from(line.getValue().getValue());
            text.setLayoutX(100);
            lineGroup.getChildren().add(text);
            lineGroup.setTranslateY(lineHeight * addedLines);
            discardBoxGroup.getChildren().add(lineGroup);

            ResourceGUI resTest = ResourceGUI.fromAsset(line.getValue().getKey());
            addAndGetShape(discardBoxGroup,discardBoxGroup,resTest,new Point3D(0,lineHeight * addedLines,0));


            addedLines++;
        }

        addNodeToBoard(parent, board, discardBoxGroup, initialPos);

    }

    private void wareBuilder(Group parent, Rectangle board,DragAndDropHandler dropHandler){

        final double xToStart = 100;
        final double yToStart = 800;
        final double wareWidth = 500;
        final double lineHeight = 150;
        SimpleWarehouseLeadersDepot simpleWarehouseLeadersDepot = getThisPlayerCache().getElem(SimpleWarehouseLeadersDepot.class).orElseThrow();

        AtomicInteger gPos = new AtomicInteger();
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
                DragAndDropData dragAndDropData = new DragAndDropData();
                dragAndDropData.setResourceGUI(resourceGUI);
                dragAndDropData.setShape3D(testShape);
                dragAndDropData.setAvailable(simpleWarehouseLeadersDepot.isPosAvailable(gPos.get()));
                dragAndDropData.setAvailablePos(simpleWarehouseLeadersDepot.getAvailableMovingPositions().get(gPos.get()));
                dragAndDropData.setGlobalPos(gPos.get());
                dropHandler.addShape(dragAndDropData);
                gPos.getAndIncrement();
            });
            lineN++;
        }
    }


    private void addNodeToBoard(Group parent, Node board, Node shape, Point3D shift){
        Point3D boardTopLeft = board.localToParent(new Point3D(0,0,0));
        shape.setTranslateX(boardTopLeft.getX()+shift.getX());
        shape.setTranslateY(boardTopLeft.getY()+shift.getY());
        shape.setTranslateZ(boardTopLeft.getZ()+shift.getZ());
        parent.getChildren().add(shape);
    }

    @NotNull
    private Shape3D addAndGetShape(Group parent, Node refSystem, ResourceGUI res, Point3D shift) {
        Shape3D stoneMesh = res.generateShape();
        Rotate rotate1 = new Rotate(270   ,new Point3D(1,0,0));
        Rotate rotate2 = new Rotate(270   ,new Point3D(0,1,0));
        stoneMesh.getTransforms().addAll(rotate1,rotate2);
        addNodeToBoard(parent,refSystem,stoneMesh,shift);
        return stoneMesh;
    }


    public static void setCamState(CamState camState) {
        BoardView3D.camState = camState;
    }




}


