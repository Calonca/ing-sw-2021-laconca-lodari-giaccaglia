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

        RuntimeTypeAdapterFactory<ClientToServerMessage> shapeAdapterFactory = RuntimeTypeAdapterFactory.of(ClientToServerMessage.class);


        shapeAdapterFactory.registerSubtype(CreateMatchRequest.class);

        Gson gson1 = new GsonBuilder()
                .registerTypeAdapterFactory(shapeAdapterFactory)
                .create();

        return  gson1.toJson(this,ClientToServerMessage.class);

    }

}
