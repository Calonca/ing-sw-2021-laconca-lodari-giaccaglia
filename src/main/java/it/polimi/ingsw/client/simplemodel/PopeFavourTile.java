package it.polimi.ingsw.client.simplemodel;


import java.nio.file.Path;

/**
 * Represents a <em>Pope Space</em> of the {@link SimpleFaithTrack}, in which is part of the {@link SimpleFaithTrack#tiles tiles} list.
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

    private Path inactiveFavourTileAssetPath;

    private Path activeFavourTileAssetPath;

    /**
     * The {@link FaithZone} to which this cell belongs.
     */
    private FaithZone zone;

    public Path getFavourTileAssetPath(){

        if(state.equals(TileState.ACTIVE))
            return activeFavourTileAssetPath;
        else return inactiveFavourTileAssetPath;

    }

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
