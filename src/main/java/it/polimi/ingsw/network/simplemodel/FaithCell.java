package it.polimi.ingsw.network.simplemodel;

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

    private int xPos;

    private int yPos;

    /**
     * @return The <em>Victory Points</em> associated with this cell.
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return true if this cell is a <em>Pope Space</em>, otherwise false.
     */
    public boolean isPopeSpace() {
        return !popeSpace;
    }

    /**
     * @return The {@link it.polimi.ingsw.server.model.player.track.FaithZone FaithZone} to which this cell belongs.
     */
    public FaithZone getZone() {
        return zone;
    }

    public int getXPos(){
        return xPos;
    }

    public int getYPos(){
        return yPos;
    }

}
