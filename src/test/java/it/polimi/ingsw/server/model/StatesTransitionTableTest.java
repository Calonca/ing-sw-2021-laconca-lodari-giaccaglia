package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.states.State;
import it.polimi.ingsw.server.model.states.StatesTransitionTable;
import it.polimi.ingsw.server.controller.strategy.Setup;
import it.polimi.ingsw.server.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StatesTransitionTableTest {
    public StatesTransitionTable singlePlayerTable, multiPlayerTable;

    @Before
    public void setUp() throws Exception {
        serializeTables();
        singlePlayerTable = StatesTransitionTable.singlePlayer();
        multiPlayerTable = StatesTransitionTable.multiPlayer();
    }

    @Test
    public void testSerialization(){
        assertEquals(Setup.class, multiPlayerTable.getStrategy(State.SETUP_PHASE, new SetupPhaseEvent()).getClass());
        assertEquals(Setup.class, singlePlayerTable.getStrategy(State.SETUP_PHASE, new SetupPhaseEvent()).getClass());
    }

    public void serializeTables(){
        StatesTransitionTable.saveTables();
    }

}