package it.polimi.ingsw.server.model.player.track;

import it.polimi.ingsw.server.model.player.track.TileState;
import org.junit.Test;

import static org.junit.Assert.*;

public class TileStateTest {

    @Test
    public void valueOf() {
        assertNotNull(TileState.valueOf("ACTIVE"));
        assertNotNull(TileState.valueOf("INACTIVE"));
        assertNotNull(TileState.valueOf("DISCARDED"));
    }
}