package it.polimi.ingsw.server.messages.clienttoserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.RuntimeTypeAdapterFactory;

import java.io.IOException;

import static it.polimi.ingsw.server.model.jsonUtility.deserializeFromString;

/**
 * All the messages from the client to the server implement this interface.
 * It contains methods that can only be used in the server.
 */
public interface ServerMessage {
    void processMessage(ClientHandler clientHandler) throws IOException;

    static ServerMessage deserialize(String jsonString){
        RuntimeTypeAdapterFactory<ServerMessage> jsonToServerAdapter = RuntimeTypeAdapterFactory.of(ServerMessage.class);


        jsonToServerAdapter.registerSubtype(CreateMatchRequest.class);
        jsonToServerAdapter.registerSubtype(JoinMatchRequest.class);

        Gson gson1 = new GsonBuilder()
                .registerTypeAdapterFactory(jsonToServerAdapter)
                .create();

        return deserializeFromString(jsonString, ServerMessage.class,gson1);
    }
}
