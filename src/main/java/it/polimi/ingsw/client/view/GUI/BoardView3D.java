package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.CLI.layout.ResChoiceRow;
import it.polimi.ingsw.client.view.GUI.board.BoxGUI;
import it.polimi.ingsw.client.view.GUI.board.CamState;
import it.polimi.ingsw.client.view.GUI.util.BoardStateController;
import it.polimi.ingsw.client.view.GUI.util.DragAndDropData;
import it.polimi.ingsw.client.view.GUI.util.DragAndDropHandler;
import it.polimi.ingsw.client.view.GUI.util.ResourceGUI;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ProductionViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.*;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.*;

public class BoardView3D {

    public enum Mode{

        MOVING_RES() {
            public void run() {
                final DragAndDropHandler ddHandler = new DragAndDropHandler();
                final Rectangle board = SetupPhase.getBoard().board;
                final Group parent = SetupPhase.getBoard().parent;
                BoxGUI.discardBuilder(SetupPhase.getBoard(),parent,board , ddHandler);
                BoxGUI.strongBuilder(SetupPhase.getBoard(),parent,board);
                SetupPhase.getBoard().wareBuilder(parent,board,ddHandler);
                ddHandler.startDragAndDropOnEach(parent,board);
            }
        },
        SELECT_CARD_SHOP(){
            public void run() {
                SetupPhase.getBoard().addNodeToParent(SetupPhase.getBoard().parent,SetupPhase.getBoard().getController().getBoughtCard(),new Point3D(300,150,0));
                final DragAndDropHandler ddHandler = new DragAndDropHandler();
                final Rectangle board = SetupPhase.getBoard().board;
                final Group parent = SetupPhase.getBoard().parent;
                BoxGUI.strongBuilder(SetupPhase.getBoard(),parent,board);
                SetupPhase.getBoard().resRowBuilder(parent,board);
                SetupPhase.getBoard().wareBuilder(parent,board,ddHandler);
            }
        },
        CHOOSE_PRODUCTION() {
            public void run() {

                final Group parent = SetupPhase.getBoard().parent;
                final DragAndDropHandler ddHandler = new DragAndDropHandler();
                final Rectangle board = SetupPhase.getBoard().board;
                SetupPhase.getBoard().productionBuilder(parent);
                BoxGUI.strongBuilder(SetupPhase.getBoard(),parent,board);
                SetupPhase.getBoard().wareBuilder(parent,board,ddHandler);
            }

        },
        CHOOSE_POS_FOR_CARD() {
            public void run() {
                final DragAndDropHandler ddHandler = new DragAndDropHandler();
                final Rectangle board = SetupPhase.getBoard().board;
                final Group parent = SetupPhase.getBoard().parent;
                SetupPhase.getBoard().productionBuilder(parent);
                BoxGUI.strongBuilder(SetupPhase.getBoard(),parent,board);
                SetupPhase.getBoard().wareBuilder(parent,board,ddHandler);
            }
        },
        BACKGROUND(){
            public void run() {
                final DragAndDropHandler ddHandler = new DragAndDropHandler();
                final Rectangle board = SetupPhase.getBoard().board;
                final Group parent = SetupPhase.getBoard().parent;
                BoxGUI.strongBuilder(SetupPhase.getBoard(),parent,board);
                SetupPhase.getBoard().wareBuilder(parent,board,ddHandler);
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
    protected Group discardBox;
    protected Group warehouse;
    protected Group strongBox;
    protected ResChoiceRow toSelect;
    protected Group productions;

    private static CamState camState = CamState.TOP;
    public static final boolean moveFreely = true;


    public void setDiscardBox(Group discardBox) {
        this.discardBox = discardBox;
    }

    public void setStrongBox(Group strongBox) {
        this.strongBox = strongBox;
    }

    public void runforStart() {
        Node root=getRoot();
        //todo fix initialization
        root.setId("3DVIEW");

        GUI.getRealPane().getChildren().add(root);

        System.out.println(GUI.getRealPane().getChildren());

    }

    public void reset(){
        if (discardBox!=null)
            discardBox.getChildren().clear();
        if (strongBox!=null)
            strongBox.getChildren().clear();
        if (warehouse!=null)
            warehouse.getChildren().clear();
        if (toSelect!=null)
            toSelect = null;
        if (productions!=null)
            productions.getChildren().clear();
    }

    public void setMode(Mode mode){
        reset();
        this.mode = mode;
        mode.run();
    }

    public ResChoiceRow getToSelect() {
        return toSelect;
    }

    public SubScene getRoot() {

        AnchorPane boardPane=new AnchorPane();
        boardPane.setMinSize(1800,1000);

        PerspectiveCamera camera = new PerspectiveCamera();

        parent = new Group();

        final int boardWidth=2402;
        final int boardHeight=1717;
        board = new Rectangle(boardWidth, boardHeight);


        Image boardPic = new Image("assets/board/biggerboard.png");
        board.setFill(new ImagePattern(boardPic));
        board.setTranslateX(-300);
        board.setTranslateY(-320);
        board.setTranslateZ(1500);
        parent.getChildren().add(board);

        getClient().getStage().getScene().setOnKeyPressed(e-> {
            KeyCode pressed = e.getCode();
            camState.animateWithKeyCode(camera,pressed);
        });

        SubScene scene = new SubScene(parent, width, len);
        scene.setCamera(camera);



        Point3D temp=new Point3D(300,250,0);
        Shape3D shape = addAndGetShape(parent,parent,ResourceGUI.FAITH,new Point3D(300,250,0));

        for(int i=0;i<20;i++)
            moveFaith(i,shape);

        Button viewCardShop=new Button();
        viewCardShop.setLayoutX(buttonStartingX);
        viewCardShop.setLayoutY(50);
        parent.getChildren().add(viewCardShop);
        viewCardShop.setText("VIEW CARD SHOP");
        viewCardShop.setOnMouseEntered( p ->
        {
            //todo fix static
            if(mode==Mode.MOVING_RES|| mode==Mode.CHOOSE_POS_FOR_CARD)
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
            if(mode==Mode.MOVING_RES|| mode==Mode.CHOOSE_POS_FOR_CARD) {
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
            if(mode==Mode.MOVING_RES|| mode==Mode.CHOOSE_POS_FOR_CARD)
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
            if(mode==Mode.MOVING_RES|| mode==Mode.CHOOSE_POS_FOR_CARD) {
                return;
            }
            GUI.removeLast();
        });







        viewMarket.setId("showButton");
        viewCardShop.setId("showButton");
        boardPane.getChildren().add(viewMarket);
        //boardPane.getChildren().add(parent);
        boardPane.getChildren().add(scene);
        return new SubScene(boardPane,width,len);

    }

    public BoardStateController getController() {
        return controller;
    }


    public void propertyChange(PropertyChangeEvent evt) {

    }


    public void wareBuilder(Group parent, Rectangle board, DragAndDropHandler dropHandler){
        Group wareGroup = new Group();
        Point3D initialPos = new Point3D(100,800,0);

        final double wareWidth = 500;
        final double lineHeight = 150;
        SimpleWarehouseLeadersDepot simpleWarehouseLeadersDepot = getThisPlayerCache().getElem(SimpleWarehouseLeadersDepot.class).orElseThrow();
        SelectablePositions selectablePositions = getThisPlayerCache().getElem(SelectablePositions.class).orElseThrow();


        AtomicInteger gPos = new AtomicInteger();
        int lineN = 0;
        for (Map.Entry<Integer, List<Pair<ResourceAsset, Boolean>>> line: simpleWarehouseLeadersDepot.getDepots().entrySet()){
            int finalLineN = lineN;
            AtomicInteger nInLine = new AtomicInteger();
            line.getValue().forEach(e->{
                double x  = (wareWidth/(1+line.getValue().size())*(1+nInLine.get()));
                Point3D shift = new Point3D(x,lineHeight* finalLineN,0);
                nInLine.getAndIncrement();
                ResourceGUI resourceGUI = ResourceGUI.fromAsset(e.getKey());
                Shape3D testShape = addAndGetShape(wareGroup,wareGroup,resourceGUI,shift);
                DragAndDropData dragAndDropData = new DragAndDropData();
                dragAndDropData.setResourceGUI(resourceGUI);
                dragAndDropData.setShape3D(testShape);
                final int globalPos = gPos.get();
                dragAndDropData.setAvailable(simpleWarehouseLeadersDepot.isPosAvailable(globalPos));
                dragAndDropData.setAvailablePos(simpleWarehouseLeadersDepot.getAvailableMovingPositions().get(globalPos));
                dragAndDropData.setGlobalPos(globalPos);

                boolean isSelectable = true; //toSelect!=null && selectablePositions.getUpdatedSelectablePositions(toSelect.getChosenInputPos()).getOrDefault(globalPos,0)>0;

                testShape.setOnMousePressed((u)->{
                    if (mode.equals(Mode.SELECT_CARD_SHOP) && isSelectable) {
                        toSelect.setNextInputPos(globalPos, e.getKey());
                        ResourceGUI.setColor(resourceGUI,testShape,true,true);
                        testShape.setOnMousePressed(n->{});
                        if (toSelect.getPointedResource().isEmpty()) {
                            CardShopViewBuilder.sendResourcesToBuy(toSelect.getChosenInputPos());
                            toSelect = null;
                        }
                    }
                });

                dragAndDropData.setOnDrop(()->{
                    if (mode.equals(Mode.MOVING_RES)) {
                        dropHandler.stopDragAndDrop();
                        ResourceMarketViewBuilder.sendMove(globalPos, dropHandler.getLastDroppedPos());
                    }
                });

                dropHandler.addShape(dragAndDropData);
                gPos.getAndIncrement();
            });
            lineN++;
        }


        addNodeToParent(parent, board, wareGroup, initialPos);
        warehouse = wareGroup;
    }


    public void resRowBuilder(Group parent, Rectangle board){
        Group resRowGroup = new Group();
        Point3D initialPos = new Point3D(-100,100,0);

        SimpleCardShop simpleCardShop = getSimpleModel().getElem(SimpleCardShop.class).orElseThrow();
        DevelopmentCardAsset card =  simpleCardShop.getPurchasedCard().orElseThrow();
        toSelect = new ResChoiceRow(0,card.getDevelopmentCard().getCosts(),new ArrayList<>());

        //addNodeToParent(parent, board, resRowGroup, initialPos);
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

        Rectangle basic=new Rectangle(100,100);
        basic.setLayoutX(600);
        basic.setLayoutY(300);
        productions.getChildren().add(basic);
        basic.setOnMouseClicked(p->{
            SetupPhase.getBoard().setMode(Mode.BACKGROUND);
            ProductionViewBuilder.sendChosenProduction(0);
        });

        Path path;
        Rectangle rectangle;
        ImagePattern tempImage;
        SimpleCardCells simpleCardCells = getThisPlayerCache().getElem(SimpleCardCells.class).orElseThrow();

        simpleCardCells.getSimpleProductions().getProductionAtPos(0);
        for (Map.Entry<Integer, Optional<DevelopmentCardAsset>> entry : simpleCardCells.getDevCardsCells().entrySet()) //using map.entrySet() for iteration
        {
            if(entry.getValue().isEmpty())
                tempImage = new ImagePattern(new Image("assets/devCards/grayed out/BACK/Masters of Renaissance__Cards_BACK_BLUE_1.png"));
            else
            {
                path=entry.getValue().get().getCardPaths().getKey();
                tempImage = new ImagePattern(new Image((InputStream) path));
            }
            rectangle=new Rectangle(100,100);
            rectangle.setLayoutX(600+250*entry.getKey());
            rectangle.setLayoutY(700);
            rectangle.setOnMouseClicked(p-> {
                if (mode.equals(Mode.CHOOSE_POS_FOR_CARD))
                    CardShopViewBuilder.sendCardPlacementPosition(entry.getKey());
                else if (mode.equals(Mode.CHOOSE_PRODUCTION))
                    ProductionViewBuilder.sendChosenProduction(entry.getKey());
            });
            rectangle.setFill(tempImage);

            productions.getChildren().add(rectangle);
        }
        parent.getChildren().add(productions);
        this.productions = productions;
    }

    @NotNull
    public Shape3D addAndGetShape(Group parent, Node refSystem, ResourceGUI res, Point3D shift) {
        Shape3D stoneMesh = res.generateShape();
        Rotate rotate1 = new Rotate(270   ,new Point3D(1,0,0));
        Rotate rotate2 = new Rotate(270   ,new Point3D(0,1,0));
        stoneMesh.getTransforms().addAll(rotate1,rotate2);
        addNodeToParent(parent,refSystem,stoneMesh,shift);
        return stoneMesh;
    }


    public static void setCamState(CamState camState) {
        BoardView3D.camState = camState;
    }



    public void moveFaithX(boolean foward, Shape3D faithBut){
        if(foward)
            faithBut.setLayoutX(faithBut.getLayoutX()+65);
        else
            faithBut.setLayoutX(faithBut.getLayoutX()-65);
    }

    public void moveFaithY(boolean downward, Shape3D faithBut){
        if(downward)
            faithBut.setLayoutY(faithBut.getLayoutY()+65);
        else
            faithBut.setLayoutY(faithBut.getLayoutY()-65);


    }

    public void moveFaith(int tempFaith, Shape3D faithBut) {


        if (tempFaith <2)
        {
            moveFaithX(true, faithBut);
        } else
        if (tempFaith <4)
        {
            moveFaithY(false, faithBut);
        }
        else if (tempFaith < 9)
        {
            moveFaithX(true, faithBut);
        }
        else if (tempFaith < 11)
        {
            moveFaithY(true, faithBut);
        }
        else if (tempFaith < 16)
        {
            moveFaithX(true, faithBut);
        }
        else if (tempFaith <18)
        {
            moveFaithY(false, faithBut);
        } else
            moveFaithX(true, faithBut);
    }



}


