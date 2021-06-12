package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.simplemodel.PlayerCache;
import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.CLI.layout.*;
import it.polimi.ingsw.client.view.CLI.layout.drawables.*;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.layout.GridElem;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ProductionViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.*;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getThisPlayerCache;

public class PersonalBoardBody extends CLIelem {

    public enum Mode{

        MOVING_RES(){
            @Override
            public String getString(PersonalBoardBody board) {

                board.root = new Column();

                board.root.setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);
                board.root.addElem(board.top);

                Row depotsAndProds = board.root.addAndGetRow();

                Column depots = depotsAndProds.addAndGetColumn();
                depots.addElemNoIndexChange(board.wareHouseLeadersDepot);

                depots.addElem(new SizedBox(0,2));
                depots.addElem(board.strongBox);

                depotsAndProds.addElem(board.discardBox);
                depotsAndProds.addElem(board.productions);

                depotsAndProds.selectInEnabledOption(cli,board.message);
                return  CanvasBody.fromGrid(board.root).toString();
            }
        },
        SELECT_CARD_SHOP(){
            @Override
            public String getString(PersonalBoardBody board) {

                board.root = new Column();

                board.root.addElem(board.top);

                Row depotsAndProds = board.root.addAndGetRow();
                depotsAndProds.setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);

                Column depots = depotsAndProds.addAndGetColumn();
                depots.addElemNoIndexChange(board.wareHouseLeadersDepot);

                depots.addElem(new SizedBox(0,2));
                depots.addElem(board.strongBox);

                depotsAndProds.addElem(board.productions);

                depotsAndProds.selectInEnabledOption(cli,board.message);
                return  CanvasBody.fromGrid(board.root).toString();
            }
        },
        SELECT_RES_FOR_PROD(){
            @Override
            public String getString(PersonalBoardBody board) {

                board.root = new Column();

                board.root.addElem(board.top);

                Row depotsAndProds = board.root.addAndGetRow();
                depotsAndProds.setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);

                Column depots = depotsAndProds.addAndGetColumn();
                depots.addElemNoIndexChange(board.wareHouseLeadersDepot);

                depots.addElem(new SizedBox(0,2));
                depots.addElem(board.strongBox);

                depotsAndProds.addElem(board.productions);

                depotsAndProds.selectInEnabledOption(cli,board.message);
                return  CanvasBody.fromGrid(board.root).toString();
            }
        },
        CHOOSE_POS_FOR_CARD(){
            @Override
            public String getString(PersonalBoardBody board) {

                board.root = new Column();
                board.root.setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);
                board.root.addElem(board.top);

                Row depotsAndProds = board.root.addAndGetRow();

                Column depots = depotsAndProds.addAndGetColumn();
                depots.addElemNoIndexChange(board.wareHouseLeadersDepot);

                depots.addElem(new SizedBox(0,2));
                depots.addElem(board.strongBox);

                depotsAndProds.addElem(board.productions);
                board.productions.selectInEnabledOption(cli,board.message);
                return  CanvasBody.fromGrid(board.root).toString();
            }
        },
        CHOOSE_PRODUCTION(){
            @Override
            public String getString(PersonalBoardBody board) {

                board.root = new Column();
                board.root.setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);
                board.root.addElem(board.top);

                Row depotsAndProds = board.root.addAndGetRow();

                Column depots = depotsAndProds.addAndGetColumn();
                depots.addElemNoIndexChange(board.wareHouseLeadersDepot);

                depots.addElem(new SizedBox(0,2));
                depots.addElem(board.strongBox);

                depotsAndProds.addElem(board.productions);
                board.productions.selectInEnabledOption(cli,board.message, ProductionViewBuilder::sendProduce);
                return  CanvasBody.fromGrid(board.root).toString();
            }
        };


        public abstract String getString(PersonalBoardBody board);

    }

    Column root = new Column();

    GridElem top;
    Column discardBox;
    Row strongBox;
    Column wareHouseLeadersDepot;
    Row productions;

    PlayerCache cache;
    String message;
    Mode mode;

    Integer lastSelectedPosition;
    Map<Integer, List<Integer>> movPos;
    ResChoiceRow resChoiceRow;

    public PersonalBoardBody(PlayerCache cache, Mode mode) {
        this.cache = cache;
        this.mode=mode;
    }

    public void setTop(GridElem faithTrack) {
        this.top = faithTrack;
    }

    public void setResChoiceRow(ResChoiceRow resChoiceRow) {
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

    public void setWareHouseLeadersDepot(Column wareHouseLeadersDepot) {
        this.wareHouseLeadersDepot = wareHouseLeadersDepot;
    }

    public void setProductions(Row productions) {
        this.productions = productions;
    }

    private boolean getSelectableMove(PersonalBoardBody board, int pos){
        boolean selectable = false;
        if (board.mode.equals(Mode.MOVING_RES)){
            if (board.lastSelectedPosition==null)
                selectable = !movPos.get(pos).isEmpty();
            else {
                List<Integer> moveEndPos = movPos.get(lastSelectedPosition);
                selectable = moveEndPos.contains(pos);
            }
        }
        return selectable;
    }

    private Column discardBoxBuilder(SimpleDiscardBox simpleDiscardBox, PersonalBoardBody board){
        Map<Integer, Pair<ResourceAsset, Integer>> discardBoxMap = simpleDiscardBox.getResourceMap();
        Optional<Integer> faithPointsPos = discardBoxMap.entrySet().stream().filter(e->e.getValue().getKey().equals(ResourceAsset.FAITH)).map(Map.Entry::getKey).findFirst();
        int faithPoints = faithPointsPos.map(p->discardBoxMap.get(faithPointsPos.get()).getValue()).orElse(0);
        faithPointsPos.ifPresent(discardBoxMap::remove);

        Stream<Option> optionList = discardBoxMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
            .map(e->optionFromAsset(e.getValue().getKey(),e.getValue().getValue(),0, getSelectableMove(board,e.getKey()),e.getKey()));
        Column discardedBoxList = new Column(optionList);
        Option faith = optionFromAsset(ResourceAsset.FAITH,faithPoints,0,false,-100);
        faith.setMode(Option.VisMode.NO_NUMBER);
        discardedBoxList.addElem(faith);
        Option discard = Option.from("Discard", ResourceMarketViewBuilder::sendDiscard);
        discard.setEnabled(simpleDiscardBox.isDiscardable());
        discardedBoxList.addElem(discard);
        return discardedBoxList;

    }

    public Row productionsBuilder(SimpleCardCells simpleCardCells){
        Map<Integer,Optional<NetworkDevelopmentCard>> frontCards=simpleCardCells.getDevCardsCells().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e->e.getValue().map(DevelopmentCardAsset::getDevelopmentCard)));
        Option basicProdOption = simpleCardCells.getSimpleProductions().getProductionAtPos(0).map(p->{
            Drawable basicProdDrawable = new Drawable();
            basicProdDrawable.add(0,"╔═ Basic Production ═════╗");
            basicProdDrawable.add(Drawable.copyShifted(0,1,DrawableProduction
                    .fromInputAndOutput(p.getInputResources(),p.getOutputResources())
            ));
            basicProdDrawable.add(0,"╚════════════════════════╝");
            Option o = Option.from(basicProdDrawable,getProdRunnable(0));
            o.setMode(Option.VisMode.NUMBER_TO_BOTTOM);
            o.setSelected(p.isSelected());
            setProdAvailable(simpleCardCells, 0, o);
            return o;
        }).orElse(null);


        Stream<Option> normalProdsList = frontCards.entrySet().stream().map(e-> {
            int prodPos = e.getKey();
            Option cell = prodOption(
                    getProdRunnable(prodPos),
                    e.getValue().orElse(null),
                    simpleCardCells.getCellHeight(e.getKey()).orElse(0)
            );
            setProdAvailable(simpleCardCells, prodPos, cell);
            cell.setSelected(cell.isSelected());
            return cell;
        });
        return new Row(Stream.concat(Stream.ofNullable(basicProdOption),normalProdsList));
    }

    @NotNull
    private Runnable getProdRunnable(int prodPos) {
        return () -> {
            if (mode.equals(Mode.CHOOSE_POS_FOR_CARD)) {
                CardShopViewBuilder.sendCardPlacementPosition(prodPos);
            } else if (mode.equals(Mode.CHOOSE_PRODUCTION)) {
                ProductionViewBuilder.sendChosenProduction(prodPos);
            }
        };
    }

    private void setProdAvailable(SimpleCardCells simpleCardCells, int prodPos, Option cell) {
        if (mode.equals(Mode.CHOOSE_POS_FOR_CARD))
            cell.setEnabled(simpleCardCells.isSpotAvailable(prodPos));
        else if (mode.equals(Mode.CHOOSE_PRODUCTION))
            cell.setEnabled(simpleCardCells.getSimpleProductions().isProductionAtPositionAvailable(prodPos).orElse(false));
        else cell.setMode(Option.VisMode.NO_NUMBER);
    }

    public static Row strongBoxBuilder(SimpleStrongBox simpleStrongBox, PersonalBoardBody board){
        Map<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>> strongBoxMap = simpleStrongBox.getResourceMap();

        Stream<Option> optionList = strongBoxMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .map(e-> {
                    boolean selectable = board.mode.equals(Mode.SELECT_CARD_SHOP) &&
                            numAndSel(e).getKey() > numAndSel(e).getValue() &&
                            board.resChoiceRow.getPointedResource().isPresent() &&
                            board.resChoiceRow.getPointedResource().get().equals(e.getValue().getKey());
                    Option strongOption = board.optionFromAsset(e.getValue().getKey(), numAndSel(e).getKey(),
                            numAndSel(e).getValue()+(board.resChoiceRow!=null?
                                    (int)board.resChoiceRow.getChosenInputPos().stream().filter(p->p.equals(e.getKey())).count():
                                    0)
                            ,selectable,e.getKey() );
                    if (board.mode.equals(Mode.MOVING_RES))
                        strongOption.setEnabled(false);
                    return strongOption;
                });
        return new Row(optionList);

    }

    private static Pair<Integer, Integer> numAndSel(Map.Entry<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>> e) {
        return e.getValue().getValue();
    }

    private Option optionFromAsset(ResourceAsset asset, int numOf, int selected, boolean selectable, int globalPos){
        Drawable dw = new Drawable();
        ResourceCLI resourceCLI = ResourceCLI.fromAsset(asset);
        dw.add(0, (numOf ==1?"": numOf +" ")+resourceCLI.getFullName()+" ",resourceCLI.getC(), Background.DEFAULT);
        dw.add(0, (selected==0?"         ":selected+" selected"),resourceCLI.getC(), Background.DEFAULT);
        Runnable r=()->{};
        if (mode.equals(Mode.MOVING_RES)) {
            r = () -> {
                if (lastSelectedPosition == null) {
                    lastSelectedPosition = globalPos;
                    initializeMove();
                    message = "Select end position or discard resources";
                    cli.show();
                } else {
                    int startPosition = lastSelectedPosition;
                    cli.show();//Animation
                    lastSelectedPosition = null;
                    ResourceMarketViewBuilder.sendMove(startPosition, globalPos);
                }
            };
        }else if (mode.equals(Mode.SELECT_CARD_SHOP)){
            r= ()->{
                resChoiceRow.setNextInputPos(globalPos,asset);
                initializeBuyOrChoosePos();
                cli.show();
                message = "All resources selected, buying card...";
                if (resChoiceRow.getPointedResource().isEmpty()){
                    CardShopViewBuilder.sendResourcesToBuy(resChoiceRow.getChosenInputPos());
                }
            };
        }
        Option o = Option.from(dw, r);
        o.setEnabled(selectable);
        if (mode.equals(Mode.MOVING_RES)) {
            o.setSelected(lastSelectedPosition != null && lastSelectedPosition == globalPos);
        }else if (mode.equals(Mode.SELECT_CARD_SHOP)) {
            o.setSelected(resChoiceRow.getChosenInputPos().contains(globalPos));
        }
        return o;
    }

    public void initializeMove() {
        SimpleDiscardBox sd = cache.getElem(SimpleDiscardBox.class).orElseThrow();
        SimpleWarehouseLeadersDepot sw = cache.getElem(SimpleWarehouseLeadersDepot.class).orElseThrow();

        movPos = sw.getAvailableMovingPositions();
        sd.getAvailableMovingPositions().forEach((key, value) -> movPos.put(key, value));

        discardBox = discardBoxBuilder(sd, this);
        wareHouseLeadersDepot = wareBuilder(sw,this);
    }

    public void initializeBuyOrChoosePos() {
        wareHouseLeadersDepot = wareBuilder(cache.getElem(SimpleWarehouseLeadersDepot.class).orElseThrow(), this);
        strongBox = strongBoxBuilder(cache.getElem(SimpleStrongBox.class).orElseThrow(), this);
    }


    private static Column wareBuilder(SimpleWarehouseLeadersDepot simpleWare, PersonalBoardBody body){
        Column wareGrid = new Column();
        Map<Integer, List<Pair<ResourceAsset, Boolean>>> resMap = simpleWare.getDepots();
        List<Map.Entry<Integer, List<Pair<ResourceAsset, Boolean>>>> toSort = new ArrayList<>(resMap.entrySet());
        toSort.sort(Map.Entry.comparingByKey());
        List<Row> rows = new ArrayList<>();
        int pos=0;
        for (Map.Entry<Integer, List<Pair<ResourceAsset, Boolean>>> e : toSort) {//Loop in rows
            int finalPos = pos;
            Row row1 = new Row(IntStream.range(0,e.getValue().size())
             .mapToObj(i -> {
                 boolean selectable;
                 Pair<ResourceAsset,Boolean> pair = e.getValue().get(i);
                 if (body.mode.equals(Mode.MOVING_RES)) selectable = body.getSelectableMove(body, finalPos + i);
                 else if (body.mode.equals(Mode.SELECT_CARD_SHOP)) selectable = body.getSelected(pair);
                 else selectable = false;

                 return body.optionFromAsset(pair.getKey(), 1, 0,selectable , finalPos + i);
             }));
            rows.add(row1);
            pos+=e.getValue().size();
        }

        for (Row row : rows) {
            wareGrid.addElem(row);
        }
        return wareGrid;
    }

    private boolean getSelected(Pair<ResourceAsset, Boolean> pair) {
        return resChoiceRow.getPointedResource().isPresent()
                && pair.getKey().equals(resChoiceRow.getPointedResource().get())
                && pair.getValue().equals(false);
    }

    public static Option prodOption(Runnable r, NetworkDevelopmentCard card,int stackHeight){
        Drawable d = DrawableDevCard.fromDevCardAsset(card,stackHeight);
        Option o = Option.from(d,r);
        o.setMode(Option.VisMode.NUMBER_TO_BOTTOM);
        return o;
    }

    public void initializeFaithTrack(SimpleModel simpleModel) {
        SimpleFaithTrack[] simpleFaithTracks = Arrays.stream(simpleModel.getPlayersCaches())
                .map(c->c.getElem(SimpleFaithTrack.class).orElseThrow()).toArray(SimpleFaithTrack[]::new);
        setTop(new FaithTrackGridElem(getThisPlayerCache().getElem(SimpleFaithTrack.class).orElseThrow(),simpleFaithTracks));
    }

    @Override
    public String toString() {
        return mode.getString(this);
    }
}
