package it.polimi.ingsw.server.messages.clienttoserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.server.messages.clienttoserver.events.*;
import it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.CardShopEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardPositionEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseResourceForCardShopEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent.*;
import it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.*;
import it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.ChooseResourcesForProductionEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.FinalProductionPhase;
import it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.ProductionEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;

import java.io.IOException;

import static it.polimi.ingsw.network.jsonUtils.JsonUtility.deserializeFromString;

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

        eventMessageAdapter.registerSubtype(TestEvent.class);

        eventMessageAdapter.registerSubtype(MiddlePhaseEvent.class);
        eventMessageAdapter.registerSubtype(InitialOrFinalPhaseEvent.class);
        eventMessageAdapter.registerSubtype(EndMiddlePhaseEvent.class);

        eventMessageAdapter.registerSubtype(SetupPhaseEvent.class);

        eventMessageAdapter.registerSubtype(ChooseCardPositionEvent.class);
        eventMessageAdapter.registerSubtype(ChooseResourcesForProductionEvent.class);
        eventMessageAdapter.registerSubtype(FinalProductionPhase.class);
        eventMessageAdapter.registerSubtype(ProductionEvent.class);

        eventMessageAdapter.registerSubtype(ChooseLineEvent.class);
        eventMessageAdapter.registerSubtype(ChooseWhiteMarbleConversionEvent.class);
        eventMessageAdapter.registerSubtype(DiscardResourcesEvent.class);
        eventMessageAdapter.registerSubtype(MarketBoardEvent.class);
        eventMessageAdapter.registerSubtype(MoveResourceEvent.class);

        eventMessageAdapter.registerSubtype(ChooseLeaderEvent.class);
        eventMessageAdapter.registerSubtype(DiscardLeaderEvent.class);
        eventMessageAdapter.registerSubtype(PlayLeaderEvent.class);
        eventMessageAdapter.registerSubtype(SkipLeaderEvent.class);

        eventMessageAdapter.registerSubtype(CardShopEvent.class);
        eventMessageAdapter.registerSubtype(ChooseCardEvent.class);
        eventMessageAdapter.registerSubtype(ChooseCardPositionEvent.class);
        eventMessageAdapter.registerSubtype(ChooseResourceForCardShopEvent.class);



        return eventMessageAdapter;
    }
}
