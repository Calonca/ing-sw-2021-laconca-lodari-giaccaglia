package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

import java.io.IOException;
import java.util.ArrayList;

public class EventNotValid extends it.polimi.ingsw.network.messages.servertoclient.EventNotValid implements ClientMessage {

    public EventNotValid(ClientToServerMessage clientToServerMessage) {
        super(clientToServerMessage);
    }


    /**
     * Notifies the last valid state when the last event was not valid
     * @param serverHandler is the corresponding Client's ServerHandler
     * @throws IOException
     */
    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {

        int playerI = serverHandler.getClient().getCommonData().getThisPlayerIndex();
        String state = serverHandler.getClient().getSimpleModel().getPlayerCache(playerI).getCurrentState();
        serverHandler.getClient().setState(new StateInNetwork(playerI,state,new ArrayList<>(),new ArrayList<>()));
        serverHandler.getClient().changeViewBuilder(serverHandler.getClient().getCurrentViewBuilder());
        serverHandler.getClient().setState(new StateInNetwork(playerI,state,new ArrayList<>(),new ArrayList<>()));
        System.out.println(Color.colorString("The last event send was not valid, going back to the last valid state",Color.RED));
        //Todo better handling
    }
}
