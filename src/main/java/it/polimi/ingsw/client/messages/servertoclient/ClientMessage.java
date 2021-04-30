package it.polimi.ingsw.client.messages.servertoclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.RuntimeTypeAdapterFactory;

import java.io.IOException;

import static it.polimi.ingsw.server.model.jsonUtility.deserializeFromString;

/**
 * All the messages from the server to the client implement this interface.
 * It contains methods that can only be used in the client
 */
public interface ClientMessage {

    static ClientMessage deserialize(String jsonString) {
        RuntimeTypeAdapterFactory<ClientMessage> jsonToClientAdapter = RuntimeTypeAdapterFactory.of(ClientMessage.class);

        //Register here all the message types
        jsonToClientAdapter.registerSubtype(CreatedMatchStatus.class);
        jsonToClientAdapter.registerSubtype(JoinStatus.class);
        jsonToClientAdapter.registerSubtype(MatchesData.class);
        jsonToClientAdapter.registerSubtype(StateMessage.class);

        Gson gson1 = new GsonBuilder()
                .registerTypeAdapterFactory(jsonToClientAdapter)
                .create();

        return deserializeFromString(jsonString, ClientMessage.class, gson1);
    }

    /**
     * Method invoked in the client to process the message.
     */
    void processMessage(ServerHandler serverHandler) throws IOException;
}
