package it.polimi.ingsw.server.model.player;

import org.junit.Test;

import static org.junit.Assert.*;

public class LeaderStateTest {

    @Test
    public void valueOf() {
        assertNotNull(LeaderState.valueOf("ACTIVE"));
        assertNotNull(LeaderState.valueOf("INACTIVE"));
        assertNotNull(LeaderState.valueOf("DISCARDED"));
    }
}