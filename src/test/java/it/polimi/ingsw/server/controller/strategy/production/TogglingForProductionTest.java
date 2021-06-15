package it.polimi.ingsw.server.controller.strategy.production;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.jsonUtils.CommonGsonAdapters;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ToggleProductionAtPosition;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TogglingForProductionTest {

    @Test
    public void execute() {

        Event clientEvent = new ToggleProductionAtPosition(2);
        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.ToggleProductionAtPosition.class, gson);
        List<String> nicknames = new ArrayList<>();
        nicknames.add("testPlayer");

        boolean isSinglePlayer = true;
        GameModel gamemodel = new GameModel(nicknames, isSinglePlayer,null);

       // assertEquals(new TogglingForProduction().execute(gamemodel, serverEvent).getKey(), State.CHOOSING_PRODUCTION);

    }

}