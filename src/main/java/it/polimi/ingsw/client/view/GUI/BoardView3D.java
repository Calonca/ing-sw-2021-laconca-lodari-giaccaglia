package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.GUI.board.CamState;
import it.polimi.ingsw.client.view.GUI.board.Text3d;
import it.polimi.ingsw.client.view.GUI.util.BoardStateController;
import it.polimi.ingsw.client.view.GUI.util.DragAndDropData;
import it.polimi.ingsw.client.view.GUI.util.DragAndDropHandler;
import it.polimi.ingsw.client.view.GUI.util.ResourceGUI;
import it.polimi.ingsw.client.view.abstractview.ProductionViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.SimpleCardCells;
import it.polimi.ingsw.network.simplemodel.SimpleDiscardBox;
import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;
import it.polimi.ingsw.network.simplemodel.SimpleWarehouseLeadersDepot;
import it.polimi.ingsw.server.model.Resource;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getClient;
import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getThisPlayerCache;

public class BoardView3D {

    public enum Mode{

        MOVING_RES() {
            public void run() {
                final DragAndDropHandler ddHandler = new DragAndDropHandler();
                final Rectangle board = SetupPhase.getBoard().board;
                final Group parent = SetupPhase.getBoard().parent;
                SetupPhase.getBoard().discardBuilder(parent,board , ddHandler);
                SetupPhase.getBoard().wareBuilder(parent,board,ddHandler);
                ddHandler.startDragAndDropOnEach(parent,board);
            }
        },
        SELECT_CARD_SHOP(){
            public void run() {

                DiscardBoxInitializer discardBoxInitializer=new DiscardBoxInitializer();
                SetupPhase.getBoard().addNodeToParent(SetupPhase.getBoard().parent,discardBoxInitializer.fillDispenser(),new Point3D(300,200,0));
                SetupPhase.getBoard().addNodeToParent(SetupPhase.getBoard().parent,SetupPhase.getBoard().getController().getBoughtCard(),new Point3D(300,150,0));

            }
        },
        CHOOSE_PRODUCTION() {
            public void run() {

                final Group parent = SetupPhase.getBoard().parent;
                Rectangle basic=new Rectangle(100,100);
                basic.setLayoutX(600);
                basic.setLayoutY(300);
                parent.getChildren().add(basic);
                basic.setOnMouseClicked(p->{
                    SetupPhase.getBoard().setMode(Mode.BACKGROUND);
                    ProductionViewBuilder.sendChosenProduction(0);
                });


                Path path;
                Rectangle rectangle;
                ImagePattern tempImage;
                SimpleCardCells simpleCardCells = getThisPlayerCache().getElem(SimpleCardCells.class).orElseThrow();;
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
                    rectangle.setOnMouseClicked(p->ProductionViewBuilder.sendChosenProduction(entry.getKey()));
                    parent.getChildren().add(rectangle);
                    rectangle.setFill(tempImage);

                }
            }
        },
        CHOOSE_POS_FOR_CARD() {
            public void run() {

            }
        },
        BACKGROUND(){
            public void run() {
                final DragAndDropHandler ddHandler = new DragAndDropHandler();
                final Rectangle board = SetupPhase.getBoard().board;
                final Group parent = SetupPhase.getBoard().parent;
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

    private static CamState camState = CamState.TOP;
    public static final boolean moveFreely = true;


    public void runforStart() {
        Node root=getRoot();
        //todo fix initialization
        root.setId("3DVIEW");

        GUI.getRealPane().getChildren().add(root);

        System.out.println(GUI.getRealPane().getChildren());

    }

    public void reset(){
        if (discardBox!=null)
            discardBox.getChildren().removeIf(n->true);
        if (warehouse!=null)
            warehouse.getChildren().removeIf(n->true);
    }

    public void setMode(Mode mode){
        reset();
        this.mode = mode;
        mode.run();
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

    public void discardBuilder(Group parent, Rectangle board,DragAndDropHandler dropHandler){
        Group discardBoxGroup = new Group();



        //Button discard=new Button();
        //discard.setLayoutX(buttonStartingX);
        //discard.setLayoutY(150);
        //discard.setText("DRiscard fadsdsdswed          er ee e e e ee ");
        //discard.setOnAction(e->{
        //    ResourceMarketViewBuilder.sendDiscard();
        //});
        //discard.setId("discard");
        //discardBoxGroup.getChildren().add(discard);
        //Box background = new Box(350,900,2);
        //Translate t = new Translate();
        //t.setX(20);
        //t.setY(200);
        //t.setZ(0);
        //background.getTransforms().add(t);
        //background.setMouseTransparent(true);
        //discardBoxGroup.getChildren().add(background);
        Point3D initialPos = new Point3D(800,800,0);
        final double lineHeight = 150;
        SimpleDiscardBox simpleDiscardBox =  getThisPlayerCache().getElem(SimpleDiscardBox.class).orElseThrow();
        int addedLines = 0;
                        //Pos       //Res           //NOfRes
        final Set<Map.Entry<Integer, Pair<ResourceAsset, Integer>>> entries = simpleDiscardBox.getResourceMap()
                .entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toCollection(LinkedHashSet::new));
        for (Map.Entry<Integer, Pair<ResourceAsset, Integer>> line : entries) {
            Group lineGroup = new Group();
            Text text = Text3d.from(line.getValue().getValue());
            text.setLayoutX(0);
            text.setTranslateZ(-100);
            lineGroup.getChildren().add(text);
            lineGroup.setTranslateY(lineHeight * addedLines);
            int gPos = line.getKey();

            ResourceGUI resTest = ResourceGUI.fromAsset(line.getValue().getKey());
            Shape3D shape = addAndGetShape(discardBoxGroup,discardBoxGroup,resTest,new Point3D(0,lineHeight * addedLines,0));
            discardBoxGroup.getChildren().add(lineGroup);
            DragAndDropData dragAndDropData = new DragAndDropData();
            dragAndDropData.setResourceGUI(resTest);
            dragAndDropData.setShape3D(shape);
            dragAndDropData.setAvailable(simpleDiscardBox.isPosAvailable(gPos));
            dragAndDropData.setAvailablePos(simpleDiscardBox.getAvailableMovingPositions().get(gPos));
            dragAndDropData.setGlobalPos(gPos!=0?gPos:null);
            if (mode.equals(Mode.MOVING_RES))
                dragAndDropData.setOnDrop(()->{
                    ResourceMarketViewBuilder.sendMove(gPos,dropHandler.getLastDroppedPos());
                });
            dropHandler.addShape(dragAndDropData);

            addedLines++;
        }
        Text discardText = Text3d.from("Discard");
        discardText.setLayoutY(lineHeight*addedLines);
        discardText.setMouseTransparent(true);
        Rectangle r = new Rectangle(200,50,Color.BLUE);
        r.setTranslateZ(-200);
        r.setLayoutY((lineHeight*addedLines)-90);
        r.setOnMouseClicked(e-> {
            if (simpleDiscardBox.isDiscardable()) {
                setMode(Mode.BACKGROUND);
                ResourceMarketViewBuilder.sendDiscard();
            }
        });
        r.setOnMouseEntered(e-> {
            if (simpleDiscardBox.isDiscardable()) {
                r.setFill(Color.CYAN);
            }
        });
        r.setOnMouseExited(e-> {
            if (simpleDiscardBox.isDiscardable()) {
                r.setFill(Color.BLUE);
            }
        });
        discardBoxGroup.getChildren().add(r);
        discardBoxGroup.getChildren().add(discardText);

        addNodeToParent(parent, board, discardBoxGroup, initialPos);
        discardBox = discardBoxGroup;
    }

    public void wareBuilder(Group parent, Rectangle board, DragAndDropHandler dropHandler){
        Group wareGroup = new Group();
        Point3D initialPos = new Point3D(100,800,0);

        final double wareWidth = 500;
        final double lineHeight = 150;
        SimpleWarehouseLeadersDepot simpleWarehouseLeadersDepot = getThisPlayerCache().getElem(SimpleWarehouseLeadersDepot.class).orElseThrow();

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

                dragAndDropData.setOnDrop(()->{
                    if (mode.equals(Mode.MOVING_RES))
                    ResourceMarketViewBuilder.sendMove(globalPos,dropHandler.getLastDroppedPos());
                });
                dropHandler.addShape(dragAndDropData);
                gPos.getAndIncrement();
            });
            lineN++;
        }


        addNodeToParent(parent, board, wareGroup, initialPos);
        warehouse = wareGroup;
    }


    private void addNodeToParent(Group parent, Node board, Node shape, Point3D shift){
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

    @NotNull
    private Shape3D addAndGetShape(Group parent, Node refSystem, ResourceGUI res, Point3D shift) {
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


