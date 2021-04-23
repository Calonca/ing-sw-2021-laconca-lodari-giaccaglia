package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.abstractview.View;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

import java.io.IOException;

public class CreatedMatchStatus extends it.polimi.ingsw.network.messages.servertoclient.CreatedMatchStatus implements ClientMessage {

    public CreatedMatchStatus(ClientToServerMessage parent, boolean created) {
        super(parent, created);
    }


    /**
     * Method invoked in the client to process the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        if (created)
            serverHandler.getClient().transitionToView(new View() {
                @Override
                public void run() {
                    System.out.println("Created");
                }
            });
        else
            serverHandler.getClient().transitionToView(new View() {
            @Override
            public void run() {
                System.out.println("Not Created");
            }
        });
    }
}
