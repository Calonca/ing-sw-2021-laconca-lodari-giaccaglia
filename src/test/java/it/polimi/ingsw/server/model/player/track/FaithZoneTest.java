package it.polimi.ingsw.server.model.player.track;

import org.junit.Test;

import static org.junit.Assert.*;

public class FaithZoneTest {

    @Test
    public void getZoneNumber() {
        FaithZone[] zones = FaithZone.values();

        for (FaithZone zone : zones)
            assertEquals(zone.getZoneNumber(), (zone.ordinal() - 1));
    }

    @Test
    public void valueOf() {
        assertNotNull(FaithZone.valueOf("NORMAL"));
        assertNotNull(FaithZone.valueOf("ZONE1"));
        assertNotNull(FaithZone.valueOf("ZONE2"));
        assertNotNull(FaithZone.valueOf("ZONE3"));
    }


}