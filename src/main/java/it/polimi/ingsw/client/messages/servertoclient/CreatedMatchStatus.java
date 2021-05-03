package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.CLI.CreateJoinLoadMatch;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

import java.io.IOException;
import java.util.UUID;

public class CreatedMatchStatus extends it.polimi.ingsw.network.messages.servertoclient.CreatedMatchStatus implements ClientMessage {


    public CreatedMatchStatus(ClientToServerMessage parent, UUID matchId, motive m) {
        super(parent, matchId, m);
    }

    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        if (matchId==null)
            serverHandler.getClient().changeViewBuilder(new CreateJoinLoadMatch(), null);
        //Todo make a view do the transition
        else serverHandler.getClient().getCommonData().setStartData(matchId,0);
    }

}
