package it.polimi.ingsw.server.model.player.leaders;

import it.polimi.ingsw.server.model.player.leaders.LeaderState;
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