package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.cli.match.CreateJoinLoadMatchCLI;

import java.util.UUID;


/**
 * This message is sent to the player after joining an existing match, informing on its status
 */
public class JoinStatus extends it.polimi.ingsw.network.messages.servertoclient.JoinStatus implements ClientMessage {

    public JoinStatus(UUID joinedMatchUUID, motive m, int playerIndex) {
        super(joinedMatchUUID, m, playerIndex);
    }

    /**
     * This method sets the correct data to be acquired by the listener
     * @param serverHandler is the corresponding Client's ServerHandler
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        if (joinedMatchUUID==null)
            serverHandler.getClient().changeViewBuilder(CreateJoinLoadMatchCLI.getBuilder(serverHandler.getClient().isCLI()));


        else serverHandler.getClient().getCommonData().setStartData(joinedMatchUUID,playerIndex);
    }
}
