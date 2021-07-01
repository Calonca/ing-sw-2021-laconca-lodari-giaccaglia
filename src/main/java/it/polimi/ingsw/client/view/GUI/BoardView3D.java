package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.PlayerCache;
import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.GUI.board.*;
import it.polimi.ingsw.client.view.GUI.layout.ResChoiceRowGUI;
import it.polimi.ingsw.client.view.GUI.util.CardSelector;
import it.polimi.ingsw.client.view.GUI.util.DragAndDropHandler;
import it.polimi.ingsw.client.view.GUI.util.NodeAdder;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.assets.leaders.NetworkDepositLeaderCard;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.SimpleCardCells;
import it.polimi.ingsw.network.simplemodel.SimpleCardShop;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;
import it.polimi.ingsw.network.simplemodel.SimpleProductions;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getCLIView;
import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getSimpleModel;

public class BoardView3D implements PropertyChangeListener {

    public enum Mode{

        /**
         * This mode allows the player to move resourcesì according to the rules, or from the discard box
         */
        MOVING_RES() {
            public void run(BoardView3D board) {
                final DragAndDropHandler ddHandler = new DragAndDropHandler();
                final Rectangle boardRec = board.boardRec;
                final Group parent = board.parent;
                if (!board.getCache().getCurrentState().equals(State.IDLE.toString()))
                    Box3D.discardBuilder(board,parent,boardRec , ddHandler);
                board.getWarehouse().setDropHandler(ddHandler);
                ddHandler.startDragAndDropOnEach(parent,boardRec);
            }
        },
        /**
         * This mode allows the player to choose a selectable card from the Card Shop
         */
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
        /**
         * This mode allows the player to choose valid resources to execute their selected production
         */
        SELECT_RESOURCE_FOR_PROD(){
            public void run(BoardView3D board) {
                final Rectangle boardRec = board.boardRec;
                final Group parent = board.parent;
                board.resRowBuilder(parent,boardRec);
                board.getWarehouse().updateSelected();
                board.getStrongBox().updateStrongBox();
            }
        },
        /**
         * This mode allows the player to select an available production
         */
        CHOOSE_PRODUCTION() {
            public void run(BoardView3D board) {
                board.productions.updateProds();
            }

        },
        /**
         * This mode allows the player to choose a valid SimpleCardCell position for the newly acquired card
         */
        CHOOSE_POS_FOR_CARD() {
            public void run(BoardView3D board) {
                board.productions.updateProds();
                CardShopGUI.addChosenCard();
            }
        },
        BACKGROUND(){
            public void run(BoardView3D board) {
            }
        };

        public abstract void run(BoardView3D board);

    }

    public BoardView3D(int playerNumber, PlayerCache cache) {
        this.playerNumber = playerNumber;
        this.cache = cache;
        cache.addPropertyChangeListener(this);
    }

    public Mode mode = Mode.BACKGROUND;
    private final int playerNumber;
    private int counterClockWiseIdx;
    private final PlayerCache cache;
    private boolean initialized = false;

    public Rectangle boardRec;
    public Group parent;

    protected Group discardBox;

    protected Warehouse3D warehouse;
    protected Box3D strongBox;


    public FaithTrack faithBoard;

    protected Group faithTrack;

    protected ResChoiceRowGUI toSelect;

    protected Group leadersGroup=new Group();
    protected Productions3D productions;


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

    public int getCounterClockWiseIdx() {
        return counterClockWiseIdx;
    }

    /**
     * This method is called to refresh the position's of the deposit leaders
     */
    public void refreshLeaders(SimplePlayerLeaders leaders) {
        if(this.leadersGroup!=null)
            this.leadersGroup.getChildren().clear();
        Group leadersGroup=new Group();
        addLeaders(leadersGroup, leaders);
        parent.getChildren().add(leadersGroup);
    }

    public PlayerCache getCache() {
        return cache;
    }


    /**
     * This method is used to add elements to the deposit leaderGroup
     */
    public void addLeaders(Group leaderGroup, SimplePlayerLeaders activeLeaders) {

        Rectangle temp;

        List<LeaderCardAsset> activeBonus = activeLeaders.getPlayerLeaders();
        int count=0;

        for (LeaderCardAsset bonus : activeBonus) {

            if (bonus.getNetworkLeaderCard().isLeaderActive())
                if (bonus.getNetworkLeaderCard() instanceof NetworkDepositLeaderCard) {
                    temp = new Rectangle(300, 450);
                    temp.setFill(CardSelector.imagePatternFromAsset(bonus.getCardPaths().getKey()));
                    NodeAdder.addNodeToParent(leaderGroup, boardRec, temp, new Point3D(-600 - 300 * count, 725, 0));
                    count++;
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

        if (CardShopGUI.chosenCard!=null)
            CardShopGUI.chosenCard.getChildren().clear();
    }

    public void setMode(Mode mode){
        reset();
        this.mode = mode;
        Platform.runLater(()->mode.run(this));
    }

    public ResChoiceRowGUI getToSelect() {
        return toSelect;
    }

    public Box3D getStrongBox() {
        return strongBox;
    }


    public void setCounterClockWiseIdx(int counterClockWiseIdx) {
        this.counterClockWiseIdx = counterClockWiseIdx;
    }

    /**
     * This method is called upon board initialization
     * @return a group containing the PlayerBoard information
     */
    public synchronized Group getRoot() {
        initialized=true;
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
        Rotate rotateCam3 = new Rotate((-this.counterClockWiseIdx *90), tbc.getX(),tbc.getY(),tbc.getZ(),Rotate.Z_AXIS);
        parent.getTransforms().add(rotateCam3);
        parent.getChildren().add(boardRec);

        faithBoard=new FaithTrack();
        faithBoard.faithTrackBuilder(this,parent, boardRec,playerNumber,cache);
        cache.addPropertyChangeListener(faithBoard);

        InfoTiles infoTiles=new InfoTiles();
        infoTiles.infoBuilder(this,parent, boardRec,playerNumber,cache);
        getSimpleModel().addPropertyChangeListener(infoTiles);

        ActionToken t = new ActionToken();
        t.actionTokenBuilder(this);
        cache.addPropertyChangeListener(t);

        warehouse = new Warehouse3D(cache,this, parent, boardRec);
        warehouse.wareBuilder();
        cache.addPropertyChangeListener(warehouse);

        Box3D strongBox = new Box3D(this);
        strongBox.strongBuilder(parent,boardRec);
        cache.addPropertyChangeListener(strongBox);

        productions = new Productions3D(this);
        productions.updateProds();
        cache.addPropertyChangeListener(productions);

        refreshLeaders(cache.getElem(SimplePlayerLeaders.class).orElseThrow());

        return parent;
    }


    /**
     * This method is used to initialize the resource selection
     * @param parent is the Board 3D main group
     * @param board is the Board 3D rectangle
     */
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //Adds other players boards after setupPhase
        if (!initialized && evt.getPropertyName().equals(State.IDLE.toString())){
            Playground.getPlayground().root.getChildren().add(getRoot());
        } else if (evt.getPropertyName().equals(SimplePlayerLeaders.class.getSimpleName())&&initialized){
            Platform.runLater(()->{
                refreshLeaders((SimplePlayerLeaders) evt.getNewValue());
            });
        }
    }

}


