package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.CLI.CreateJoinLoadMatch;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MatchesData extends it.polimi.ingsw.network.messages.servertoclient.MatchesData implements ClientMessage {


    public MatchesData(Map<UUID, String[]> matchesData) {
        super(matchesData);
    }


    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        serverHandler.getClient().getCommonData().setMatchesData(Optional.ofNullable(matchesData));
    }
}
