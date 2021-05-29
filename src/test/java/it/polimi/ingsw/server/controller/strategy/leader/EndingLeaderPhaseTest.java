package it.polimi.ingsw.server.controller.strategy.leader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseCardEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.leaderphaseevent.SkipLeaderEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.InitialOrFinalPhaseEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class EndingLeaderPhaseTest {

    @Test
    public void execute() {


        Event clientEvent = new SkipLeaderEvent();
        String serializedEvent = JsonUtility.serialize(clientEvent);

        RuntimeTypeAdapterFactory<Event> adapter = ClientToServerMessage.eventMessageAdapter();
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(adapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent.SkipLeaderEvent.class, gson);

        List<String> nicknames = new ArrayList<>();
        nicknames.add("testPlayer");
        boolean isSinglePlayer = true;
        GameModel gamemodel = new GameModel(nicknames, isSinglePlayer,null);
        gamemodel.getCurrentPlayer().setCurrentState(State.INITIAL_PHASE);

        assertEquals(State.INITIAL_PHASE, new EndingLeaderPhase().execute(gamemodel, serverEvent).getKey() );

        gamemodel.getCurrentPlayer().setCurrentState(State.FINAL_PHASE);

        assertEquals(State.FINAL_PHASE,new EndingLeaderPhase().execute(gamemodel,serverEvent).getKey() );
    }
}