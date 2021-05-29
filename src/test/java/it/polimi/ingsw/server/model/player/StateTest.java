package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.states.State;
import org.junit.Test;

import static org.junit.Assert.*;

public class StateTest {

    @Test
    public void valueOf() {

        assertNotNull(State.valueOf("SETUP_PHASE"));
        assertNotNull(State.valueOf("INITIAL_PHASE"));
        assertNotNull(State.valueOf("MIDDLE_PHASE"));

        assertNotNull(State.valueOf("CHOOSING_PRODUCTION"));
        assertNotNull(State.valueOf("CHOOSING_MARKET_LINE"));
        assertNotNull(State.valueOf("CHOOSING_RESOURCE_FOR_PRODUCTION"));
        assertNotNull(State.valueOf("CHOOSING_RESOURCES_FOR_DEVCARD"));
        assertNotNull(State.valueOf("CHOOSING_POSITION_FOR_DEVCARD"));
        assertNotNull(State.valueOf("CHOOSING_WHITEMARBLE_CONVERSION"));
        assertNotNull(State.valueOf("CHOOSING_POSITION_FOR_RESOURCES"));
        assertNotNull(State.valueOf("CHOOSING_DEVELOPMENT_CARD"));
        assertNotNull(State.valueOf("FINAL_PHASE"));
        assertNotNull(State.valueOf("IDLE"));
        assertNotNull(State.valueOf("END_PHASE"));
    }
}