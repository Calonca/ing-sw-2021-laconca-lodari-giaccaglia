package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.GUI.board.CamState;
import it.polimi.ingsw.client.view.GUI.util.BoardStateController;
import it.polimi.ingsw.client.view.GUI.util.ModelImporter;
import it.polimi.ingsw.server.model.player.Player;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;

import java.util.Map;
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

        }
        return singleton;
    }

    public static Point3D getTableCenter() {
        return tableCenter;
    }


    /**
     * Method used to refresh card shop
     */
    public static void refreshCardShop() {

        if(getClient().getCommonData().getCurrentPlayerIndex() != CommonData.getThisPlayerIndex())
            return;
        Playground p = getPlayground();
        System.out.println("REFRESHING CARD SHOP");
        if(p.cardShop!=null)
            p.cardShop.getChildren().clear();
        Group cardShop=new Group();
        p.addCardShop(cardShop);
        p.cardShop = cardShop;
        p.root.getChildren().add(cardShop);
    }

    /**
     * Method used to refresh card shop
     */
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

    /**
     * Method used to bootstrap the GUI
     */
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

    /**
     * This method is connected to the camera state machine
     * @param state is the next camera state
     */
    public static void changeCamState(CamState state){
        Playground p = getPlayground();
        p.camState.animateToState(state);
    }

    public void setCamState(CamState camState) {
        this.camState = camState;
    }


    /**
     * @return the main board scene, constructin all the game element representations
     */
    public SubScene getRoot() {

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
                        e.getKey()-thisPlayerIndex,
                Map.Entry::getValue
        ));

        //Sets the counter-clock-wise index of the boards
        boardsClockWise.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e-> e.getValue().setCounterClockWiseIdx(e.getKey()));

        root.getChildren().addAll((boardsClockWise.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .filter(e->e.getKey()<=getThisPlayerBoard().getCounterClockWiseIdx())
                .map(e->e.getValue().getRoot())
                .collect(Collectors.toList())));

        CamState.setCamera(camera);
        getClient().getStage().getScene().setOnKeyPressed(e-> {
            KeyCode pressed = e.getCode();
            camState.animateWithKeyCode(pressed);
        });

        SubScene scene = new SubScene(root, GUI.GUIwidth, GUI.GUIlen,true,SceneAntialiasing.DISABLED);
        scene.setCamera(camera);

        cardShopGUI=new CardShopGUI();
        resourceMarketGUI=new ResourceMarketGUI();

        refreshMarket();
        refreshCardShop();

        Playground.changeCamState(CamState.TOP);
        return scene;
    }

    public BoardView3D getBoard(int i){
        return boards.getOrDefault(i,getThisPlayerBoard());
    }

    public static BoardStateController getController() {
        return controller;
    }

    /**
     * Helper method used to add nodes
     * @param shape is the shape to add
     * @param shift is the starting point
     */
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

}


