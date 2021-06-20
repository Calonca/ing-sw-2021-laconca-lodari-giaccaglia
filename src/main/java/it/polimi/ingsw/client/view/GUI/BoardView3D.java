package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.GUI.board.Warehouse3D;
import it.polimi.ingsw.client.view.GUI.layout.ResChoiceRowGUI;
import it.polimi.ingsw.client.view.abstractview.ResChoiceRow;
import it.polimi.ingsw.client.view.GUI.board.Box3D;
import it.polimi.ingsw.client.view.GUI.board.CamState;
import it.polimi.ingsw.client.view.GUI.board.FaithTrack;
import it.polimi.ingsw.client.view.GUI.util.*;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ProductionViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.simplemodel.*;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.*;

public class BoardView3D {

    public enum Mode{

        MOVING_RES() {
            public void run() {
                final DragAndDropHandler ddHandler = new DragAndDropHandler();
                final Rectangle board = SetupPhase.getBoard().board;
                final Group parent = SetupPhase.getBoard().parent;
                Box3D.discardBuilder(SetupPhase.getBoard(),parent,board , ddHandler);
                Box3D.strongBuilder(SetupPhase.getBoard(),parent,board);
                Warehouse3D.wareBuilder(SetupPhase.getBoard(), parent,board,ddHandler);
                ddHandler.startDragAndDropOnEach(parent,board);
            }
        },
        SELECT_CARD_SHOP(){
            public void run() {
                final Rectangle board = SetupPhase.getBoard().board;
                final Group parent = SetupPhase.getBoard().parent;
                final DragAndDropHandler ddHandler = new DragAndDropHandler();
                CardShopGUI.addChosenCard();
                Box3D.strongBuilder(SetupPhase.getBoard(),parent,board);
                SetupPhase.getBoard().resRowBuilder(parent,board);
                Warehouse3D.wareBuilder(SetupPhase.getBoard(), parent,board,ddHandler);
            }
        },
        SELECT_RESOURCE_FOR_PROD(){
            public void run() {
                SetupPhase.getBoard().addNodeToParent(SetupPhase.getBoard().parent,SetupPhase.getBoard().getController().getBoughtCard(),new Point3D(300,150,0));
                final DragAndDropHandler ddHandler = new DragAndDropHandler();
                final Rectangle board = SetupPhase.getBoard().board;
                final Group parent = SetupPhase.getBoard().parent;
                Box3D.strongBuilder(SetupPhase.getBoard(),parent,board);
                SetupPhase.getBoard().resRowBuilder(parent,board);
                Warehouse3D.wareBuilder(SetupPhase.getBoard(), parent,board,ddHandler);
            }
        },
        CHOOSE_PRODUCTION() {
            public void run() {

                final Group parent = SetupPhase.getBoard().parent;
                final DragAndDropHandler ddHandler = new DragAndDropHandler();
                final Rectangle board = SetupPhase.getBoard().board;
                SetupPhase.getBoard().productionBuilder(parent);
                Box3D.strongBuilder(SetupPhase.getBoard(),parent,board);
                Warehouse3D.wareBuilder(SetupPhase.getBoard(), parent,board,ddHandler);
            }

        },
        CHOOSE_POS_FOR_CARD() {
            public void run() {
                final DragAndDropHandler ddHandler = new DragAndDropHandler();
                final Rectangle board = SetupPhase.getBoard().board;
                final Group parent = SetupPhase.getBoard().parent;
                CardShopGUI.addChosenCard();
                SetupPhase.getBoard().productionBuilder(parent);
                Box3D.strongBuilder(SetupPhase.getBoard(),parent,board);
                Warehouse3D.wareBuilder(SetupPhase.getBoard(), parent,board,ddHandler);
            }
        },
        BACKGROUND(){
            public void run() {
                final DragAndDropHandler ddHandler = new DragAndDropHandler();
                final Rectangle board = SetupPhase.getBoard().board;
                final Group parent = SetupPhase.getBoard().parent;
                Box3D.strongBuilder(SetupPhase.getBoard(),parent,board);
                SetupPhase.getBoard().productionBuilder(parent);
                Warehouse3D.wareBuilder(SetupPhase.getBoard(), parent,board,ddHandler);
            }
        };
        //CHOOSE_PRODUCTION();
        public abstract void run();

    }

    public Mode mode = Mode.BACKGROUND;
    BoardStateController controller=new BoardStateController();
    double buttonStartingX=100;
    double width=1800;
    public Rectangle board;
    public Group parent;
    double len=1000;

    public boolean active=false;

