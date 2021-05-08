package it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent;
import com.google.gson.Gson;
import it.polimi.ingsw.network.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import it.polimi.ingsw.server.model.GameModel;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.server.model.utils.JsonUtility.deserializeFromString;
import static it.polimi.ingsw.server.model.utils.JsonUtility.serialize;
import static org.junit.Assert.*;

public class SetupPhaseEventTest {

    SetupPhaseEvent clientEventTest;
    it.polimi.ingsw.server.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent serverEventTest;
    GameModel gameModelTest;
    List<Pair<Integer, Integer>> initialResources;
    List<Integer> initialChosenLeaders;
    List<Integer> initialDiscardedLeaders;
    String serializedEvent;
    Gson gson;

    @Before
    public void setup(){
        initializeGameModel();
        gameModelTest.setCurrentPlayer(gameModelTest.getPlayer("testPlayer4"));
        gson = new Gson();
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
        initialResources.add(new Pair<>(2, 3));

        initialChosenLeaders = new ArrayList<>(2);
        initialChosenLeaders.add(0);
        initialChosenLeaders.add(2);

        initialDiscardedLeaders = new ArrayList<>(2);
        initialDiscardedLeaders.add(1);
        initialDiscardedLeaders.add(3);

        initializeEvent(4,initialResources,initialChosenLeaders,initialDiscardedLeaders);
    }


    private void anotherValidTestInitialization(){


        initialResources = new ArrayList<>();
        initialResources.add(new Pair<>(4, 2));
        initialResources.add(new Pair<>(5, 2));

        initialChosenLeaders = new ArrayList<>(2);
        initialChosenLeaders.add(0);
        initialChosenLeaders.add(2);

        initialDiscardedLeaders = new ArrayList<>(2);
        initialDiscardedLeaders.add(1);
        initialDiscardedLeaders.add(3);

        initializeEvent(4,initialResources,initialChosenLeaders,initialDiscardedLeaders);

    }


    private void invalidResourcesTestInitialization(){

        initialResources = new ArrayList<>();
        initialResources.add(new Pair<>(1, 3));
        initialResources.add(new Pair<>(2, 2)); //different resource same deposit in warehouse --> validation fail

        initialChosenLeaders = new ArrayList<>(2);
        initialChosenLeaders.add(0);
        initialChosenLeaders.add(2);

        initialDiscardedLeaders = new ArrayList<>(2);
        initialDiscardedLeaders.add(1);
        initialDiscardedLeaders.add(3);

        initializeEvent(4,initialResources,initialChosenLeaders,initialDiscardedLeaders);

    }

    private void invalidLeadersTestInitialization(){

        initialResources = new ArrayList<>();
        initialResources.add(new Pair<>(1, 3));
        initialResources.add(new Pair<>(2, 3)); //different resource same deposit in warehouse --> validation fail

        initialChosenLeaders = new ArrayList<>(2);
        initialChosenLeaders.add(0);
        initialChosenLeaders.add(2);

        initialDiscardedLeaders = new ArrayList<>(2);
        initialDiscardedLeaders.add(1);
        initialDiscardedLeaders.add(3);

        gameModelTest.getLeader(0);
        gameModelTest.getLeader(2);

        initializeEvent(4,initialResources,initialChosenLeaders,initialDiscardedLeaders);

    }



    private void initializeEvent(int playerNumber,  List<Pair<Integer, Integer>> initialResources, List<Integer> initialChosenLeaders, List<Integer> initialDiscardedLeaders){
        clientEventTest = new SetupPhaseEvent(initialResources.size(), initialChosenLeaders.size(), initialDiscardedLeaders.size(), playerNumber);


        for (Pair<Integer, Integer> initialResource : initialResources) {
            clientEventTest.addResource(initialResource);
        }


        for (Integer initialChosenLeader : initialChosenLeaders) {
            clientEventTest.addChosenLeader(initialChosenLeader);
        }

        for (Integer initialDiscardedLeader : initialDiscardedLeaders) {
            clientEventTest.addDiscardedLeader(initialDiscardedLeader);
        }
    }

    private void initializeGameModel(){
        List<String> players = new ArrayList<>();
        players.add("testPlayer1");
        players.add("testPlayer2");
        players.add("testPlayer3");
        players.add("testPlayer4");

        gameModelTest = new GameModel(players, false, null);
        gameModelTest.setGameStatus(true);
    }


}