package it.polimi.ingsw.client.view.GUI.GUIelem;

import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import javafx.util.Pair;

import java.util.Map;
import java.util.UUID;

public class MatchRow {
    Map.Entry<UUID, Pair<String[], String[]>> uuidPair;

    public MatchRow(Map.Entry<UUID, Pair<String[], String[]>> uuidPair) {
        this.uuidPair = uuidPair;
    }

    public UUID getId()
    { return  uuidPair.getKey();}

    public Map.Entry<UUID, Pair<String[], String[]>> getUuidPair() {
        return uuidPair;
    }

    public String getKey() {
        return CreateJoinLoadMatchViewBuilder.idAndNames(uuidPair).getKey();
    }

    public String getPeople() {
        return    ""+getKey()+
                "\n"+CreateJoinLoadMatchViewBuilder.idAndNames(uuidPair).getValue();
    }
}