    protected Group discardBox;
    protected Group warehouse;
    protected Group strongBox;
    protected Group faithTrack;

    protected ResChoiceRowGUI toSelect;

    protected Group cardShop=new Group();
    protected Group resourceMarket=new Group();
    protected CardShopGUI cardShopGUI=new CardShopGUI();
    protected ResourceMarketGUI resourceMarketGUI= new ResourceMarketGUI();
    protected Group productions;

    private static CamState camState = CamState.TOP;


    public void setFaithTrack(Group faithTrack) {
        this.faithTrack = faithTrack;
    }

    public void setDiscardBox(Group discardBox) {
        this.discardBox = discardBox;
    }

    public void setWarehouse(Group warehouse) {
        this.warehouse = warehouse;
    }

    public void refreshCardShop() {
        if(this.cardShop!=null)
            this.cardShop.getChildren().clear();
        Group cardShop=new Group();
        addCardShop(cardShop);
        this.cardShop = cardShop;
        parent.getChildren().add(cardShop);
    }

    public void refreshMarket() {
        if(this.resourceMarket!=null)
            this.resourceMarket.getChildren().clear();
        Group resourceMarket=new Group();
        addResourceMarket(resourceMarket);
        this.resourceMarket = resourceMarket;
        parent.getChildren().add(resourceMarket);
    }
    public void setStrongBox(Group strongBox) {
        this.strongBox = strongBox;
    }

    public void addResourceMarket(Group parent) {
        ResourceMarketGUI resourceMarketGUI=new ResourceMarketGUI();
        addNodeToParent(parent,resourceMarketGUI.getRoot(), board.localToParent(1000,-900,0));

    }

    public void addCardShop(Group parent) {
        CardShopGUI cardShopGUI=new CardShopGUI();
        addNodeToParent(parent ,cardShopGUI.getRoot(), board.localToParent(0,-900,0));

    }

    public void runforStart() {
        Node root=getRoot();
        //todo fix initialization
        root.setId("3DVIEW");

        GUI.getRealPane().getChildren().add(root);

        System.out.println(GUI.getRealPane().getChildren());
        active=true;
    }

    public void reset(){
        if (discardBox!=null)
            discardBox.getChildren().clear();
        if (strongBox!=null)
            strongBox.getChildren().clear();
        if (warehouse!=null)
            warehouse.getChildren().clear();
        if (toSelect!=null){
            toSelect.clear();
            toSelect = null;}
        if (productions!=null)
            productions.getChildren().clear();
        if (CardShopGUI.chosenCard!=null)
            CardShopGUI.chosenCard.getChildren().clear();
    }

    public void changeCamState(CamState state){
        camState.animateToState(state);
    }

    public void setMode(Mode mode){
        reset();
        this.mode = mode;
        mode.run();
    }

    public ResChoiceRowGUI getToSelect() {
        return toSelect;
    }

    public SubScene getRoot() {

        AnchorPane boardPane=new AnchorPane();
        boardPane.setMinSize(1800,1000);

        PerspectiveCamera camera = new PerspectiveCamera();

        parent = new Group();

        final double boardWidth=2402;
        final double boardHeight=1717;
        board = new Rectangle(boardWidth, boardHeight);



        Shape3D table = ModelImporter.getShape3d("table");
        table.setMaterial(new PhongMaterial(Color.BURLYWOOD));
        Point3D tableCenter = new Point3D(400,300,8501);
        table.setTranslateX(tableCenter.getX());
        table.setTranslateY(tableCenter.getY());
        table.setTranslateZ(tableCenter.getZ());
        table.setScaleX(100);
        table.setScaleY(100);
        table.setScaleZ(100);
        table.setMouseTransparent(true);
        parent.getChildren().add(table);

        Image boardPic = new Image("assets/board/biggerboard.png");
        board.setFill(new ImagePattern(boardPic));
        board.setTranslateX(-300);
        board.setTranslateY(-320);
        board.setTranslateZ(1500);
        parent.getChildren().add(board);

        CamState.setCamera(camera);
        getClient().getStage().getScene().setOnKeyPressed(e-> {
            KeyCode pressed = e.getCode();
            camState.animateWithKeyCode(pressed);
        });

        SubScene scene = new SubScene(parent, width, len,true,SceneAntialiasing.DISABLED);
        scene.setCamera(camera);



        FaithTrack faithTrack=new FaithTrack();
        faithTrack.faithTrackBuilder(this,parent,board);
        getClient().addToListeners(faithTrack);


        cardShopGUI=new CardShopGUI();
        resourceMarketGUI=new ResourceMarketGUI();



        refreshMarket();
        refreshCardShop();


        //boardPane.getChildren().add(parent);
        boardPane.getChildren().add(scene);
        return new SubScene(boardPane,width,len);

    }

