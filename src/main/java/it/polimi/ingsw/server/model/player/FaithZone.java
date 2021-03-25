package it.polimi.ingsw.server.model.player;

public enum FaithZone {
    NORMAL(-1),
    ZONE1(0),
    ZONE2(1),
    ZONE3(2);

    private final int zoneNumber;

    FaithZone(final int zoneNumber) {
        this.zoneNumber = zoneNumber;
    }

    public int getZoneNumber() {
        return this.zoneNumber;
    }
}
