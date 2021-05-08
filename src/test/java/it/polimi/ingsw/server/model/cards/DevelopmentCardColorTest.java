package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.network.assets.devcards.DevelopmentCardColor;
import org.junit.Test;

import static org.junit.Assert.*;

public class DevelopmentCardColorTest {

    @Test
    public void valueOf()
    {
        assertEquals(DevelopmentCardColor.fromInt(0),DevelopmentCardColor.values()[0]);
        assertEquals(DevelopmentCardColor.fromInt(1),DevelopmentCardColor.values()[1]);
        assertEquals(DevelopmentCardColor.fromInt(2),DevelopmentCardColor.values()[2]);
        assertEquals(DevelopmentCardColor.fromInt(3),DevelopmentCardColor.values()[3]);

    }
}