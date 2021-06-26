package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.GUI.board.CamState;
import it.polimi.ingsw.client.view.GUI.util.BoardStateController;
import it.polimi.ingsw.client.view.GUI.util.ModelImporter;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static it.polimi.ingsw.client.view.GUI.util.NodeAdder.addNodeToParent;
import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.*;


/**
 * Keeps all the data for the 3d view and the boards of all players
 */
public class Playground {

    private static Playground singleton;
    private static final BoardStateController controller=new BoardStateController();
    private static final Point3D tableCenter = new Point3D(400,-3500,0);
    double width=1800;
    double len=1000;
    private boolean active=false;

    public Group root;

    protected Group cardShop=new Group();
    protected Group resourceMarket=new Group();
    protected CardShopGUI cardShopGUI=new CardShopGUI();
    protected ResourceMarketGUI resourceMarketGUI= new ResourceMarketGUI();
    private CamState camState = CamState.TOP;
    private Map<Integer, BoardView3D> boards;

    public synchronized static Playground getPlayground()
    {
        if (singleton ==null) {
            singleton = new Playground();
            singleton.boards = IntStream.range(0,getSimpleModel().getNumOfPlayers()).boxed().collect(
                    Collectors.toMap(
                    i->i,
                    i-> new BoardView3D(i,getSimpleModel().getPlayerCache(i))
                    ));
            singleton.runforStart();
            Platform.runLater(()->{
                singleton.boards.values().forEach(b->b.setMode(BoardView3D.Mode.BACKGROUND));
            });

        }
        return singleton;
    }

    public static Point3D getTableCenter() {
        return tableCenter;
    }

    public static void refreshCardShop() {
        Playground p = getPlayground();
        System.out.println("REFRESHING CARD SHOP");
        if(p.cardShop!=null)
            p.cardShop.getChildren().clear();
        Group cardShop=new Group();
        p.addCardShop(cardShop);
        p.cardShop = cardShop;
        p.root.getChildren().add(cardShop);
    }

    public static void refreshMarket() {
        Playground p = getPlayground();
        System.out.println("REFRESHING MARKET");
        if(p.resourceMarket!=null)
            p.resourceMarket.getChildren().clear();
        Group resourceMarket=new Group();
        addResourceMarket(resourceMarket);
        p.resourceMarket = resourceMarket;
        p.root.getChildren().add(resourceMarket);
    }

    public static void addResourceMarket(Group parent) {
        ResourceMarketGUI resourceMarketGUI=new ResourceMarketGUI();
        addNodeToParent(parent,resourceMarketGUI.getRoot(), getThisPlayerBoard().boardRec.localToParent(1400,-900,0));

    }

    public void addCardShop(Group parent) {
        CardShopGUI cardShopGUI=new CardShopGUI();
        addNodeToParent(parent ,cardShopGUI.getRoot(), getThisPlayerBoard().boardRec.localToParent(0,-900,0));

    }

    private synchronized void runforStart() {
        if (!active)
        {
            active = true;
            Node root = getRoot();
            root.setId("3DVIEW");
            GUI.getRealPane().getChildren().add(1,root);
            System.out.println(GUI.getRealPane().getChildren());
        }
    }

    public static void changeCamState(CamState state){
        Playground p = getPlayground();
        p.camState.animateToState(state);
    }

    public SubScene getRoot() {

        AnchorPane boardPane=new AnchorPane();
        boardPane.setMinSize(1800,1000);

        PerspectiveCamera camera = new PerspectiveCamera();

        root = new Group();

        Sphere pivotSphere = new Sphere(100);
        pivotSphere.setTranslateX(tableCenter.getX());
        pivotSphere.setTranslateY(tableCenter.getY());
        pivotSphere.setTranslateZ(tableCenter.getZ());

        root.getChildren().add(pivotSphere);

        Shape3D table = ModelImporter.getShape3d("table");
        table.setMaterial(new PhongMaterial(Color.BURLYWOOD));

        Point3D tbPos = new Point3D(400,300,8501);
        table.setTranslateX(tbPos.getX());
        table.setTranslateY(tbPos.getY());
        table.setTranslateZ(tbPos.getZ());
        table.setScaleX(100);
        table.setScaleY(100);
        table.setScaleZ(100);
        table.setMouseTransparent(true);
        root.getChildren().add(table);



        final int thisPlayerIndex = getCommonData().getThisPlayerIndex();
        //Orders the other players' boards based on their position in respect to the player controlling the GUI
        Map<Integer,BoardView3D> boardsClockWise = boards.entrySet().stream().collect(Collectors.toMap(e->
                        (e.getKey()<thisPlayerIndex?e.getKey()+boards.size():e.getKey())-thisPlayerIndex,
                Map.Entry::getValue
        ));

        //Adds the tables in clockwise order
        root.getChildren().addAll((boardsClockWise.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .map(e->e.getValue().getRoot(e.getKey()))
                .collect(Collectors.toList())));

        CamState.setCamera(camera);
        getClient().getStage().getScene().setOnKeyPressed(e-> {
            KeyCode pressed = e.getCode();
            camState.animateWithKeyCode(pressed);
        });

        SubScene scene = new SubScene(root, width, len,true,SceneAntialiasing.DISABLED);
        scene.setCamera(camera);

        cardShopGUI=new CardShopGUI();
        resourceMarketGUI=new ResourceMarketGUI();

        refreshMarket();
        refreshCardShop();

        boardPane.getChildren().add(scene);
        return new SubScene(boardPane,width,len);

    }

    public BoardView3D getBoard(int i){
        return boards.getOrDefault(i,getThisPlayerBoard());
    }

    public static BoardStateController getController() {
        return controller;
    }

    public static void addNodeToThisPlayer(Node shape, Point3D shift){
        Point3D boardTopLeft = getThisPlayerBoard().parent.localToParent(new Point3D(0,0,0));
        shape.setTranslateX(boardTopLeft.getX()+shift.getX());
        shape.setTranslateY(boardTopLeft.getY()+shift.getY());
        shape.setTranslateZ(boardTopLeft.getZ()+shift.getZ());
        getThisPlayerBoard().parent.getChildren().add(shape);
    }

    /**
     * Returns the board of the player the client is playing as
     */
    public static BoardView3D getThisPlayerBoard() {
        final int thisPlayerIndex = getCommonData().getThisPlayerIndex();
        return getPlayground().boards.getOrDefault(thisPlayerIndex,getPlayground().boards.get(0));
    }


    public static void setCamState(CamState camState) {
        getPlayground().camState = camState;
    }


}


