package it.polimi.ingsw.network.simplemodel;

import javafx.util.Pair;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class VaticanReportInfo extends SimpleModelElement{

    // Lorenzo has index -1

    private Set<Integer> playersTriggeringVaticanReport;

    //        playerIndex  popeTileNumber isActive
    private  Map<Integer, Pair<Integer, Boolean>> popeTileState;

    private final AtomicBoolean hasReportBeenShown = new AtomicBoolean(false);

    private long timeOfReport;


    public VaticanReportInfo(){
        hasReportBeenShown.set(false);
    }

    public VaticanReportInfo(Set<Integer> playersTriggeringVaticanReport, Map<Integer, Pair<Integer, Boolean>> popeTileStatus){
        this.playersTriggeringVaticanReport = playersTriggeringVaticanReport;
        this.popeTileState = popeTileStatus;
    }

    @Override
    public void update(SimpleModelElement element) {
        VaticanReportInfo serverElement = (VaticanReportInfo)element;
        playersTriggeringVaticanReport = serverElement.playersTriggeringVaticanReport;
        popeTileState = serverElement.popeTileState;
        hasReportBeenShown.set(false);

    }

    public Set<Integer> getPlayersTriggeringVaticanReport(){
        return playersTriggeringVaticanReport;
    }

    public  Map<Integer, Pair<Integer, Boolean>> getPopeTileStatusMap(){
        return popeTileState;
    }

    public boolean hasReportOccurred(){
        return !playersTriggeringVaticanReport.isEmpty();
    }


    public void reportWillBeShown(){
        hasReportBeenShown.set(true);
    }

    public boolean hasReportBeenShown(){
        return hasReportBeenShown.get();
    }


}
