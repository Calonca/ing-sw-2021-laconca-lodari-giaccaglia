package it.polimi.ingsw.server.controller.strategy.resourcemarket;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.jsonUtils.CommonGsonAdapters;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.ChooseWhiteMarbleConversionEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ToggleProductionAtPosition;
import it.polimi.ingsw.server.controller.strategy.production.TogglingForProduction;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.market.MarketLine;
import it.polimi.ingsw.server.model.states.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ChoosingMarketBonusTest {


    boolean oneMarbleWay=false;
    boolean moreThanOneMarbleWay=false;
    //we cannot know what are the lines which contain more than two white marble, and we
    //do not think it's worth making a stochastic test
    public void execute() {

        Event clientEvent = new ChooseWhiteMarbleConversionEvent(1);
        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.ChooseWhiteMarbleConversionEvent.class, gson);
        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        boolean isSinglePlayer = true;
        GameModel gamemodel = new GameModel(players, isSinglePlayer,null, onlineUsers);


        //todo THIS IS A RANDOM TEST. fix
        gamemodel.chooseLineFromMarketBoard(MarketLine.FIRST_COLUMN);

        if(gamemodel.getNumberOfWhiteMarblesInPickedLine()>1)
        {
            assertEquals(new ChoosingMarketBonus().execute(gamemodel, serverEvent).getKey(), State.CHOOSING_WHITEMARBLE_CONVERSION);
            moreThanOneMarbleWay=true;
        }
        else
        {
            assertEquals(new ChoosingMarketBonus().execute(gamemodel, serverEvent).getKey(), State.CHOOSING_POSITION_FOR_RESOURCES);
            oneMarbleWay=true;
        }
    }


    @Test
    public void executeTest() {

        do {
            execute();
        }while (!(oneMarbleWay&&moreThanOneMarbleWay));
    }


}