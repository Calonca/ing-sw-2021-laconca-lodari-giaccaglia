package it.polimi.ingsw.network.messages.servertoclient;

import javafx.util.Pair;

import java.util.UUID;

public class MatchesData extends ServerToClientMessage {
    protected Pair<UUID,String[]>[] matchesData;


    public MatchesData(Pair<UUID,String[] >[] matchesData) {
        super();
        this.matchesData = matchesData;
    }

}
