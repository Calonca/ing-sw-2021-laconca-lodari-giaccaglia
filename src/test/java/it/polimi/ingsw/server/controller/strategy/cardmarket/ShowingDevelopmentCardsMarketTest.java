package it.polimi.ingsw.server.controller.strategy.cardmarket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseCardEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ChooseProductionAtPositionEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ShowingDevelopmentCardsMarketTest {

    @Test
    public void execute()
    {

        Event clientEvent = new ChooseCardEvent(2,2);
        String serializedEvent = JsonUtility.serialize(clientEvent);

        RuntimeTypeAdapterFactory<Event> adapter = ClientToServerMessage.eventMessageAdapter();
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(adapter).create();

        Event serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardEvent.class, gson);

        List<String> nicknames = new ArrayList<>();
        nicknames.add("testPlayer");
        boolean isSinglePlayer = true;
        GameModel gamemodel = new GameModel(nicknames, isSinglePlayer,null);

        assertEquals(State.CHOOSING_RESOURCES_FOR_DEVCARD, new AcquiringDevelopmentCard().execute(gamemodel, serverEvent).getKey());

    }
}