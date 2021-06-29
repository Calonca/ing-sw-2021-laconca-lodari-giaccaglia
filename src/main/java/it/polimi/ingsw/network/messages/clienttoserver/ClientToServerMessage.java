package it.polimi.ingsw.network.messages.clienttoserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.jsonUtils.CommonGsonAdapters;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.messages.NetworkMessage;



/**
 * A message sent to the server.
 * Can have response or not.
 * Contains common methods between server and client
 */
public abstract class ClientToServerMessage extends NetworkMessage
{
    public String serialized()
    {

        Gson gson1 = new GsonBuilder()
                .registerTypeAdapterFactory(CommonGsonAdapters.gsonClientToServerMessageAdapter)
                .registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter)
                .create();

        return JsonUtility.serialize(this,ClientToServerMessage.class, gson1);

    }


}
