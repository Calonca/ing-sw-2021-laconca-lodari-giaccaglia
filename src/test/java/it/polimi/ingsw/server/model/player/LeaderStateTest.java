package it.polimi.ingsw.server.model.player;

import org.junit.Test;

import static org.junit.Assert.*;

public class LeaderStateTest {

    @Test
    public void valueOf() {
        assertNotNull(TileState.valueOf("ACTIVE"));
        assertNotNull(TileState.valueOf("INACTIVE"));
        assertNotNull(TileState.valueOf("DISCARDED"));
    }
}