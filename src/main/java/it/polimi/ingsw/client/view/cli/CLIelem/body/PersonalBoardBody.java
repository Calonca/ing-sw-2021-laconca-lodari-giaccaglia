package it.polimi.ingsw.client.view.cli.CLIelem.body;

import it.polimi.ingsw.client.simplemodel.PlayerCache;
import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.cli.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.cli.endgame.WinLooseCLI;
import it.polimi.ingsw.client.view.cli.idle.IDLEPlayersInfoCLI;
import it.polimi.ingsw.client.view.cli.idle.IDLEViewBuilderCLI;
import it.polimi.ingsw.client.view.cli.layout.GridElem;
import it.polimi.ingsw.client.view.cli.layout.Option;
import it.polimi.ingsw.client.view.cli.layout.ResChoiceRowCLI;
import it.polimi.ingsw.client.view.cli.layout.SizedBox;
import it.polimi.ingsw.client.view.cli.layout.drawables.*;
import it.polimi.ingsw.client.view.cli.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.cli.layout.recursivelist.RecursiveList;
import it.polimi.ingsw.client.view.cli.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.cli.middle.MiddlePhaseCLI;
import it.polimi.ingsw.client.view.cli.middle.MiddlePlayersInfoCLI;
import it.polimi.ingsw.client.view.cli.textutil.Background;
import it.polimi.ingsw.client.view.cli.textutil.Color;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ProductionViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.*;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.*;
import static it.polimi.ingsw.network.simplemodel.SimpleStrongBox.numAndSel;

public class PersonalBoardBody extends CLIelem {

    private Column getRoot() {
        return root;
    }

    private void setRoot(Column root) {
        this.root = root;
    }

    private RecursiveList getTop() {
        return top;
    }

    private Column getDiscardBox() {
        return discardBox;
    }

    private Row getStrongBox() {
        return strongBox;
    }

    private Column getWareHouseLeadersDepot() {
        return wareHouseLeadersDepot;
    }

    private void setWareHouseLeadersDepot(Column wareHouseLeadersDepot) {
        this.wareHouseLeadersDepot = wareHouseLeadersDepot;
    }

    private Row getProductions() {
        return productions;
    }

    private PlayerCache getCache() {
        return cache;
    }

    private String getMessage() {
        return message;
    }

    private Mode getMode() {
        return mode;
    }

    private Integer getLastSelectedPosition() {
        return lastSelectedPosition;
    }

    private void setLastSelectedPosition(Integer lastSelectedPosition) {
        this.lastSelectedPosition = lastSelectedPosition;
    }

    private Map<Integer, List<Integer>> getMovPos() {
        return movPos;
    }

    private void setMovPos(Map<Integer, List<Integer>> movPos) {
        this.movPos = movPos;
    }

    private ResChoiceRowCLI getResChoiceRow() {
        return resChoiceRow;
    }

    private Map<ViewMode, Runnable> getViewModeBuilders() {
        return viewModeBuilders;
    }

    private ViewBuilder getRequestBuilder() {
        return requestBuilder;
    }

    private void setRequestBuilder(ViewBuilder requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    public enum Mode{

        /**
         * In this mode the user is allowed to move resources between DiscardBox and various deposit rows
         */
        MOVING_RES(){
            @Override
            public String getString(PersonalBoardBody board) {

                board.setRoot(new Column());

                board.getRoot().setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);
                board.getRoot().addElem(board.getTop());

                Row depotsAndProds = board.getRoot().addAndGetRow();

                Column depots = depotsAndProds.addAndGetColumn();
                depots.addElemNoIndexChange(board.getWareHouseLeadersDepot());

                depots.addElem(new SizedBox(0,2));
                depots.addElem(board.getStrongBox());

                State currentState = State.valueOf(Objects.requireNonNull(getThisPlayerCache()).getCurrentState());

                if(!(currentState.equals(State.IDLE) ||currentState.equals(State.MIDDLE_PHASE)))
                    depotsAndProds.addElem(board.getDiscardBox());

                depotsAndProds.addElem(board.getProductions());

                if(currentState.equals(State.IDLE))
                    depotsAndProds.selectInEnabledOption(cli, board.getMessage() , () -> getClient().changeViewBuilder(new IDLEViewBuilderCLI()));

                else if(currentState.equals(State.MIDDLE_PHASE))
                    depotsAndProds.selectInEnabledOption(cli, board.getMessage() , () -> getClient().changeViewBuilder(new MiddlePhaseCLI()));

                else
                    depotsAndProds.selectInEnabledOption(cli, board.getMessage());

                return  CanvasBody.fromGrid(board.getRoot()).toString();
            }
        },

        /**
         * In this mode the used is allowed to make a selection on the Card Shop
         */
        SELECT_CARD_SHOP(){
            @Override
            public String getString(PersonalBoardBody board) {

                board.setRoot(new Column());

                board.getTop().updateElem(2, board.getResChoiceRow().getGridElem());
                board.getRoot().addElem(board.getTop());

                Row depotsAndProds = board.getRoot().addAndGetRow();
                depotsAndProds.setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);

                Column depots = depotsAndProds.addAndGetColumn();
                depots.addElemNoIndexChange(board.getWareHouseLeadersDepot());

                depots.addElem(new SizedBox(0,2));
                depots.addElem(board.getStrongBox());

                depotsAndProds.addElem(board.getProductions());

                depotsAndProds.selectInEnabledOption(cli, board.getMessage());
                return  CanvasBody.fromGrid(board.getRoot()).toString();
            }
        },

