package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.simplemodel.SimpleModelElement;

import java.io.IOException;
import java.util.ArrayList;

public class EventNotValid extends it.polimi.ingsw.network.messages.servertoclient.EventNotValid implements ClientMessage {
    public EventNotValid(ClientToServerMessage clientToServerMessage) {
        super(clientToServerMessage);
    }

    /**
     * Notifies the last valid state when the last event was not valid
     */
    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        int playerI = serverHandler.getClient().getCommonData().getThisPlayerIndex();
        String state = serverHandler.getClient().getSimpleModel().getPlayerCache(playerI).getCurrentState();
        serverHandler.getClient().setState(new StateInNetwork(playerI,state,new ArrayList<>(),new ArrayList<>()));
        serverHandler.getClient().changeViewBuilder(serverHandler.getClient().getCurrentViewBuilder());
        System.out.println("Event not valid");
        //Todo better handling
    }
}
