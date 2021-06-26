package it.polimi.ingsw.network.simplemodel;


import java.nio.file.Path;
import java.util.Optional;

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

    private int x_pos;

    private int y_pos;

    public Optional<Path> getFavourTileAssetPath(){

        if(state.equals(TileState.ACTIVE))
            return Optional.of(activeFavourTileAssetPath);
        else if(state.equals(TileState.INACTIVE))
            return Optional.of(inactiveFavourTileAssetPath);
        else return Optional.empty();

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

    public int getX_pos(){
        return x_pos;
    }

    public int getY_pos(){
        return y_pos;
    }

}
