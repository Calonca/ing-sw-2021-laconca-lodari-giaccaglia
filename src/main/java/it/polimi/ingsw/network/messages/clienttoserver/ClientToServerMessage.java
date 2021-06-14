package it.polimi.ingsw.network.messages.clienttoserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.messages.NetworkMessage;
import it.polimi.ingsw.server.utils.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.network.messages.clienttoserver.events.*;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.*;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.ChooseLineEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.ChooseWhiteMarbleConversionEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.DiscardResourcesEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.MarketBoardEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ToggleProductionAtPosition;
import it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ChooseResourcesForProductionEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.FinalProductionPhaseEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.MoveResourceEvent;


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

        eventMessageAdapter.registerSubtype(ChooseCardEvent.class);
        eventMessageAdapter.registerSubtype(ChooseCardPositionEvent.class);
        eventMessageAdapter.registerSubtype(ChooseResourceForCardShopEvent.class);
        eventMessageAdapter.registerSubtype(MoveResourceEvent.class);

        eventMessageAdapter.registerSubtype(ChooseLineEvent.class);
        eventMessageAdapter.registerSubtype(ChooseWhiteMarbleConversionEvent.class);
        eventMessageAdapter.registerSubtype(DiscardResourcesEvent.class);
        eventMessageAdapter.registerSubtype(MarketBoardEvent.class);

        eventMessageAdapter.registerSubtype(ToggleProductionAtPosition.class);
        eventMessageAdapter.registerSubtype(ChooseResourcesForProductionEvent.class);
        eventMessageAdapter.registerSubtype(FinalProductionPhaseEvent.class);

        eventMessageAdapter.registerSubtype(SetupPhaseEvent.class);

        eventMessageAdapter.registerSubtype(EndMiddlePhaseEvent.class);
        eventMessageAdapter.registerSubtype(InitialOrFinalPhaseEvent.class);
        eventMessageAdapter.registerSubtype(MiddlePhaseEvent.class);

        return eventMessageAdapter;
    }
}
