package it.polimi.ingsw.network.messages.clienttoserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.messages.NetworkMessage;
import it.polimi.ingsw.RuntimeTypeAdapterFactory;

/**
 * A message sent to the server.
 * Cam have response or not.
 * Contains common methods between server and client
 */
public abstract class ClientToServerMessage extends NetworkMessage
{

    public String serialized()
    {

        RuntimeTypeAdapterFactory<ClientToServerMessage> clientToJsonAdapter = RuntimeTypeAdapterFactory.of(ClientToServerMessage.class);


        clientToJsonAdapter.registerSubtype(CreateMatchRequest.class);
        clientToJsonAdapter.registerSubtype(JoinMatchRequest.class);

        Gson gson1 = new GsonBuilder()
                .registerTypeAdapterFactory(clientToJsonAdapter)
                .create();

        return  gson1.toJson(this,ClientToServerMessage.class);

    }

}
