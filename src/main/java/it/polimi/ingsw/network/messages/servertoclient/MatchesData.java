package it.polimi.ingsw.network.messages.servertoclient;

import javafx.util.Pair;

import java.util.Map;
import java.util.UUID;

public class MatchesData extends ServerToClientMessage {
    protected Map<Pair<UUID,Boolean>, Pair<String[], String[]>> data;

    public MatchesData(Map<Pair<UUID,Boolean>,Pair<String[], String[]>> matchesData) {
        super();
        this.data = matchesData;
    }

}
