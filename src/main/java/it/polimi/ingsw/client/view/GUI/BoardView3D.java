package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.PlayerCache;
import it.polimi.ingsw.client.view.GUI.board.Box3D;
import it.polimi.ingsw.client.view.GUI.board.FaithTrack;
import it.polimi.ingsw.client.view.GUI.board.InfoTiles;
import it.polimi.ingsw.client.view.GUI.board.Warehouse3D;
import it.polimi.ingsw.client.view.GUI.layout.ResChoiceRowGUI;
import it.polimi.ingsw.client.view.GUI.util.DragAndDropHandler;
import it.polimi.ingsw.client.view.GUI.util.NodeAdder;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ProductionViewBuilder;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.assets.leaders.NetworkDepositLeaderCard;
import it.polimi.ingsw.network.assets.leaders.NetworkProductionLeaderCard;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.simplemodel.SimpleCardCells;
import it.polimi.ingsw.network.simplemodel.SimpleCardShop;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;
import it.polimi.ingsw.network.simplemodel.SimpleProductions;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.*;

public class BoardView3D {

    public enum Mode{

        MOVING_RES() {
            public void run(BoardView3D board) {
                final DragAndDropHandler ddHandler = new DragAndDropHandler();
                final Rectangle boardRec = board.boardRec;
                final Group parent = board.parent;
                Box3D.discardBuilder(board,parent,boardRec , ddHandler);
                board.getWarehouse().setDropHandler(ddHandler);
                ddHandler.startDragAndDropOnEach(parent,boardRec);
            }
        },
        SELECT_CARD_SHOP(){
            public void run(BoardView3D board) {
                final Rectangle boardRec = board.boardRec;
                final Group parent = board.parent;
                CardShopGUI.addChosenCard();
                board.resRowBuilder(parent,boardRec);
                board.getWarehouse().updateSelected();
                board.getStrongBox().updateStrongBox();
            }
        },
        SELECT_RESOURCE_FOR_PROD(){
            public void run(BoardView3D board) {
                final Rectangle boardRec = board.boardRec;
                final Group parent = board.parent;
                board.resRowBuilder(parent,boardRec);
                board.getWarehouse().updateSelected();
                board.getStrongBox().updateStrongBox();
            }
        },
        CHOOSE_PRODUCTION() {
            public void run(BoardView3D board) {
                final Group parent = board.parent;
                board.productionBuilder(parent);
            }

        },
        CHOOSE_POS_FOR_CARD() {
            public void run(BoardView3D board) {
                final Group parent = board.parent;
                CardShopGUI.addChosenCard();
                board.productionBuilder(parent);
            }
        },
        BACKGROUND(){
            public void run(BoardView3D board) {
                final Group parent = board.parent;
                board.productionBuilder(parent);
            }
        };

        public abstract void run(BoardView3D board);

    }

    public BoardView3D(int playerNumber, PlayerCache cache) {
        this.playerNumber = playerNumber;
        this.cache = cache;
    }

    public Mode mode = Mode.BACKGROUND;
    private int playerNumber;
    private PlayerCache cache;

    double width=1800;
    double len=1000;

    public Rectangle boardRec;
    public Group parent;

    protected Group discardBox;

    protected Warehouse3D warehouse;
    protected Box3D strongBox;


    public FaithTrack faithBoard;

    protected Group faithTrack;

    protected ResChoiceRowGUI toSelect;

    protected Group leadersGroup=new Group();
    protected Group productions;


    public void setFaithTrack(Group faithTrack) {
        this.faithTrack = faithTrack;
    }

    public void setDiscardBox(Group discardBox) {
        this.discardBox = discardBox;
    }

    public void setWarehouse(Warehouse3D warehouse) {
        this.warehouse = warehouse;
    }

    public Warehouse3D getWarehouse() {
        return warehouse;
    }

    public void setStrongBox(Box3D strongBox) {
        this.strongBox = strongBox;
    }

    public void refreshLeaders() {
        if(this.leadersGroup!=null)
            this.leadersGroup.getChildren().clear();
        Group leadersGroup=new Group();
        addLeaders(leadersGroup);
        //addNodeToParent(parent, board, leadersGroup, new Point3D(0,0,0));
        parent.getChildren().add(leadersGroup);
    }

