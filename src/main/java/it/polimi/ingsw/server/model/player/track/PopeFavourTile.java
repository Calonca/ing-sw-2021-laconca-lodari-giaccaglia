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

    private int xPos;

    private int yPos ;

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
}
