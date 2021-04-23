package it.polimi.ingsw.server.messages.clienttoserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.RuntimeTypeAdapterFactory;

import java.io.IOException;

/**
 * All the messages from the client to the server implement this interface.
 * It contains methods that can only be used in the server.
 */
public interface ServerMessage {
    void processMessage(ClientHandler clientHandler) throws IOException;

    static ServerMessage deserialize(String jsonString){
        RuntimeTypeAdapterFactory<ServerMessage> shapeAdapterFactory = RuntimeTypeAdapterFactory.of(ServerMessage.class);


        shapeAdapterFactory.registerSubtype(CreateMatchRequest.class);

        Gson gson1 = new GsonBuilder()
                .registerTypeAdapterFactory(shapeAdapterFactory)
                .create();

        return gson1.fromJson(jsonString, ServerMessage.class);
    }
}
