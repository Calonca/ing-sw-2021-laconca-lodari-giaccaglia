package it.polimi.ingsw.client.messages.servertoclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.messages.servertoclient.ServerToClientMessage;

import java.io.IOException;

import static it.polimi.ingsw.server.model.jsonUtility.deserializeFromString;

/**
 * All the messages from the server to the client implement this interface.<br>
 * It contains methods that can only be used in the client
 */
public interface ClientMessage {

    static ClientMessage deserialize(String jsonString) {
        RuntimeTypeAdapterFactory<ClientMessage> jsonToClientAdapter = RuntimeTypeAdapterFactory.of(ClientMessage.class);

        //Register here all the message types
        jsonToClientAdapter.registerSubtype(CreatedMatchStatus.class);
        jsonToClientAdapter.registerSubtype(JoinStatus.class);
        jsonToClientAdapter.registerSubtype(MatchesData.class);
        jsonToClientAdapter.registerSubtype(StateMessageContainer.class);

        Gson gson1 = new GsonBuilder()
                .registerTypeAdapterFactory(jsonToClientAdapter)
                .registerTypeAdapterFactory(ServerToClientMessage.stateMessageAdapter())
                .create();

        return deserializeFromString(jsonString, ClientMessage.class, gson1);
    }

    /**
     * Will update values in the client that will fire view updates.
     */
    void processMessage(ServerHandler serverHandler) throws IOException;
}