        /**
         * In this mode the user is allowed to select each resource position for a DevelopmentCard to be placed
         */
        SELECT_RES_FOR_PROD(){
            @Override
            public String getString(PersonalBoardBody board) {

                board.setRoot(new Column());

                board.getRoot().addElem(board.getResChoiceRow().getGridElem());

                Row depotsAndProds = board.getRoot().addAndGetRow();
                depotsAndProds.setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);

                Column depots = depotsAndProds.addAndGetColumn();
                depots.addElemNoIndexChange(board.getWareHouseLeadersDepot());

                depots.addElem(new SizedBox(0,2));
                depots.addElem(board.getStrongBox());

                depotsAndProds.addElem(board.getProductions());

                board.getRoot().selectInEnabledOption(cli, board.getMessage());
                return  CanvasBody.fromGrid(board.getRoot()).toString();
            }
        },

        /**
         * In this mode the user is allowed to choose one of the three SimpleCardCells
         */
        CHOOSE_POS_FOR_CARD(){
            @Override
            public String getString(PersonalBoardBody board) {

                board.setRoot(new Column());
                board.getRoot().setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);
                board.getRoot().addElem(board.getTop());

                Row depotsAndProds = board.getRoot().addAndGetRow();

                Column depots = depotsAndProds.addAndGetColumn();
                depots.addElemNoIndexChange(board.getWareHouseLeadersDepot());

                depots.addElem(new SizedBox(0,2));
                depots.addElem(board.getStrongBox());

                depotsAndProds.addElem(board.getProductions());
                board.getProductions().selectInEnabledOption(cli, board.getMessage());
                return  CanvasBody.fromGrid(board.getRoot()).toString();
            }
        },

        /**
         * This is View mode for both IDLE and MIDDLE_PHASE
         */
        VIEWING() {

            @Override
            public String getString (PersonalBoardBody board){
                board.setRoot(new Column());

                board.getRoot().setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);
                board.getRoot().addElem(board.getTop());

                Row depotsAndProds = board.getRoot().addAndGetRow();

                Column depots = depotsAndProds.addAndGetColumn();
                depots.addElemNoIndexChange(board.getWareHouseLeadersDepot());

                depots.addElem(new SizedBox(0,2));
                depots.addElem(board.getStrongBox());

                depotsAndProds.addElem(board.getProductions());

                cli.enableViewMode();

                cli.runOnInput(board.getMessage() , () -> {
                    cli.disableViewMode();
                    getClient().changeViewBuilder(board.getRequestBuilder());
                });

                return  CanvasBody.fromGrid(board.getRoot()).toString();

            }
        },


        /**
         * In this mode the user is allowed to select an available production to make
         */
        CHOOSE_PRODUCTION(){
            @Override
            public String getString(PersonalBoardBody board) {

                board.setRoot(new Column());
                board.getRoot().setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);
                board.getRoot().addElem(board.getTop());

                Row depotsAndProds = board.getRoot().addAndGetRow();

                Column depots = depotsAndProds.addAndGetColumn();
                depots.addElemNoIndexChange(board.getWareHouseLeadersDepot());

                depots.addElem(new SizedBox(0,2));
                depots.addElem(board.getStrongBox());

                depotsAndProds.addElem(board.getProductions());
                board.getProductions().selectInEnabledOption(cli, board.getMessage() , ProductionViewBuilder::sendProduce);
                return  CanvasBody.fromGrid(board.getRoot()).toString();
            }
        };


        public abstract String getString(PersonalBoardBody board);

    }

    private Column root = new Column();

    private RecursiveList top;
    private Column discardBox;
    private Row strongBox;
    private Column wareHouseLeadersDepot;
    private Row productions;

    private final PlayerCache cache;
    private String message;
    private final Mode mode;

    private Integer lastSelectedPosition;
    private Map<Integer, List<Integer>> movPos;
    private ResChoiceRowCLI resChoiceRow;
    private final Map<ViewMode, Runnable> viewModeBuilders;
    private ViewBuilder requestBuilder;

    public enum ViewMode{IDLE, MIDDLE, PLAYERS_INFO_IDLE, PLAYERS_INFO_MIDDLE, PLAYERS_INFO_END}

    public PersonalBoardBody(PlayerCache cache, Mode mode) {

        this.cache = cache;
        this.mode= mode;

        viewModeBuilders = new EnumMap<>(ViewMode.class);
        getViewModeBuilders().put(ViewMode.IDLE, () -> setRequestBuilder(new IDLEViewBuilderCLI()));
        getViewModeBuilders().put(ViewMode.MIDDLE, () -> setRequestBuilder(new MiddlePhaseCLI()));
        getViewModeBuilders().put(ViewMode.PLAYERS_INFO_IDLE, () -> setRequestBuilder(new IDLEPlayersInfoCLI()));
        getViewModeBuilders().put(ViewMode.PLAYERS_INFO_MIDDLE, () -> setRequestBuilder(new MiddlePlayersInfoCLI()));
        getViewModeBuilders().put(ViewMode.PLAYERS_INFO_END, () -> setRequestBuilder(new WinLooseCLI()));

    }

    public void setRightBuilderForViewMode(ViewMode name){
       getViewModeBuilders().get(name).run();
    }

    public void setTop(RecursiveList top) {
        this.top = top;
    }

    public void setResChoiceRow(ResChoiceRowCLI resChoiceRow) {
        this.resChoiceRow = resChoiceRow;
    }

    public void setDiscardBox(Column discardBox) {
        this.discardBox = discardBox;
    }

    public void setStrongBox(Row strongBox) {
        this.strongBox = strongBox;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setProductions(Row productions) {
        this.productions = productions;
    }

    /**
     * This method is called during moving phase
     * @param board is the player's board
     * @param pos is the starting position
     * @return true if the selected position is available
     */
    private boolean getSelectableMove(PersonalBoardBody board, int pos){
        boolean selectable = false;
        if (board.getMode().equals(Mode.MOVING_RES)){
            if (board.getLastSelectedPosition() ==null)
                selectable = !getMovPos().get(pos).isEmpty();
            else {
                List<Integer> moveEndPos = getMovPos().get(getLastSelectedPosition());
                selectable = moveEndPos.contains(pos);
            }
        }
        return selectable;
    }

    /**
     * @param simpleDiscardBox is a SimpleModel element
     * @param board is the player's board
     * @return a column containing the DiscardBox informations
     */
    private Column discardBoxBuilder(SimpleDiscardBox simpleDiscardBox, PersonalBoardBody board){
        Map<Integer, Pair<ResourceAsset, Integer>> discardBoxMap = simpleDiscardBox.getResourceMap();
        Optional<Integer> faithPointsPos = discardBoxMap.entrySet().stream().filter(e->e.getValue().getKey().equals(ResourceAsset.FAITH)).map(Map.Entry::getKey).findFirst();
        int faithPoints = faithPointsPos.map(p->discardBoxMap.get(faithPointsPos.get()).getValue()).orElse(0);
        faithPointsPos.ifPresent(discardBoxMap::remove);

        Stream<Option> optionList = discardBoxMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .map(e->optionFromAsset(e.getValue().getKey(),e.getValue().getValue(),0, getSelectableMove(board,e.getKey()),e.getKey(), false, null));
        Column discardedBoxList = new Column(optionList);
        Option faith = optionFromAsset(ResourceAsset.FAITH,faithPoints,0,false,-100, false, null);
        faith.setMode(Option.VisMode.NO_NUMBER);
        discardedBoxList.addElem(faith);
        Option discard = Option.from("Discard", ResourceMarketViewBuilder::sendDiscard);
        discard.setEnabled(simpleDiscardBox.isDiscardable());
        discardedBoxList.addElem(discard);
        return discardedBoxList;

    }

    /**
     * @param simpleCardCells is a SimpleModel element
     * @return a column containing the SimpleCardCells informations
     */
    public Row productionsBuilder(SimpleCardCells simpleCardCells){

        Option basicProdOption = buildBasicProduction(simpleCardCells);

        Stream<Option> leaderProdsList = simpleCardCells.getActiveProductionLeaders().size()>0 ? buildLeaderProductionsList(simpleCardCells) : Stream.empty();
        Stream<Option> normalProdsList = buildNormalProductionsList(simpleCardCells);

        Column basicAndLeaderColumn = new Column(Stream.concat(Stream.ofNullable(basicProdOption),leaderProdsList));

        return new Row(Stream.concat(Stream.of(basicAndLeaderColumn) , normalProdsList));

    }

    private Stream<Option> buildNormalProductionsList(SimpleCardCells simpleCardCells){

        Map<Integer,Optional<NetworkDevelopmentCard>> frontCards=simpleCardCells
                .getDevCardsOnTop()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e->e.getValue()
                        .map(DevelopmentCardAsset::getDevelopmentCard)));

        return frontCards
                .entrySet()
                .stream()
                .filter(e -> e.getKey()<4)
                .map(e-> {
                    int prodPos = e.getKey();
                     Option cell = prodOption(getProdRunnable(prodPos), e.getValue().orElse(null), simpleCardCells.getCellHeight(e.getKey()).orElse(0));

                     setProdAvailable(simpleCardCells, prodPos, cell);
                     cell.setSelected(simpleCardCells.getSimpleProductions()
                             .getProductionAtPos(e.getKey())
                             .map(SimpleProductions.SimpleProduction::isSelected)
                             .orElse(false));
            return cell;
        });
    }

    private Option buildBasicProduction(SimpleCardCells simpleCardCells){

        return simpleCardCells
                .getSimpleProductions()
                .getProductionAtPos(0)
                .map(p->{

                    Drawable basicProdDrawable = new Drawable();
                    basicProdDrawable.add(0,"╔═  Basic  Production  ══╗");
                    basicProdDrawable.add(Drawable.copyShifted(0,1,DrawableProduction.fromInputAndOutput(p.getInputResources(),p.getOutputResources())));

                    basicProdDrawable.add(0,"╚════════════════════════╝");
                    Option o = Option.from(basicProdDrawable,getProdRunnable(0));
                    o.setMode(Option.VisMode.NUMBER_TO_BOTTOM);
                    o.setSelected(p.isSelected());
                    setProdAvailable(simpleCardCells, 0, o);
                    return o; }).orElse(null);

    }

    private Stream<Option> buildLeaderProductionsList(SimpleCardCells simpleCardCells){

        SimpleProductions simpleProductions = simpleCardCells.getSimpleProductions();

        int indexOfLastProduction = simpleProductions.getIndexOfLastProduction();

        return IntStream.rangeClosed(4, indexOfLastProduction).boxed().filter(index -> simpleProductions.getProductionAtPos(index).isPresent())
                .map(index -> {

                    SimpleProductions.SimpleProduction production = simpleProductions.getProductionAtPos(index).get();

                    Drawable leaderProdDrawable = new Drawable();

                    leaderProdDrawable.add(0,"╔═  Leader Production  ══╗");

                    leaderProdDrawable.add(
                            Drawable.copyShifted(0,1,DrawableProduction.fromInputAndOutput(production.getInputResources(),production.getOutputResources())));

                    leaderProdDrawable.add(0,"╚════════════════════════╝");

                    Option o = Option.from(leaderProdDrawable,getProdRunnable(index));
                    o.setMode(Option.VisMode.NUMBER_TO_BOTTOM);
                    o.setSelected(production.isSelected());
                    setProdAvailable(simpleCardCells, index, o);
                    return o;
                });
    }


    /**
     * This method is called for player's interaction with DevelopmentCard
     * @param prodPos is a valid SimpleCardCells position
     * @return the corresponding Runnable
     */
    private Runnable getProdRunnable(int prodPos) {
        return () -> {
            if (getMode().equals(Mode.CHOOSE_POS_FOR_CARD)) {
                CardShopViewBuilder.sendCardPlacementPosition(prodPos);
            } else if (getMode().equals(Mode.CHOOSE_PRODUCTION)) {
                ProductionViewBuilder.sendChosenProduction(prodPos);
            }
        };
    }


    /**
     * @param simpleCardCells is a SimpleModel element
     * @param prodPos is a valid SimpleCardCells position
     * @param cell is an option to use
     */
    private void setProdAvailable(SimpleCardCells simpleCardCells, int prodPos, Option cell) {
        if (getMode().equals(Mode.CHOOSE_POS_FOR_CARD))
            cell.setEnabled(simpleCardCells.isSpotAvailable(prodPos));
        else if (getMode().equals(Mode.CHOOSE_PRODUCTION))
            cell.setEnabled(simpleCardCells.getSimpleProductions().isProductionAtPositionAvailable(prodPos).orElse(false));
        else cell.setMode(Option.VisMode.NO_NUMBER);
    }


    /**
     * @param simpleStrongBox is a SimpleModel element
     * @param board is the player's board
     * @return a row containing the SimpleStronBox informations
     */
    public static Row strongBoxBuilder(SimpleStrongBox simpleStrongBox, PersonalBoardBody board){

        SelectablePositions selectablePositions = board.getCache().getElem(SelectablePositions.class).orElseThrow();
        Map<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>> strongBoxMap = simpleStrongBox.getResourceMap();
        Map<Integer,Integer> selectablePositionsMap = new HashMap<>();
        if(Objects.nonNull(board.getResChoiceRow()))
            selectablePositionsMap = selectablePositions.getUpdatedSelectablePositions(board.getResChoiceRow().getChosenInputPos());

        Map<Integer, Integer> finalSelectablePositionsMap = selectablePositionsMap;

        Stream<Option> optionList = strongBoxMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .map(e-> {
                    boolean selectable;
                    if (board.getMode().equals(Mode.SELECT_CARD_SHOP))
                        selectable = finalSelectablePositionsMap.containsKey(e.getKey()) && finalSelectablePositionsMap.get(e.getKey()) > 0 &&
                                board.getResChoiceRow().getPointedResource().isPresent() &&
                                board.getResChoiceRow().getPointedResource().get().equals(e.getValue().getKey());
                    else if (board.getMode().equals(Mode.SELECT_RES_FOR_PROD))
                        selectable = board.getSelectableInProd(e.getKey());
                    else selectable = false;
                    Option strongOption = board.optionFromAsset(
                            e.getValue().getKey(),
                            numAndSel(e).getKey(),
                            numAndSel(e).getValue()+(board.getResChoiceRow() !=null?
                                    (int) board.getResChoiceRow().getChosenInputPos().stream().filter(p->p.equals(e.getKey())).count():
                                    0)
                            ,selectable,
                            e.getKey(),
                            false,
                            null);
                    if (board.getMode().equals(Mode.MOVING_RES))
                        strongOption.setEnabled(false);
                    return strongOption;
                });
        return new Row(optionList);

    }


    /**
     * @param asset is a valid resource asset
     * @param numOf
     * @param selected is a selected position
     * @param selectable is the selection condition
     * @param globalPos is the resource global position
     * @param isLeaderDepot indicates if it's in a leader's depot
     * @param leaderAsset represent the corresponding leader's asset
     * @return
     */
    private Option optionFromAsset(ResourceAsset asset, int numOf, int selected, boolean selectable, int globalPos, boolean isLeaderDepot, ResourceAsset leaderAsset){
        Drawable dw = new Drawable();
        ResourceCLI resourceCLI = ResourceCLI.fromAsset(asset);

        if(isLeaderDepot)
            dw.add(0, (numOf == 1 ? "" : numOf + " ") + resourceCLI.getFullName() + " ", Color.WHITE, ResourceCLI.fromAsset(leaderAsset).getB());
        else
            dw.add(0, (numOf ==1?"": numOf +" ")+resourceCLI.getFullName()+" ",resourceCLI.getC(), Background.DEFAULT);


        dw.add(0, (selected==0 ? "         ":selected+" selected"),resourceCLI.getC(), Background.DEFAULT);
        Runnable r=()->{
            if (getMode().equals(Mode.MOVING_RES)) {
                if (getLastSelectedPosition() == null) {
                    setLastSelectedPosition(globalPos);
                    initializeMove();
                    if(Objects.requireNonNull(getThisPlayerCache()).getCurrentState().equals(State.CHOOSING_POSITION_FOR_RESOURCES.toString()))
                        setMessage("Select end position or discard resources");
                    else
                        setMessage("Select end position or press ENTER to go back");
                    cli.show();
                } else {
                    int startPosition = getLastSelectedPosition();
                    cli.show();//Animation
                    setLastSelectedPosition(null);
                    ResourceMarketViewBuilder.sendMove(startPosition, globalPos);
                }
            }else if (getMode().equals(Mode.SELECT_CARD_SHOP)){
                getResChoiceRow().setNextInputPos(globalPos,asset);
                initializeBuyOrChoosePos();
                setMessage("Select resources to buy the card");
                cli.show();
                if (getResChoiceRow().getPointedResource().isEmpty()){
                    CardShopViewBuilder.sendResourcesToBuy(getResChoiceRow().getChosenInputPos());
                }
            } else if (getMode().equals(Mode.SELECT_RES_FOR_PROD)){
                getSelectedForProdRunnable(asset, globalPos).run();
            }
        };
        Option o = Option.from(dw, r);
        o.setEnabled(selectable);
        if (getMode().equals(Mode.MOVING_RES)) {
            o.setSelected(getLastSelectedPosition() != null && getLastSelectedPosition() == globalPos);
        }else if (getMode().equals(Mode.SELECT_CARD_SHOP)) {
            o.setSelected(getResChoiceRow().getChosenInputPos().contains(globalPos));
        }
        return o;
    }


    /**
     * This method uses resChoiceRow to transform TO CHOOSE in concrete resourcee
     * @param asset is a valid resource asset
     * @param globalPos is the resource global position
     * @return the corresponding runnable
     */
    private Runnable getSelectedForProdRunnable(ResourceAsset asset, int globalPos) {
        Runnable r;
        r= ()->{
            if (getResChoiceRow().choosingInput()) {
                getResChoiceRow().setNextInputPos(globalPos, asset);
                setMessage("Select resources for production");
                if (!getResChoiceRow().choosingInput()) {
                    getResChoiceRow().setOnChosenOutput(getSelectedForProdRunnable(ResourceAsset.fromInt(cli.getLastInt()),0));
                    setMessage("Select output resources 1");
                }
            } else {
                getResChoiceRow().setNextInputPos(0,ResourceAsset.fromInt(cli.getLastInt()));
                getResChoiceRow().setOnChosenOutput(getSelectedForProdRunnable(ResourceAsset.fromInt(cli.getLastInt()),0));
                setMessage("Select output resources");
            }

            initializeBuyOrChoosePos();
            cli.show();
            if (getResChoiceRow().getPointedResource().isEmpty())
                ProductionViewBuilder.sendChosenResources(getResChoiceRow().getChosenInputPos(), getResChoiceRow().getChosenOutputRes());
        };
        return r;
    }

    /**
     * Method called upon entering move mode
     */
    public void initializeMove() {
        SimpleDiscardBox sd = getCache().getElem(SimpleDiscardBox.class).orElseThrow();
        SimpleWarehouseLeadersDepot sw = getCache().getElem(SimpleWarehouseLeadersDepot.class).orElseThrow();

        setMovPos(sw.getAvailableMovingPositions());
        sd.getAvailableMovingPositions().forEach((key, value) -> getMovPos().put(key, value));

        setDiscardBox(discardBoxBuilder(sd, this));
        setWareHouseLeadersDepot(wareBuilder(sw,this));
    }

    /**
     * Method called upon selecting the resource or the position of a DevelopmentCard
     */
    public void initializeBuyOrChoosePos() {
        setWareHouseLeadersDepot(wareBuilder(getCache().getElem(SimpleWarehouseLeadersDepot.class).orElseThrow(), this));
        setStrongBox(strongBoxBuilder(getCache().getElem(SimpleStrongBox.class).orElseThrow(), this));
    }

    /**
     * @param simpleWare is a SimpleModel element
     * @param body is a CLI body
     * @return a column containing the SimpleWareHouseLeaderDepot informations
     */
    private static Column wareBuilder(SimpleWarehouseLeadersDepot simpleWare, PersonalBoardBody body){
        Column wareGrid = new Column();
        Map<Integer, List<Pair<ResourceAsset, Boolean>>> resMap = simpleWare.getDepots();
        Map<Integer, ResourceAsset> leaderDepotsMap = simpleWare.getResourcesTypesOfLeaderDepots();
        List<Map.Entry<Integer, List<Pair<ResourceAsset, Boolean>>>> toSort = new ArrayList<>(resMap.entrySet());
        toSort.sort(Map.Entry.comparingByKey());
        List<Row> rows = new ArrayList<>();
        int pos=0;
        for (Map.Entry<Integer, List<Pair<ResourceAsset, Boolean>>> e : toSort) {//Loop in rows
            int rowStart = pos;
            Row row1 = new Row(IntStream.range(0,e.getValue().size())
                    .mapToObj(i -> {
                        boolean selectable;
                        Pair<ResourceAsset,Boolean> pair = e.getValue().get(i);
                        if (body.getMode().equals(Mode.MOVING_RES)) selectable = body.getSelectableMove(body, rowStart + i);
                        else if (body.getMode().equals(Mode.SELECT_CARD_SHOP)) selectable = body.getSelected(pair, rowStart + i);
                        else if (body.getMode().equals(Mode.SELECT_RES_FOR_PROD)) selectable = body.getSelectableInProd(rowStart+i);
                        else selectable = false;

                        boolean isLeaderDepot = leaderDepotsMap.containsKey(rowStart + i);
                        if(isLeaderDepot)
                            return body.optionFromAsset(pair.getKey(), 1, 0, selectable , rowStart + i, true, leaderDepotsMap.get(rowStart + i));
                        else
                            return body.optionFromAsset(pair.getKey(), 1, 0, selectable , rowStart + i, false, null);
                    }));
            rows.add(row1);
            pos+=e.getValue().size();
        }

        for (Row row : rows) {
            wareGrid.addElem(row);
        }
        return wareGrid;
    }

    /**
     * @param gPos is a production global position
     * @return
     */
    private boolean getSelectableInProd(Integer gPos){
        return getCache().getElem(SelectablePositions.class).orElseThrow()
                .getUpdatedSelectablePositions(getResChoiceRow().getChosenInputPos()).containsKey(gPos);
    }

    /**
     *
     * @param pair
     * @param globalPos
     * @return
     */
    private boolean getSelected(Pair<ResourceAsset, Boolean> pair, int globalPos) {
        return getResChoiceRow().getPointedResource().isPresent()
                && pair.getKey().equals(getResChoiceRow().getPointedResource().get())
                && !getResChoiceRow().getChosenInputPos().contains(globalPos)
                && pair.getValue().equals(false);
    }

    /**
     *
     * @param r runnable associated with the option
     * @param card is a valid DevelopmentCard
     * @param stackHeight is the number of developmentCards in the stack
     * @return the corresponding option
     */
    public static Option prodOption(Runnable r, NetworkDevelopmentCard card,int stackHeight){
        Drawable d = DrawableDevCard.fromDevCardAsset(card,stackHeight);
        Option o = Option.from(d,r);
        o.setMode(Option.VisMode.NUMBER_TO_BOTTOM);
        return o;
    }

    public void initializeFaithTrack() {
        Column topColumn = new Column();
        topColumn.addElem(new FaithTrackGridElem(getCache().getElem(SimpleFaithTrack.class).orElseThrow()));
        setTop(topColumn);
    }

    /**
     * Initializes the personalboard for the first time
     * @param message is the command line message for the player
     */
    public void preparePersonalBoard(String message){

        initializeFaithTrack();
        initializeMove();
        setStrongBox(strongBoxBuilder(getCache().getElem(SimpleStrongBox.class).orElseThrow(), this));
        SimpleCardCells simpleCardCells = getCache().getElem(SimpleCardCells.class).orElseThrow();
        Row prodsRow = productionsBuilder(simpleCardCells);
        setProductions(prodsRow);
        setMessage(message);
        getCLIView().setBody(this);
        getCLIView().show();

    }

    public static void seePersonalBoard(int playerIndex, ViewMode mode){
        PlayerCache playerCache = getSimpleModel().getPlayerCache(playerIndex);
        PersonalBoardBody board = new PersonalBoardBody(playerCache, PersonalBoardBody.Mode.VIEWING);
        board.setRightBuilderForViewMode(mode);
        board.preparePersonalBoard("Press ENTER to go back");
    }

    @Override
    public String toString() {
        return getMode().getString(this);
    }
}
