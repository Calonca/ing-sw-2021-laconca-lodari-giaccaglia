package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.simplemodel.PlayerCache;
import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.CLI.layout.*;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.drawables.FaithTrackGridElem;
import it.polimi.ingsw.client.view.CLI.layout.drawables.ResourceCLI;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.SimpleDiscardBox;
import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;
import it.polimi.ingsw.network.simplemodel.SimpleStrongBox;
import it.polimi.ingsw.network.simplemodel.SimpleWarehouseLeadersDepot;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersonalBoardBody extends CLIelem {

    public enum Mode{

        MOVING_RES(){
                @Override
        public void initialize(PersonalBoardBody board, PlayerCache cache, SimpleModel simpleModel){
                    board.mode = MOVING_RES;
                    SimpleFaithTrack[] simpleFaithTracks =Arrays.stream(simpleModel.getPlayersCaches())
                            .map(c->c.getElem(SimpleFaithTrack.class).orElseThrow()).toArray(SimpleFaithTrack[]::new);
                    board.faithTrack = new FaithTrackGridElem(cache.getElem(SimpleFaithTrack.class).orElseThrow(),simpleFaithTracks);
                    board.discardBox = board.discardBoxBuilder(cache.getElem(SimpleDiscardBox.class).orElseThrow(), board);
                    board.wareHouseLeadersDepot = wareBuilder(cache.getElem(SimpleWarehouseLeadersDepot.class).orElseThrow(),board );
                    board.strongBox = strongBoxBuilder(cache.getElem(SimpleStrongBox.class).orElseThrow(), board);
                    
        }

            @Override
            public String getString(PersonalBoardBody board) {
                Column root = new Column();
                root.setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);
                root.addElem(board.faithTrack);

                Row endRow = root.addAndGetRow();

                Column depotsRow = endRow.addAndGetColumn();
                depotsRow.addElem(board.wareHouseLeadersDepot);

                depotsRow.addElem(new SizedBox(0,2));
                depotsRow.addElem(board.strongBox);

                endRow.addElem(board.discardBox);

                return  CanvasBody.fromGrid(root).toString();
            }
        },
        TEST() {
            @Override
            public void initialize(PersonalBoardBody board, PlayerCache cache, SimpleModel simpleModel) {
                board.mode = MOVING_RES;   
                board.wareHouseLeadersDepot = wareBuilder(cache.getElem(SimpleWarehouseLeadersDepot.class).orElseThrow(),board );
            }

            @Override
            public String getString(PersonalBoardBody board) {
                return "Test";
            }
        };

        public abstract void initialize(PersonalBoardBody board, PlayerCache cache, SimpleModel simpleModel);

        public abstract String getString(PersonalBoardBody board);

    }

    FaithTrackGridElem faithTrack;
    Column discardBox;
    Row strongBox;
    Column wareHouseLeadersDepot;
    
    Mode mode;

    public PersonalBoardBody(PlayerCache cache, Mode mode, SimpleModel simpleModel) {
        mode.initialize(this,cache, simpleModel);
    }

    private Column discardBoxBuilder(SimpleDiscardBox simpleDiscardBox, PersonalBoardBody board){

        Map<Integer, Pair<ResourceAsset, Integer>> discardBoxMap = simpleDiscardBox.getResourceMap();
        Stream<Option> optionList = discardBoxMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
            .map(e->optionFromAsset(e.getValue().getKey(),e.getValue().getValue(),0,board.mode ));
        Column discardedBoxList = new Column(optionList);
        discardedBoxList.addElem(Option.from("Discard", ResourceMarketViewBuilder::sendDiscard));
        return discardedBoxList;

    }

    private static Row strongBoxBuilder(SimpleStrongBox simpleStrongBox, PersonalBoardBody board){

        Map<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>> strongBoxMap = simpleStrongBox.getResourceMap();
        Stream<Option> optionList = strongBoxMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .map(e->optionFromAsset(e.getValue().getKey(), numAndSel(e).getKey(),numAndSel(e).getValue(), board.mode));
        return new Row(optionList);

    }

    private static Pair<Integer, Integer> numAndSel(Map.Entry<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>> e) {
        return e.getValue().getValue();
    }

    private static Option optionFromAsset(ResourceAsset asset, int n, int selected, Mode mode){
        Drawable dw = new Drawable();
        ResourceCLI resourceCLI = ResourceCLI.fromAsset(asset);
        dw.add(0, (n==1?"":n+" ")+resourceCLI.getFullName()+" ",resourceCLI.getC(), Background.DEFAULT);
        dw.add(0, (selected==0?"         ":selected+" selected"),resourceCLI.getC(), Background.DEFAULT);
        Runnable r=()->{};
        if (mode.equals(Mode.MOVING_RES))
            r = ()->{

            };
        return Option.from(dw, ResourceMarketViewBuilder::sendDiscard);
    }

    private static Column wareBuilder(SimpleWarehouseLeadersDepot simpleWare, PersonalBoardBody body){
        Column wareGrid = new Column();
        Map<Integer, List<Pair<ResourceAsset, Boolean>>> resMap = simpleWare.getDepots();
        List<Row> rows = resMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .map(e-> new Row(
                        e.getValue().stream().map(a -> optionFromAsset(a.getKey(), 1,0,body.mode ))))
                .collect(Collectors.toList());
        for (Row row : rows) {
            wareGrid.addElem(row);
        }
        return wareGrid;
    }


    public void selectInDiscardBox(){
        discardBox.selectAndRunOption(cli);
    }

    @Override
    public String toString() {
        return mode.getString(this);
    }
}
