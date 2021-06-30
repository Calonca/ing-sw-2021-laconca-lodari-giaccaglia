package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This message is sent after an incorrect or malicious message is sent to the server
 */
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

        int playerIndex = serverHandler.getClient().getCommonData().getThisPlayerIndex();
        int currentPlayerIndex = serverHandler.getClient().getCommonData().getCurrentPlayerIndex();
        String state = serverHandler.getClient().getSimpleModel().getPlayerCache(playerIndex).getCurrentState();
        serverHandler.getClient().setState(new StateInNetwork(playerIndex,currentPlayerIndex, state, new ArrayList<>(),new ArrayList<>()));
        serverHandler.getClient().changeViewBuilder(serverHandler.getClient().getCurrentViewBuilder());
        serverHandler.getClient().setState(new StateInNetwork(playerIndex,currentPlayerIndex, state,new ArrayList<>(),new ArrayList<>()));
        System.out.println(Color.colorString("The last event send was not valid, going back to the last valid state",Color.RED));
        //Todo better handling
    }
}