    public PlayerCache getCache() {
        return cache;
    }

    public void addLeaders(Group parent) {
        SimplePlayerLeaders activeLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).orElseThrow();

        Rectangle temp;

        /*     temp=new Rectangle(150,150);
        temp.setTranslateY(500);
        temp.setTranslateX(200);

        addNodeToParent(parent,board,temp,new Point3D(-300,500+150,0));
        */
        List<LeaderCardAsset> activeBonus = activeLeaders.getPlayerLeaders();
        for(int i=0;i<activeBonus.size();i++)
        {

            if(activeBonus.get(i).getNetworkLeaderCard().isLeaderActive())
                if(activeBonus.get(i).getNetworkLeaderCard() instanceof NetworkDepositLeaderCard)
                {
                    temp=new Rectangle(150,100);
                    temp.setTranslateY(500);
                    temp.setTranslateX(200*i);

                    temp.setFill(new ImagePattern(new Image(activeBonus.get(i).getCardPaths().getKey().toString(),false)));
                    NodeAdder.addNodeToParent(parent, boardRec,temp,new Point3D(-300,500+150*i,0));
                }

        }
        leadersGroup.setTranslateY(500);


    }

    public void reset(){
        if (discardBox!=null)
            discardBox.getChildren().clear();

        if (toSelect!=null){
            toSelect.clear();
            toSelect = null;}

        if (productions!=null)
            productions.getChildren().clear();

        if (CardShopGUI.chosenCard!=null)
            CardShopGUI.chosenCard.getChildren().clear();
    }

    public void setMode(Mode mode){
        reset();
        this.mode = mode;
        mode.run(this);
    }

    public ResChoiceRowGUI getToSelect() {
        return toSelect;
    }

    public Box3D getStrongBox() {
        return strongBox;
    }

    public Group getRoot(int clockwiseIndex) {

        parent = new Group();

        final double boardWidth=2402;
        final double boardHeight=1717;
        boardRec = new Rectangle(boardWidth, boardHeight);
        boardRec.setMouseTransparent(true);

        Image boardPic = new Image("assets/board/biggerboard.png");
        boardRec.setFill(new ImagePattern(boardPic));
        boardRec.setTranslateX(-300);
        boardRec.setTranslateY(-320);
        boardRec.setTranslateZ(1500);
        Point3D tbc= Playground.getTableCenter();
        Rotate rotateCam3 = new Rotate((-clockwiseIndex*90), tbc.getX(),tbc.getY(),tbc.getZ(),Rotate.Z_AXIS);
        parent.getTransforms().add(rotateCam3);
        parent.getChildren().add(boardRec);

        faithBoard=new FaithTrack();
        faithBoard.faithTrackBuilder(this,parent, boardRec,playerNumber,cache);
        getSimpleModel().addPropertyChangeListener(faithBoard);


        InfoTiles infoTiles=new InfoTiles();
        infoTiles.infoBuilder(this,parent, boardRec,playerNumber,cache);
        getSimpleModel().addPropertyChangeListener(infoTiles);

        warehouse = new Warehouse3D(cache,this, parent, boardRec);
        warehouse.wareBuilder();
        cache.addPropertyChangeListener(warehouse);

        Box3D strongBox = new Box3D(this);
        strongBox.strongBuilder(parent,boardRec);
        cache.addPropertyChangeListener(strongBox);

        return parent;
    }


    public void resRowBuilder(Group parent, Rectangle board){
        Point3D initialPos = new Point3D(-100,800,0);

        List<ResourceAsset> costs;
        List<ResourceAsset> outputs;
        if (mode.equals(Mode.SELECT_CARD_SHOP)) {
            SimpleCardShop simpleCardShop = getSimpleModel().getElem(SimpleCardShop.class).orElseThrow();
            DevelopmentCardAsset card = simpleCardShop.getPurchasedCard().orElseThrow();

            costs = card.getDevelopmentCard().getCosts();
            outputs = new ArrayList<>();
        }else {
            SimpleCardCells simpleCardCells = cache.getElem(SimpleCardCells.class).orElseThrow();
            SimpleProductions.SimpleProduction lastSelectedProduction = simpleCardCells.getSimpleProductions().lastSelectedProduction().orElseThrow();
            costs = lastSelectedProduction.getUnrolledInputs();
            outputs = lastSelectedProduction.getUnrolledOutputs();
        }
        toSelect = new ResChoiceRowGUI(0,costs,outputs);
        Group resRowGroup = toSelect.getRowGroup();

        resRowGroup.setTranslateX(initialPos.getX());
        resRowGroup.setTranslateX(initialPos.getY());
        resRowGroup.setTranslateX(initialPos.getZ());

        NodeAdder.addNodeToParent(parent, board, resRowGroup, initialPos);
    }


    private void productionBuilder(Group parent) {
        Group productions = new Group();
        Button productionButton = new Button();
        productionButton.setText("Produce");
        productionButton.setLayoutX(500);
        productionButton.setLayoutY(1550);
        productionButton.setTranslateZ(-15);
        productionButton.setPrefHeight(80);
        productionButton.setPrefWidth(200);
        productionButton.setStyle("-fx-font-size:30");

        productionButton.setOpacity(mode.equals(Mode.CHOOSE_PRODUCTION)?1:0);

        productionButton.setOnAction(e -> {
            if (mode.equals(Mode.CHOOSE_PRODUCTION))
                ProductionViewBuilder.sendProduce();
        });

        productions.getChildren().add(productionButton);

        Rectangle basic=new Rectangle(300,300);
        basic.setOpacity(0);
        //basic.setMouseTransparent(true);

        NodeAdder.addNodeToParent(productions,basic,new Point3D(600,1200,0));
        basic.setOnMouseClicked(p->{
            if (mode.equals(Mode.CHOOSE_PRODUCTION)) {
                ProductionViewBuilder.sendChosenProduction(0);
            }
        });

        SimpleCardCells simpleCardCells = cache.getElem(SimpleCardCells.class).orElseThrow();
        Map<Integer,Optional<DevelopmentCardAsset>> frontCards=simpleCardCells.getDevCardsOnTop();

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
                path = simpleCardCells.getDevCardsCells().get(key).get().get(simpleCardCells.getDevCardsCells().get(key).get().size()-1).getCardPaths().getKey();
                tempImage = new ImagePattern(new Image(path.toString(), false));
                System.out.println(path);
            }
            rectangle.setLayoutX(400 + 250 * key);
            rectangle.setLayoutY(0);
            rectangle.setOnMouseClicked(p -> {
                if (mode.equals(Mode.CHOOSE_POS_FOR_CARD) && simpleCardCells.isSpotAvailable(key)) {
                    CardShopViewBuilder.sendCardPlacementPosition(key);
                } else if (mode.equals(Mode.CHOOSE_PRODUCTION))
                    if (value.isPresent())
                        ProductionViewBuilder.sendChosenProduction(key);
            });
            rectangle.setFill(tempImage);





            NodeAdder.addNodeToParent(productions, rectangle, new Point3D(20 + 220 * key, 700, -20));
        }

        SimplePlayerLeaders activeLeaders = cache.getElem(SimplePlayerLeaders.class).orElseThrow();

        List<LeaderCardAsset> activeBonus = activeLeaders.getPlayerLeaders();
        Rectangle temp;
        Path path;
        for (LeaderCardAsset bonus : activeBonus) {
            int count = 0;
            if (bonus.getNetworkLeaderCard().isLeaderActive())
                if (bonus.getNetworkLeaderCard() instanceof NetworkProductionLeaderCard) {
                    temp = new Rectangle(462, 698);
                    temp.setTranslateY(250);
                    temp.setLayoutX(400 + 750 + 250 * (count + 1));

                    temp.setFill(new ImagePattern(new Image(bonus.getCardPaths().getKey().toString(), false)));
                    NodeAdder.addNodeToParent(productions, temp, new Point3D(680 + 220 * (count + 1), 700, -20));

                }


        }
        NodeAdder.addNodeToParent(parent, boardRec, productions, new Point3D(0,0,0));
        this.productions = productions;
    }


}


