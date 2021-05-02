package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.CLI.CLIBuilder;
import it.polimi.ingsw.client.view.CLI.CreateJoinLoadMatchView;
import it.polimi.ingsw.client.view.CLI.WaitForMatchToStart;
import it.polimi.ingsw.client.view.abstractview.View;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.UUID;

public class CreatedMatchStatus extends it.polimi.ingsw.network.messages.servertoclient.CreatedMatchStatus implements ClientMessage {


    public CreatedMatchStatus(ClientToServerMessage parent, UUID matchId, motive m) {
        super(parent, matchId, m);
    }

    /**
     * Method invoked in the client to process the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        if (matchId!=null)
            serverHandler.getClient().transitionToView(new WaitForMatchToStart(matchId));
        else
            serverHandler.getClient().transitionToView(new CreateJoinLoadMatchView());
    }

}
