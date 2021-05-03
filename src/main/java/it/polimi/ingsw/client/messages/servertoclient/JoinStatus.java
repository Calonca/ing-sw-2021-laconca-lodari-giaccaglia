package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.CLI.CreateJoinLoadMatch;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

import java.io.IOException;
import java.util.UUID;

public class JoinStatus extends it.polimi.ingsw.network.messages.servertoclient.JoinStatus implements ClientMessage {

    public JoinStatus(ClientToServerMessage parent, UUID joinedMatchUUID, motive m) {
        super(parent, joinedMatchUUID, m);
    }

    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        if (joinedMatchUUID==null)
            serverHandler.getClient().changeViewBuilder(new CreateJoinLoadMatch(),null );
        //Todo make a view do the transition.
    }
}
