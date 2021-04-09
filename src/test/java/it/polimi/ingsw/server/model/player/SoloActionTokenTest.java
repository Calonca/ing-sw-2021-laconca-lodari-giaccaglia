package it.polimi.ingsw.server.model.player;
import org.junit.Test;

import static org.junit.Assert.*;

public class SoloActionTokenTest {


    //effects are tested in SinglePlayerDeckTest

    @Test
    public void values() {

        assertNotNull(SoloActionToken.valueOf("DISCARD2GREEN"));
        assertNotNull(SoloActionToken.valueOf("DISCARD2BLUE"));
        assertNotNull(SoloActionToken.valueOf("DISCARD2YELLOW"));
        assertNotNull(SoloActionToken.valueOf("DISCARD2PURPLE"));
        assertNotNull(SoloActionToken.valueOf("SHUFFLE_ADD1FAITH"));
        assertNotNull(SoloActionToken.valueOf("ADD2FAITH"));
    }

}