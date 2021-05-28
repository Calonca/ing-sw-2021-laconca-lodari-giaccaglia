package it.polimi.ingsw.network.messages.clienttoserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.messages.NetworkMessage;
import it.polimi.ingsw.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.leaderphaseevent.DiscardLeaderEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.leaderphaseevent.PlayLeaderEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.leaderphaseevent.SkipLeaderEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;

/**
 * A message sent to the server.
 * Can have response or not.
 * Contains common methods between server and client
 */
public abstract class ClientToServerMessage extends NetworkMessage
{
    public String serialized()
    {

        RuntimeTypeAdapterFactory<ClientToServerMessage> clientToJsonAdapter = RuntimeTypeAdapterFactory.of(ClientToServerMessage.class);

        clientToJsonAdapter.registerSubtype(CreateMatchRequest.class);
        clientToJsonAdapter.registerSubtype(JoinMatchRequest.class);
        clientToJsonAdapter.registerSubtype(EventMessage.class);

        Gson gson1 = new GsonBuilder()
                .registerTypeAdapterFactory(clientToJsonAdapter)
                .registerTypeAdapterFactory(eventMessageAdapter())
                .create();

        return JsonUtility.serialize(this,ClientToServerMessage.class, gson1);

    }

    public static RuntimeTypeAdapterFactory<Event> eventMessageAdapter(){
        RuntimeTypeAdapterFactory<Event> eventMessageAdapter = RuntimeTypeAdapterFactory.of(Event.class);

        //Todo Register here all the event message types in the server
        eventMessageAdapter.registerSubtype(SetupPhaseEvent.class);
        eventMessageAdapter.registerSubtype(PlayLeaderEvent.class);
        eventMessageAdapter.registerSubtype(DiscardLeaderEvent.class);
        eventMessageAdapter.registerSubtype(SkipLeaderEvent.class);

        return eventMessageAdapter;
    }
}
