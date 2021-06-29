package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.CLI.match.CreateJoinLoadMatchCLI;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

import java.io.IOException;
import java.util.UUID;


/**
 * This message is sent to the player after joining an existing match, informing on its status
 */
public class JoinStatus extends it.polimi.ingsw.network.messages.servertoclient.JoinStatus implements ClientMessage {


    public JoinStatus(ClientToServerMessage parent, UUID joinedMatchUUID, motive m, int playerIndex) {
        super(parent, joinedMatchUUID, m, playerIndex);
    }

    /**
     * This method sets the correct data to be acquired by the listener
     * @param serverHandler is the corresponding Client's ServerHandler
     * @throws IOException
     */
    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        if (joinedMatchUUID==null)
            serverHandler.getClient().changeViewBuilder(CreateJoinLoadMatchCLI.getBuilder(serverHandler.getClient().isCLI()));

        //Todo make a view do the transition.
        else serverHandler.getClient().getCommonData().setStartData(joinedMatchUUID,playerIndex);
    }
}
