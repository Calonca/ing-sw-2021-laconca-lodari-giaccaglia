package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.simplemodel.PlayerCache;
import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.VerticalList;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableFaithTrack;
import it.polimi.ingsw.client.view.CLI.layout.drawables.ResourceCLI;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.simplemodel.SimpleDiscardBox;
import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;
import javafx.util.Pair;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class PersonalBoardBody extends CLIelem {

    DrawableFaithTrack faithTrack;
    VerticalList discardBox;

    public PersonalBoardBody(PlayerCache cache, boolean showSimpleTrack, boolean showSimpleDiscardBox) {
        if (showSimpleTrack)
            this.faithTrack = new DrawableFaithTrack(cache.getElem(SimpleFaithTrack.class).orElseThrow());
        if (showSimpleDiscardBox)
            this.discardBox = discardBoxBuilder(cache.getElem(SimpleDiscardBox.class).orElseThrow());
    }

    private static VerticalList discardBoxBuilder(SimpleDiscardBox simpleDiscardBox){
        Map<Integer, Pair<ResourceAsset, Integer>> discardBoxMap = simpleDiscardBox.getResourceMap();
        Stream<Option> optionList = discardBoxMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(e->{
            Drawable dw = new Drawable();
            //dw.add(0, String.valueOf(e.getKey()+4),Color.CYAN, Background.DEFAULT);
            ResourceCLI resourceCLI = ResourceCLI.fromAsset(e.getValue().getKey());
            dw.add(0, e.getValue().getValue()+" "+resourceCLI.getFullName(),resourceCLI.getC(), Background.DEFAULT);
            Option o = Option.from(dw, ResourceMarketViewBuilder::sendDiscard);
            return o;
        });
        VerticalList discardedBoxList = new VerticalList(optionList,20);
        discardedBoxList.addOption(Option.from("Discard", ResourceMarketViewBuilder::sendDiscard));
        System.out.println(Color.colorString(JsonUtility.serialize(discardBoxMap),Color.GOLD));
        return discardedBoxList;
    }

    public void selectInDiscardBox(){
        discardBox.selectAndRunOption(cli);
    }

    @Override
    public String toString() {
        return faithTrack.toString()+discardBox.toString();
    }
}
