package it.polimi.ingsw.network.messages.servertoclient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MatchesData extends ServerToClientMessage {
    protected Map<UUID,String[]> matchesData;

    public MatchesData(){}

    public MatchesData(Map<UUID,String[]> matchesData) {
        super();
        this.matchesData = matchesData;
    }

}
