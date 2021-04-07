package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.market.Marble;
import org.junit.Test;

import static org.junit.Assert.*;

public class MarbleTest {

    @Test
    public void getConvertedMarble() {
        assertEquals(Marble.valueOf("WHITE").getConvertedMarble(), Resource.EMPTY );
        assertEquals(Marble.valueOf("BLUE").getConvertedMarble(), Resource.SHIELD );
        assertEquals(Marble.valueOf("GRAY").getConvertedMarble(), Resource.STONE);
        assertEquals(Marble.valueOf("YELLOW").getConvertedMarble(), Resource.GOLD);
        assertEquals(Marble.valueOf("PURPLE").getConvertedMarble(), Resource.SERVANT);
        assertEquals(Marble.valueOf("RED").getConvertedMarble(), Resource.FAITH);
    }

    @Test
    public void valueOf() {
        assertNotNull(Marble.valueOf("WHITE"));
        assertNotNull(Marble.valueOf("BLUE"));
        assertNotNull(Marble.valueOf("GRAY"));
        assertNotNull(Marble.valueOf("YELLOW"));
        assertNotNull(Marble.valueOf("PURPLE"));
        assertNotNull(Marble.valueOf("RED"));
    }
}