    public BoardStateController getController() {
        return controller;
    }


    public void resRowBuilder(Group parent, Rectangle board){
        Point3D initialPos = new Point3D(-100,800,0);
        SimpleCardShop simpleCardShop = getSimpleModel().getElem(SimpleCardShop.class).orElseThrow();
        DevelopmentCardAsset card =  simpleCardShop.getPurchasedCard().orElseThrow();
        toSelect = new ResChoiceRowGUI(0,card.getDevelopmentCard().getCosts(),new ArrayList<>());
        Group resRowGroup = toSelect.getRowGroup();

        resRowGroup.setTranslateX(initialPos.getX());
        resRowGroup.setTranslateX(initialPos.getY());
        resRowGroup.setTranslateX(initialPos.getZ());

        addNodeToParent(parent, board, resRowGroup, initialPos);
    }

    public void addNodeToParent(Group parent, Node board, Node shape, Point3D shift){
        Point3D boardTopLeft = board.localToParent(new Point3D(0,0,0));
        shape.setTranslateX(boardTopLeft.getX()+shift.getX());
        shape.setTranslateY(boardTopLeft.getY()+shift.getY());
        shape.setTranslateZ(boardTopLeft.getZ()+shift.getZ());
        parent.getChildren().add(shape);
    }

    private void addNodeToParent(Group parent,Node shape, Point3D shift){
        Point3D boardTopLeft = parent.localToParent(new Point3D(0,0,0));
        shape.setTranslateX(boardTopLeft.getX()+shift.getX());
        shape.setTranslateY(boardTopLeft.getY()+shift.getY());
        shape.setTranslateZ(boardTopLeft.getZ()+shift.getZ());
        parent.getChildren().add(shape);
    }

    private void productionBuilder(Group parent) {
        Group productions = new Group();

        Rectangle basic=new Rectangle(300,300);
        basic.setOpacity(0);
        //basic.setMouseTransparent(true);

        productions.getChildren().add(basic);
        addNodeToParent(parent,board,basic,new Point3D(600,1200,0));
        basic.setOnMouseClicked(p->{
            if (mode.equals(Mode.CHOOSE_PRODUCTION)) {
                ProductionViewBuilder.sendChosenProduction(0);
            }
        });

        SimpleCardCells simpleCardCells = getThisPlayerCache().getElem(SimpleCardCells.class).orElseThrow();
        Map<Integer,Optional<DevelopmentCardAsset>> frontCards=simpleCardCells.getDevCardsCells();

        for (Map.Entry<Integer, Optional<DevelopmentCardAsset>> entry : frontCards.entrySet()) {
            Integer key = entry.getKey();
            Optional<DevelopmentCardAsset> value = entry.getValue();
            ImagePattern tempImage;
            Path path;
            Rectangle rectangle = new Rectangle(462, 698);
            //rectangle.setMouseTransparent(true);
            System.out.println(JsonUtility.serialize(value.orElse(null)));
            if (value.isEmpty()) {
                tempImage = new ImagePattern(new Image("assets/devCards/grayed out/BACK/Masters of Renaissance__Cards_BACK_BLUE_1.png"));
                rectangle.setOpacity(0);
            } else {
                path = value.get().getCardPaths().getKey();
                tempImage = new ImagePattern(new Image(path.toString(), false));
            }
            rectangle.setLayoutX(400 + 250 * key);
            rectangle.setLayoutY(0);
            rectangle.setOnMouseClicked(p -> {
                if (mode.equals(Mode.CHOOSE_POS_FOR_CARD))
                    if (simpleCardCells.isSpotAvailable(key)) {
                        CardShopViewBuilder.sendCardPlacementPosition(key);
                    } else if (mode.equals(Mode.CHOOSE_PRODUCTION))
                        if (value.isPresent())
                            ProductionViewBuilder.sendChosenProduction(key);
            });
            rectangle.setFill(tempImage);

            addNodeToParent(parent, board, rectangle, new Point3D(20 + 220 * key, 700, -20));
        }
        parent.getChildren().add(productions);
        this.productions = productions;
    }

    @NotNull
    public Shape3D addAndGetShape(Group parent, Node refSystem, ResourceGUI res, Point3D shift) {
        Shape3D stoneMesh = res.generateShape();
        addNodeToParent(parent,refSystem,stoneMesh,shift);
        return stoneMesh;
    }


    public static void setCamState(CamState camState) {
        BoardView3D.camState = camState;
    }





}


