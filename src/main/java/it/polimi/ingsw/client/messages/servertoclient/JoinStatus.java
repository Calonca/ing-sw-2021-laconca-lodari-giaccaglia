package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.CLI.CreateJoinLoadMatch;
import it.polimi.ingsw.client.view.CLI.CLIelem.Spinner;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

import java.io.IOException;
import java.util.UUID;

public class JoinStatus extends it.polimi.ingsw.network.messages.servertoclient.JoinStatus implements ClientMessage {

    public JoinStatus(ClientToServerMessage parent, UUID joinedMatchUUID, motive m) {
        super(parent, joinedMatchUUID, m);
    }

    /**
     * Method invoked in the client to process the message.
     *
     * @param serverHandler
     */
    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        if (joinedMatchUUID==null)
            serverHandler.getClient().transitionToView(new CreateJoinLoadMatch());
    }
}
