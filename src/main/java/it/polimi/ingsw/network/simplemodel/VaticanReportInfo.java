package it.polimi.ingsw.network.simplemodel;

import javafx.util.Pair;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class VaticanReportInfo extends SimpleModelElement{

    // Lorenzo has index -1

    private Set<Integer> playersTriggeringVaticanReport;

    //        playerIndex  popeTileNumber isActive
    private  Map<Integer, Pair<Integer, TileState>> popeTileState;

    private final AtomicBoolean hasReportBeenShown = new AtomicBoolean(false);


    public VaticanReportInfo(){
        hasReportBeenShown.set(false);
    }

    public VaticanReportInfo(Set<Integer> playersTriggeringVaticanReport, Map<Integer, Pair<Integer,TileState>> popeTileState){
        this.playersTriggeringVaticanReport = playersTriggeringVaticanReport;
        this.popeTileState = popeTileState;
    }

    @Override
    public void update(SimpleModelElement element) {
        VaticanReportInfo serverElement = (VaticanReportInfo)element;
        if(!serverElement.playersTriggeringVaticanReport.isEmpty() || !(playersTriggeringVaticanReport ==null))
            playersTriggeringVaticanReport = serverElement.playersTriggeringVaticanReport;
        popeTileState = serverElement.popeTileState;
        hasReportBeenShown.set(false);

    }

    public Set<Integer> getPlayersTriggeringVaticanReport(){
        return playersTriggeringVaticanReport;
    }

    public  Map<Integer, Pair<Integer, TileState>> getPopeTileStateMap(){
        return popeTileState;
    }

    public boolean hasReportOccurred(){
        return Objects.nonNull(playersTriggeringVaticanReport) && !playersTriggeringVaticanReport.isEmpty();
    }


    public void reportWillBeShown(){
        hasReportBeenShown.set(true);
    }

    public boolean hasReportBeenShown(){
        return hasReportBeenShown.get();
    }


}
