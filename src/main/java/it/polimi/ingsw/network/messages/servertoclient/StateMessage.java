package it.polimi.ingsw.network.messages.servertoclient;

import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

public class StateMessage extends ServerToClientMessage {
    protected State state;
    protected String serializedObject;//Todo Use treemap instead
    protected int player;

    /**
     * Initializes the answer message.
     *
     * @param command The CommandMsg being answered.
     */
    public StateMessage(ClientToServerMessage command, int player, State state, String serializedObject) {
        super(command);
        this.state = state;
        this.serializedObject = serializedObject;
        this.player = player;
    }

    /**
     * Initializes a ServerToClientMessage that doesn't answer any messages.
     */
    public StateMessage(int player, State state, String serializedObject) {
        super();
        this.state = state;
        this.serializedObject = serializedObject;
        this.player = player;
    }
}
