package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.jsonutils.CommonGsonAdapters;
import it.polimi.ingsw.network.jsonutils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.controller.Match;
import it.polimi.ingsw.server.controller.strategy.production.ChoosingResourceForProduction;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.states.State;
import it.polimi.ingsw.server.utils.Deserializator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class productionCardTest {
    @Test
    public void executeInputResource() {

        List<DevelopmentCard> cards = Deserializator.devCardsListDeserialization();

        List<String> devcardS = cards.stream().map(c->c.getCardId().toString()).collect(Collectors.toList());
        System.out.println(devcardS);
        DevelopmentCard green1 = cards.stream().filter(c->c.getCardId().toString().equals("f5d3858f-4ca2-3e84-a7aa-7a9cc95ea6b2")).findFirst().orElseThrow();
        assertArrayEquals(new int[]{0,0,0,1,0,0,0},green1.getProduction().getInputs());

        List<Integer> input= new ArrayList<>();
        input.add(-8);
        input.add(-7);
        input.add(-6);
        input.add(-5);
        Event clientEvent = new it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ChooseResourcesForProductionEvent(input, new ArrayList<>());

        String serializedEvent = JsonUtility.serialize(clientEvent);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CommonGsonAdapters.gsonEventMessageAdapter).create();

        Validable serverEvent = JsonUtility.deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.ChooseResourcesForProductionEvent.class, gson);
        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        Match match= new Match(1);
        GameModel gamemodel = new GameModel(players, true ,match, onlineUsers);

        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(new int[]{10,10,10,10});


        assertEquals(new ChoosingResourceForProduction().execute(gamemodel, serverEvent).getKey(), State.CHOOSING_PRODUCTION);
        assertEquals(gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().getNSelected(Resource.GOLD),1);

    }
}
