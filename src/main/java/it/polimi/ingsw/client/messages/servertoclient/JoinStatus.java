package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.CLI.CLIBuilder;
import it.polimi.ingsw.client.view.abstractview.View;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

import java.io.IOException;

public class JoinStatus extends it.polimi.ingsw.network.messages.servertoclient.JoinStatus implements ClientMessage {
    public JoinStatus(ClientToServerMessage parent, boolean joined, motive m) {
        super(parent, joined, m);
    }

    /**
     * Method invoked in the client to process the message.
     *
     * @param serverHandler
     */
    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        if (joined)
            serverHandler.getClient().transitionToView(new View() {
                @Override
                public void run() {
                    CLIBuilder.scroll();
                    System.out.println("joined");
                }
            });
        else
            serverHandler.getClient().transitionToView(new View() {
                @Override
                public void run() {
                    System.out.println("Not joined");
                }
            });
    }
}
