package it.polimi.ingsw.network.simplemodel;

import com.google.gson.Gson;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.List;

import static it.polimi.ingsw.network.jsonUtils.JsonUtility.deserializeFromString;

public class SimpleFaithTrack extends SimpleModelElement {

    /**
     *  <p>Enum class for the <em>Faith Marker</em> and the <em>Black Cross</em> token of <em>Solo mode</em>
     */
    public enum Piece{

        /**
         * Represents the <em>Faith Marker</em>
         */
        PLAYER,

        /**
         * Represents the <em>Black Cross</em>, which is the <em>Faith Marker</em> of <em>Lorenzo il Magnifico</em> for
         * the <em>Solo mode</em> game
         */
        LORENZO

    }

    private String config;

    private MutablePair<Piece, Integer> playerPiece = new MutablePair<>(Piece.PLAYER, 0);
    private MutablePair<Piece, Integer> lorenzoPiece = new MutablePair<>(Piece.LORENZO, 0);

    @Override
    public void update(SimpleModelElement element){

        SimpleFaithTrack serverFaithTrack = (SimpleFaithTrack) element;
        //serverFaithTrack = updateTrack(config);

        config = serverFaithTrack.config;
        playerPiece = serverFaithTrack.playerPiece;
        lorenzoPiece = serverFaithTrack.lorenzoPiece;
        track = serverFaithTrack.track;
        tiles = serverFaithTrack.tiles;

    }

    public SimpleFaithTrack faithTrackConstructor(String config){
        return updateTrack(config);
    }

    public SimpleFaithTrack updateTrack(String config){
        return deserializeFromString(config, SimpleFaithTrack.class, new Gson());

    }

    /**
     * <p>{@link List} of {@link it.polimi.ingsw.network.simplemodel.FaithCell FaithCells} objects representing the core structure of the <em>FaithTrack</em>.<br>
     * The default number of elements is 25, which is set on the game setup.
     */
    private List<FaithCell> track;
    /**
     * <p>{@link List} of {@link PopeFavourTile PopeFavourTiles} objects of the <em>FaithTrack</em>.<br>
     * The default number of elements is 3, which is set on the game setup.<br>
     */
    private List<PopeFavourTile> tiles;

    public List<FaithCell> getTrack() {
        return track;
    }

    public List<PopeFavourTile> getTiles(){ return tiles;}

    /**
     * Gets the current position along the <em>FaithTrack</em> of the <em>Piece</em> passed as a parameter.
     *
     * @param piece <em>FaithTrack</em> piece which can be either a multiplayer <em>FaithMarker</em> or a solo mode
     * <em>Black Cross</em> token.
     *
     * @return Int value of the current <em>Piece</em> position
     */
    public int getPiecePosition(MutablePair<Piece, Integer> piece){
        return piece.getValue();
    }

    public int getLorenzoPosition(){
        return getPiecePosition(lorenzoPiece);
    }

    public int getPlayerPosition(){
        return getPiecePosition(playerPiece);
    }



}

