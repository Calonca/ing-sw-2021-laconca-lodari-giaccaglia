package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.simplemodel.PlayerCache;
import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.CLI.layout.*;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableDevCard;
import it.polimi.ingsw.client.view.CLI.layout.drawables.FaithTrackGridElem;
import it.polimi.ingsw.client.view.CLI.layout.drawables.ResourceCLI;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.*;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PersonalBoardBody extends CLIelem {

    public enum Mode{

        MOVING_RES(){
                @Override
        public void initialize(PersonalBoardBody board){
                    board.mode = MOVING_RES;
                    PlayerCache cache = board.cache;
                    SimpleFaithTrack[] simpleFaithTracks =Arrays.stream(board.simpleModel.getPlayersCaches())
                            .map(c->c.getElem(SimpleFaithTrack.class).orElseThrow()).toArray(SimpleFaithTrack[]::new);
                    board.faithTrack = new FaithTrackGridElem(cache.getElem(SimpleFaithTrack.class).orElseThrow(),simpleFaithTracks);

                    board.initializeMove();
                    board.strongBox = strongBoxBuilder(cache.getElem(SimpleStrongBox.class).orElseThrow(), board);
                    board.message = "Select move starting position or discard resources";
                }

            @Override
            public String getString(PersonalBoardBody board) {

                board.root = new Column();

                board.root.setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);
                board.root.addElem(board.faithTrack);

                Row depotsAndProds = board.root.addAndGetRow();

                Column depots = depotsAndProds.addAndGetColumn();
                depots.addElemNoIndexChange(board.wareHouseLeadersDepot);

                depots.addElem(new SizedBox(0,2));
                depots.addElem(board.strongBox);

                depotsAndProds.addElem(board.discardBox);

                depotsAndProds.selectInEnabledOption(cli,board.message);
                return  CanvasBody.fromGrid(board.root).toString();
            }
        },
        SELECT_CARD_SHOP(){
            @Override
            public void initialize(PersonalBoardBody board){
                board.mode = SELECT_CARD_SHOP;
                PlayerCache cache = board.cache;
                SimpleFaithTrack[] simpleFaithTracks =Arrays.stream(board.simpleModel.getPlayersCaches())
                        .map(c->c.getElem(SimpleFaithTrack.class).orElseThrow()).toArray(SimpleFaithTrack[]::new);
                board.faithTrack = new FaithTrackGridElem(cache.getElem(SimpleFaithTrack.class).orElseThrow(),simpleFaithTracks);
                board.productions = new Row();
                SimpleCardShop cardShop = board.simpleModel.getElem(SimpleCardShop.class).orElseThrow();
                Drawable purchasedCard = DrawableDevCard.fromDevCardAsset(cardShop.getPurchasedCard().map(c->c.getDevelopmentCard()).orElseThrow());
                board.productions.addElem(Option.noNumber(purchasedCard));
                board.wareHouseLeadersDepot = wareBuilder(cache.getElem(SimpleWarehouseLeadersDepot.class).orElseThrow(),board);
                board.strongBox = strongBoxBuilder(cache.getElem(SimpleStrongBox.class).orElseThrow(), board);
                board.message = "Select the resources to buy the card";
            }

            @Override
            public String getString(PersonalBoardBody board) {

                board.root = new Column();

                board.root.setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);
                board.root.addElem(board.faithTrack);

                Row depotsAndProds = board.root.addAndGetRow();

                Column depots = depotsAndProds.addAndGetColumn();
                depots.addElemNoIndexChange(board.wareHouseLeadersDepot);

                depots.addElem(new SizedBox(0,2));
                depots.addElem(board.strongBox);

                depotsAndProds.addElem(board.productions);

                depotsAndProds.selectInEnabledOption(cli,board.message);
                return  CanvasBody.fromGrid(board.root).toString();
            }
        },
        TEST() {
            @Override
            public void initialize(PersonalBoardBody board) {
                board.mode = MOVING_RES;
                board.wareHouseLeadersDepot = wareBuilder(board.cache.getElem(SimpleWarehouseLeadersDepot.class).orElseThrow(),board);
            }

            @Override
            public String getString(PersonalBoardBody board) {
                return "Test";
            }
        };

        public abstract void initialize(PersonalBoardBody board);

        public abstract String getString(PersonalBoardBody board);

    }

    Column root = new Column();
    FaithTrackGridElem faithTrack;
    Column discardBox;
    Row strongBox;
    Column wareHouseLeadersDepot;
    Row productions;

    PlayerCache cache;
    SimpleModel simpleModel;
    String message;
    
    Mode mode;
    Integer lastSelectedPosition;
    Map<Integer, List<Integer>> movPos;

    public PersonalBoardBody(PlayerCache cache, Mode mode, SimpleModel simpleModel) {
        this.cache = cache;
        this.simpleModel = simpleModel;
        mode.initialize(this);
    }

    private boolean getSelectable(PersonalBoardBody board,int pos){
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
            .map(e->optionFromAsset(e.getValue().getKey(),e.getValue().getValue(),0,getSelectable(board,e.getKey()),e.getKey()));
        Column discardedBoxList = new Column(optionList);
        Option faith = optionFromAsset(ResourceAsset.FAITH,faithPoints,0,false,-100);
        faith.setMode(Option.VisMode.NO_NUMBER);
        discardedBoxList.addElem(faith);
        Option discard = Option.from("Discard", ResourceMarketViewBuilder::sendDiscard);
        discard.setEnabled(simpleDiscardBox.isDiscardable());
        discardedBoxList.addElem(discard);
        return discardedBoxList;

    }

    private static Row strongBoxBuilder(SimpleStrongBox simpleStrongBox, PersonalBoardBody board){

        Map<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>> strongBoxMap = simpleStrongBox.getResourceMap();
        Stream<Option> optionList = strongBoxMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .map(e-> {
                    Option strongOption = board.optionFromAsset(e.getValue().getKey(), numAndSel(e).getKey(), numAndSel(e).getValue(),false,e.getKey() );
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
        if (mode.equals(Mode.MOVING_RES))
            r = ()->{
                if (lastSelectedPosition==null){
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
        Option o = Option.from(dw, r);
        o.setEnabled(selectable);
        o.setSelected(lastSelectedPosition!=null&&lastSelectedPosition==globalPos);
        return o;
    }

    private void initializeMove() {
        SimpleDiscardBox sd = cache.getElem(SimpleDiscardBox.class).orElseThrow();
        SimpleWarehouseLeadersDepot sw = cache.getElem(SimpleWarehouseLeadersDepot.class).orElseThrow();

        movPos = sw.getAvailableMovingPositions();
        sd.getAvailableMovingPositions().forEach((key, value) -> movPos.put(key, value));

        discardBox = discardBoxBuilder(sd, this);
        wareHouseLeadersDepot = wareBuilder(sw,this);
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
             .mapToObj(i -> body.optionFromAsset(e.getValue().get(i).getKey(), 1, 0, body.getSelectable(body, finalPos +i), finalPos +i)));
            rows.add(row1);
            pos+=e.getValue().size();
        }

        for (Row row : rows) {
            wareGrid.addElem(row);
        }
        return wareGrid;
    }

    @Override
    public String toString() {
        return mode.getString(this);
    }
}
