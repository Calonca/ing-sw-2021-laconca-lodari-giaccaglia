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

    private int xPos;

    private int yPos;

    public Optional<Path> getFavourTileAssetPath(){

        if(state.equals(TileState.ACTIVE))
            return Optional.of(activeFavourTileAssetPath);
        else if(state.equals(TileState.INACTIVE))
            return Optional.of(inactiveFavourTileAssetPath);
        else return Optional.empty();

    }

    /**
     * @return The current {@link TileState} associated with this tile.
     */
    public TileState getTileState() {
        return state;
    }


}
