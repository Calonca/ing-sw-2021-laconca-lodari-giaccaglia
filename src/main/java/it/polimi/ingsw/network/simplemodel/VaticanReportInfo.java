package it.polimi.ingsw.network.simplemodel;

import javafx.util.Pair;

import java.util.Map;
import java.util.Set;

public class VaticanReportInfo extends SimpleModelElement{

    // Lorenzo has index -1

    private Set<Integer> playersTriggeringVaticanReport;

    //        playerIndex  popeTileNumber isActive
    private  Map<Integer, Pair<Integer, Boolean>> popeTileState;


    public VaticanReportInfo(){}

    public VaticanReportInfo(Set<Integer> playersTriggeringVaticanReport, Map<Integer, Pair<Integer, Boolean>> popeTileStatus){
        this.playersTriggeringVaticanReport = playersTriggeringVaticanReport;
        this.popeTileState = popeTileStatus;
    }

    @Override
    public void update(SimpleModelElement element) {
        VaticanReportInfo serverElement = (VaticanReportInfo)element;
        playersTriggeringVaticanReport = serverElement.playersTriggeringVaticanReport;
        popeTileState = serverElement.popeTileState;

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

}
