package it.polimi.ingsw.server.model.player.track;

/**
 * Represents a <em>Pope Space</em> of the {@link FaithTrack}, in which is part of the {@link FaithTrack#tiles tiles} list.
 */
public class PopeFavourTile {

    /**
     * The number of <em>Victory Points</em> associated with this tile.
     */
    private int victoryPoints;

    /**
     * The current {@link TileState} associated with this tile.
     */
    private TileState state;

    /**
     * The {@link FaithZone} to which this cell belongs.
     */
    private FaithZone zone;

    private int x_pos;

    private int y_pos;


    /*
    /**
     * This constructor creates a <em>PopeFavourTile</em> when the {@link FaithTrack} is populated during game setup.
     * When the object is created, the initialization process sets the {@link TileState} to the default
     * {@link TileState#INACTIVE INACTIVE} value.
     *
    public PopeFavourTile(int victoryPoints, FaithZone zone) {
        this.victoryPoints = victoryPoints;
        this.state = TileState.INACTIVE;
        this.zone = zone;
    }

    */

    /**
     * @return <em>Victory Points</em> associated with this tile.
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * @return The current {@link TileState} associated with this tile.
     */
    public TileState getTileState() {
        return state;
    }

    /**
     * Updates the {@link TileState} if a <em>Vatican Report</em> occurs and the Tile hasn't been previously
     * activated or discarded.
     *
     * @param tileState The updated {@link TileState} associated with this tile.
     */
    public void setTileState(TileState tileState) {
        this.state = tileState;
    }

    /*
    /**
     * @return The {@link FaithZone} to which this tile belongs.
     *
    public FaithZone getZone() {
        return zone;
    }
   */
}
