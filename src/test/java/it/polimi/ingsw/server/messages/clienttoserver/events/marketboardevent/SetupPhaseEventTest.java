package it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent;
import com.google.gson.Gson;
import it.polimi.ingsw.network.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import it.polimi.ingsw.server.model.GameModel;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static it.polimi.ingsw.network.jsonUtils.JsonUtility.deserializeFromString;
import static it.polimi.ingsw.network.jsonUtils.JsonUtility.serialize;
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
        List<String> players = new ArrayList<>();
        players.add("testPlayer1");
        players.add("testPlayer2");
        players.add("testPlayer3");

        initializeGameModel(players);
        gameModelTest.setCurrentPlayer(gameModelTest.getPlayer("testPlayer3"));
        gson = new Gson();
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

        initializeEvent(2,initialResources,initialDiscardedLeaders);
    }


    private void anotherValidTestInitialization(){


        initialResources = new ArrayList<>();
        initialResources.add(new Pair<>(4, 2));

        initialDiscardedLeaders = new ArrayList<>(2);
        initialDiscardedLeaders.add(playerLeadersUUIDs.get(2));
        initialDiscardedLeaders.add(playerLeadersUUIDs.get(3));

        initializeEvent(2,initialResources,initialDiscardedLeaders);

    }


    private void invalidResourcesTestInitialization(){

        List<String> players = new ArrayList<>();
        players.add("testPlayer1");
        players.add("testPlayer2");
        players.add("testPlayer3");
        players.add("testPlayer4");
        initializeGameModel(players);
        gameModelTest.setCurrentPlayer(gameModelTest.getPlayer("testPlayer3"));
        playerLeadersUUIDs = gameModelTest.getCurrentPlayer().getLeadersUUIDs();

        initialResources = new ArrayList<>();
        initialResources.add(new Pair<>(1, 3));
        initialResources.add(new Pair<>(2, 2)); //different resource same deposit in warehouse --> validation fail

        initialDiscardedLeaders = new ArrayList<>(2);
        initialDiscardedLeaders.add(playerLeadersUUIDs.get(2));
        initialDiscardedLeaders.add(playerLeadersUUIDs.get(3));

        initializeEvent(4,initialResources,initialDiscardedLeaders);
    }

    private void invalidLeadersTestInitialization(){


        initialResources = new ArrayList<>();
        initialResources.add(new Pair<>(1, 3));
        initialResources.add(new Pair<>(2, 3)); //different resource same deposit in warehouse --> validation fail

        initialDiscardedLeaders = new ArrayList<>(2);
        initialDiscardedLeaders.add(gameModelTest.getRemainingLeadersUUIDs().get(1));
        initialDiscardedLeaders.add(playerLeadersUUIDs.get(3));

        initializeEvent(4,initialResources,initialDiscardedLeaders);

    }

    private void initializeEvent(int playerNumber,  List<Pair<Integer, Integer>> initialResources, List<UUID> initialDiscardedLeaders){
        clientEventTest = new SetupPhaseEvent(initialResources.size(), initialDiscardedLeaders.size(), playerNumber);


        for (Pair<Integer, Integer> initialResource : initialResources) {
            clientEventTest.addResource(initialResource);
        }

        for (UUID initialDiscardedLeader : initialDiscardedLeaders) {
            clientEventTest.addDiscardedLeader(initialDiscardedLeader);
        }
    }

    private void initializeGameModel(List<String> players){
        gameModelTest = new GameModel(players, false, null);
        gameModelTest.setGameStatus(true);
    }



}