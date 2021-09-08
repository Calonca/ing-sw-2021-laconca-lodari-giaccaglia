package it.polimi.ingsw.server.controller.strategy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.jsonutils.CommonGsonAdapters;
import it.polimi.ingsw.network.jsonutils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SetupTest {
    @Test
    public void execute() {

        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        GameModel gamemodel = new GameModel(players, true ,null, onlineUsers);

        SetupPhaseEvent clientEvent = new SetupPhaseEvent(1,2, 0);

        clientEvent.addChosenLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(0));
        clientEvent.addChosenLeader(gamemodel.getCurrentPlayer().getLeadersUUIDs().get(1));

        Pair<Integer, Integer> toAdd;
        toAdd=new Pair<>(1,0);
        clientEvent.addResource(toAdd);
        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent.class, gson);



        assertEquals(new Setup().execute(gamemodel, serverEvent).getKey(), State.IDLE);
        assertEquals(gamemodel.getCurrentPlayer().getPersonalBoard().getWarehouseLeadersDepots().getNumberOf(Resource.GOLD), 1);

    }

}