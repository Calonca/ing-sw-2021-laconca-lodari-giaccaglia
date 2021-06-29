package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


/**
 * This is a message to update the existing and in progress matches
 */
public class MatchesData extends it.polimi.ingsw.network.messages.servertoclient.MatchesData implements ClientMessage {

    public MatchesData(Map<Pair<UUID, Boolean>, Pair<String[], String[]>> matchesData) {
        super(matchesData);
    }

    /**
     * This method sets the correct data to be acquired by the listener
     * @param serverHandler
     * @throws IOException
     */
    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        serverHandler.getClient().getCommonData().setMatchesData(Optional.ofNullable(matchesData));
    }
}
