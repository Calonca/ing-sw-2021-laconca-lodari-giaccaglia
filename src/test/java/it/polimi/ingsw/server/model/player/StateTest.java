package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.controller.states.State;
import org.junit.Test;

import static org.junit.Assert.*;

public class StateTest {

    @Test
    public void valueOf() {
        assertNotNull(State.valueOf("INITIAL_PHASE"));
        assertNotNull(State.valueOf("SHOWING_LEADERS_INITIAL"));
        assertNotNull(State.valueOf("MIDDLE_PHASE"));
        assertNotNull(State.valueOf("CHOOSING_CARD_FOR_PRODUCTION"));
        assertNotNull(State.valueOf("CHOOSING_RESOURCE_FOR_PRODUCTION"));
        assertNotNull(State.valueOf("SHOWING_MARKET_RESOURCES"));
        assertNotNull(State.valueOf("CHOOSING_WHITEMARBLE_CONVERSION"));
        assertNotNull(State.valueOf("CHOOSING_POSITION_FOR_RESOURCES"));
        assertNotNull(State.valueOf("SHOWING_CARD_SHOP"));
        assertNotNull(State.valueOf("CHOOSING_DEVELOPMENT_CARD"));
        assertNotNull(State.valueOf("FINAL_PHASE"));
        assertNotNull(State.valueOf("SHOWING_LEADERS_FINAL"));
        assertNotNull(State.valueOf("IDLE"));
        assertNotNull(State.valueOf("WINNING_STATE"));
        assertNotNull(State.valueOf("LOSING_STATE"));
    }
}