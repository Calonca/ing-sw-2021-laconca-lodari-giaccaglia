package it.polimi.ingsw.network.messages.servertoclient;

import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.server.model.State;

import java.util.Map;

public class StateMessage extends ServerToClientMessage {
    protected String state;
    protected Map<String,Object> serializedObject;//Todo Use treemap instead
    protected int player;

    /**
     * Initializes the answer message.
     *
     * @param command The CommandMsg being answered.
     */
    public StateMessage(ClientToServerMessage command, int player, String state, Map<String,Object> serializedObject) {
        super(command);
        this.state = state;
        this.serializedObject = serializedObject;
        this.player = player;
    }

    /**
     * Initializes a ServerToClientMessage that doesn't answer any messages.
     */
    public StateMessage(int player, String state, Map<String,Object> serializedObject) {
        super();
        this.state = state;
        this.serializedObject = serializedObject;
        this.player = player;
    }


}
