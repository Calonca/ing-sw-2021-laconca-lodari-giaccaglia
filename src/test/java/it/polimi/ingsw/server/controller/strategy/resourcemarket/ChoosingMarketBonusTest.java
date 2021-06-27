package it.polimi.ingsw.server.controller.strategy.resourcemarket;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.network.jsonUtils.CommonGsonAdapters;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.ChooseWhiteMarbleConversionEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ToggleProductionAtPosition;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ChoosingMarketBonusTest {

    @Test
    public void execute() {


        ///assertEquals(new ChoosingMarketBonus().execute(gamemodel, serverEvent).getKey(), State.CHOOSING_PRODUCTION);

    }

}