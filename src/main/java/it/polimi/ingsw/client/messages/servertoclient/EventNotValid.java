package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.cli.textutil.Color;

import java.util.ArrayList;

/**
 * This message is sent after an incorrect or malicious message is sent to the server
 */
public class EventNotValid extends it.polimi.ingsw.network.messages.servertoclient.EventNotValid implements ClientMessage {



    /**
     * Notifies the last valid state when the last event was not valid
     * @param serverHandler is the corresponding Client's ServerHandler
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {

        int playerIndex = CommonData.getThisPlayerIndex();
        int currentPlayerIndex = serverHandler.getClient().getCommonData().getCurrentPlayerIndex();
        String currentPlayerNickname = serverHandler.getClient().getCommonData().getCurrentPlayerNickname();

        String state = serverHandler.getClient().getSimpleModel().getPlayerCache(playerIndex).getCurrentState();

        serverHandler.getClient().setState(new StateInNetwork(playerIndex,currentPlayerIndex, currentPlayerNickname,  state, new ArrayList<>(),new ArrayList<>()));
        serverHandler.getClient().changeViewBuilder(serverHandler.getClient().getCurrentViewBuilder());
        serverHandler.getClient().setState(new StateInNetwork(playerIndex,currentPlayerIndex, currentPlayerNickname,state,new ArrayList<>(),new ArrayList<>()));

        System.out.println(Color.colorString("The last event send was not valid, going back to the last valid state",Color.RED));
    }
}
