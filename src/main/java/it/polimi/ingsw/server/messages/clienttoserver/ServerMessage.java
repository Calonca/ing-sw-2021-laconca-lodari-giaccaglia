package it.polimi.ingsw.server.messages.clienttoserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.server.messages.clienttoserver.events.TestEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;

import java.io.IOException;

import static it.polimi.ingsw.server.model.utils.JsonUtility.deserializeFromString;

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
        jsonToServerAdapter.registerSubtype(EventMessage.class);

        Gson gson1 = new GsonBuilder()
                .registerTypeAdapterFactory(jsonToServerAdapter)
                .registerTypeAdapterFactory(eventMessageAdapter())
                .create();
        return deserializeFromString(jsonString, ServerMessage.class,gson1);
    }

    static RuntimeTypeAdapterFactory<Validable> eventMessageAdapter(){
        RuntimeTypeAdapterFactory<Validable> eventMessageAdapter = RuntimeTypeAdapterFactory.of(Validable.class);

        //Todo Register here all the event message types in the server
        eventMessageAdapter.registerSubtype(TestEvent.class);
        return eventMessageAdapter;
    }
}
