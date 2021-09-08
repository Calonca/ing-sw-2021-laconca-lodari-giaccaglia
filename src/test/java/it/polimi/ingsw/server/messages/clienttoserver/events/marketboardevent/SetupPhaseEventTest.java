package it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent;
import com.google.gson.Gson;
import it.polimi.ingsw.network.jsonutils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import it.polimi.ingsw.server.model.GameModel;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static it.polimi.ingsw.network.jsonutils.JsonUtility.deserializeFromString;
import static it.polimi.ingsw.network.jsonutils.JsonUtility.serialize;
import static org.junit.Assert.*;

public class SetupPhaseEventTest {

    SetupPhaseEvent clientEventTest;
    it.polimi.ingsw.server.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent serverEventTest;
    GameModel gameModelTest;
    List<Pair<Integer, Integer>> initialResources;

    List<UUID> initialDiscardedLeaders;
    List<UUID> playerLeadersUUIDs;
    String serializedEvent;
    Gson gson;

    @Before
    public void setup(){
        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        players.put(1,"testPlayer2");

        initializeGameModel(players);
        gameModelTest.setCurrentPlayer(gameModelTest.getPlayer("testPlayer2").get());
        gson = JsonUtility.customGson;
        playerLeadersUUIDs = gameModelTest.getCurrentPlayer().getLeadersUUIDs();
    }

    @Test
    public void validationOkTest(){

       validTestInitialization();
       serializedEvent = serialize(clientEventTest);
       serverEventTest = deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent.class , gson);
       assertTrue(serverEventTest.validate(gameModelTest));

    }

    @Test
    public void anotherValidationOkTest(){

        anotherValidTestInitialization();
        serializedEvent = serialize(clientEventTest);
        serverEventTest = deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent.class , gson);
        assertTrue(serverEventTest.validate(gameModelTest));

    }

    @Test
    public void validationNotOkResourcesTest(){
        invalidResourcesTestInitialization();
        serializedEvent = serialize(clientEventTest);
        serverEventTest = deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent.class , gson);
        assertFalse(serverEventTest.validate(gameModelTest));

    }

    @Test
    public void validationNotOkLeadersTest(){
        invalidLeadersTestInitialization();
        serializedEvent = serialize(clientEventTest);
        serverEventTest = deserializeFromString(serializedEvent, it.polimi.ingsw.server.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent.class , gson);
        assertFalse(serverEventTest.validate(gameModelTest));

    }

    private void validTestInitialization(){

        initialResources = new ArrayList<>();
        initialResources.add(new Pair<>(1, 3));


        initialDiscardedLeaders = new ArrayList<>(2);

        initialDiscardedLeaders.add(playerLeadersUUIDs.get(2));
        initialDiscardedLeaders.add(playerLeadersUUIDs.get(3));

        initializeEvent(1,initialResources,initialDiscardedLeaders);
    }


    private void anotherValidTestInitialization(){

        initialResources = new ArrayList<>();
        initialResources.add(new Pair<>(4, 2));

        initialDiscardedLeaders = new ArrayList<>(2);
        initialDiscardedLeaders.add(playerLeadersUUIDs.get(2));
        initialDiscardedLeaders.add(playerLeadersUUIDs.get(3));

        initializeEvent(1,initialResources,initialDiscardedLeaders);

    }


    private void invalidResourcesTestInitialization(){

        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        players.put(1,"testPlayer2");
        players.put(2,"testPlayer3");
        players.put(3,"testPlayer4");
        initializeGameModel(players);
        gameModelTest.setCurrentPlayer(gameModelTest.getPlayer("testPlayer3").get());
        playerLeadersUUIDs = gameModelTest.getCurrentPlayer().getLeadersUUIDs();

        initialResources = new ArrayList<>();
        initialResources.add(new Pair<>(1, 3));
        initialResources.add(new Pair<>(2, 2)); //different resource same deposit in warehouse --> validation fail

        initialDiscardedLeaders = new ArrayList<>(2);
        initialDiscardedLeaders.add(playerLeadersUUIDs.get(2));
        initialDiscardedLeaders.add(playerLeadersUUIDs.get(3));

        initializeEvent(3,initialResources,initialDiscardedLeaders);
    }

    private void invalidLeadersTestInitialization(){

        initialResources = new ArrayList<>();
        initialResources.add(new Pair<>(1, 3));
        initialResources.add(new Pair<>(2, 3)); //different resource same deposit in warehouse --> validation fail

        initialDiscardedLeaders = new ArrayList<>(2);
        initialDiscardedLeaders.add(gameModelTest.getRemainingLeadersUUIDs().get(1));
        initialDiscardedLeaders.add(playerLeadersUUIDs.get(3));
        initializeEvent(3,initialResources,initialDiscardedLeaders);

    }

    private void initializeEvent(int playerNumber,  List<Pair<Integer, Integer>> initialResources, List<UUID> initialDiscardedLeaders){
        clientEventTest = new SetupPhaseEvent(initialResources.size(), initialDiscardedLeaders.size(), playerNumber);


        for (Pair<Integer, Integer> initialResource : initialResources) {
            clientEventTest.addResource(initialResource);
        }

        for (UUID initialDiscardedLeader : initialDiscardedLeaders) {
            clientEventTest.addChosenLeader(initialDiscardedLeader);
        }
    }

    private void initializeGameModel(Map<Integer, String> players){
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());
        gameModelTest = new GameModel(players, false, null, onlineUsers);
        gameModelTest.start();
    }



}