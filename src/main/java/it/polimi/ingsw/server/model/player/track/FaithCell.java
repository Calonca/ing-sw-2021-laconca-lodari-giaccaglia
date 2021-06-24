package it.polimi.ingsw.server.model.player.track;

/**
 * Represents a cell of the {@link FaithTrack}, in which is part of the {@link FaithTrack#track track} list.
 */
public class FaithCell {

    /**
     * The number of <em>Victory Points</em> associated with this cell.
     */
    private int points;

    /**
     * The {@link FaithZone} to which this cell belongs.
     */
    private FaithZone zone;

    /**
     * Boolean value to indicate if this cell is a <em>Pope space</em>, which leads to a <em>Vatican Report</em>.
     */
    private boolean popeSpace;


    private int x_pos;

    private int y_pos;

   /* /**
     * This constructor creates a FaithCell when the {@link FaithTrack} is populated during game setup.

    public FaithCell(int points, FaithZone zone, boolean popeSpace) {
        this.zone = zone;
        this.points = points;
        this.popeSpace = popeSpace;
    }*/

    /**
     * @return The <em>Victory Points</em> associated with this cell.
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return true if this cell is a <em>Pope Space</em>, otherwise false.
     */
    public boolean isPopeSpace () {
        return popeSpace;
    }

    /**
     * @return The {@link FaithZone FaithZone} to which this cell belongs.
     */
    public FaithZone getZone() {
        return zone;
    }


}
