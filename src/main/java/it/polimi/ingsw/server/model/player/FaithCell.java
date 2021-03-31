package it.polimi.ingsw.server.model.player;

public class FaithCell {

    private final int points;
    private final FaithZone zone;
    private final boolean popeSpace;

    public FaithCell(int points, FaithZone zone, boolean popeSpace) {
        this.zone = zone;
        this.points = points;
        this.popeSpace = popeSpace;
    }


    public int getPoints() {
        return points;
    }

    public boolean isPopeSpace () {
        return popeSpace;
    }

    public FaithZone getZone() {
        return zone;
    }


}
