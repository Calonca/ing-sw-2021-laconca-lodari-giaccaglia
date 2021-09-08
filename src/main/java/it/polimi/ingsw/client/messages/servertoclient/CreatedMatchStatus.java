package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.cli.match.CreateJoinLoadMatchCLI;

import java.util.UUID;

/**
 * This message is sent after creating a match, notifying the player
 */
public class CreatedMatchStatus extends it.polimi.ingsw.network.messages.servertoclient.CreatedMatchStatus implements ClientMessage {


    /**
     * @param matchId is the match UUID
     * @param m is a message for the user
     */
    public CreatedMatchStatus(UUID matchId, motive m) {
        super(matchId, m);
    }

    /**
     * This message sets the the correct data to be acquired by the listener
     * @param serverHandler is the corresponding Client's ServerHandler
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        if (matchId==null)
            serverHandler.getClient().changeViewBuilder(CreateJoinLoadMatchCLI.getBuilder(serverHandler.getClient().isCLI()));
        else serverHandler.getClient().getCommonData().setStartData(matchId,0);
    }

}
