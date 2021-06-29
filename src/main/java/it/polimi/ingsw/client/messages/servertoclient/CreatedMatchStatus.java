package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.CLI.match.CreateJoinLoadMatchCLI;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

import java.io.IOException;
import java.util.UUID;

/**
 * This message is sent after creating a match, notifying the player
 */
public class CreatedMatchStatus extends it.polimi.ingsw.network.messages.servertoclient.CreatedMatchStatus implements ClientMessage {


    /**
     * @param parent is a sample message
     * @param matchId is the match UUID
     * @param m is a message for the user
     */
    public CreatedMatchStatus(ClientToServerMessage parent, UUID matchId, motive m) {
        super(parent, matchId, m);
    }

    /**
     * This message sets the the correct data to be acquired by the listener
     * @param serverHandler is the corresponding Client's ServerHandler
     * @throws IOException
     */
    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        if (matchId==null)
            serverHandler.getClient().changeViewBuilder(CreateJoinLoadMatchCLI.getBuilder(serverHandler.getClient().isCLI()));
        //Todo make a view do the transition
        else serverHandler.getClient().getCommonData().setStartData(matchId,0);
    }

}
