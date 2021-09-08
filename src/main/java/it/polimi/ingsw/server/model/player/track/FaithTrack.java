package it.polimi.ingsw.server.model.player.track;

import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.player.leaders.Leader;
import it.polimi.ingsw.server.model.solo.SoloActionToken;
import javafx.util.Pair;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static it.polimi.ingsw.network.jsonutils.JsonUtility.serialize;

/**
 * Represents the {@link Player} <em>FaithTrack</em>
 */
public class FaithTrack {

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

    private final MutablePair<Piece, Integer> playerPiece = new MutablePair<>(Piece.PLAYER, 0);
    private final MutablePair<Piece, Integer> lorenzoPiece = new MutablePair<>(Piece.LORENZO, 0);

    private Map<Integer, Boolean> playerTilesStatus;
    private Map<Integer, Boolean> lorenzoTilesStatus;
    /**
     * <p>{@link List} of {@link FaithCell FaithCells} objects representing the core structure of the <em>FaithTrack</em>.<br>
     * The default number of elements is 25, which is set on the game setup.
     */
    private List<FaithCell> track;

    /**
     * <p>{@link List} of {@link PopeFavourTile PopeFavourTiles} objects of the <em>FaithTrack</em>.<br>
     * The default number of elements is 3, which is set on the game setup.<br>
     */
    private List<PopeFavourTile> tiles;

    public String serializeFaithTrack(){
        return serialize(this);
    }

    public void initTrackCells(){
        playerTilesStatus = initializeTrackCellsStatus();
        lorenzoTilesStatus = initializeTrackCellsStatus();

    }

    private Map<Integer, Boolean> initializeTrackCellsStatus(){

        return IntStream.range(0, tiles.size()).boxed().collect(Collectors.toMap(
                index -> index,
                index -> false));

    }

    /**
     * <p>Increments by one the <em>position</em> of the <em>Piece</em> parameter when a <em>Vatican Report</em> occurs,
     * an opponent's {@link Leader} or {@link Resource} is discarded.<br>
     * If the Piece's position equals to 24, after the increment has been done, the game ends as
     * the {@link Piece} has reached the last place of this <em>FaithTrack</em>
     *
     * @param piece <em>FaithTrack</em> piece which can be either a multiplayer <em>FaithMarker</em> or a solo mode
     * <em>Black Cross</em> token.
     */
    private void addFaithPoint(MutablePair<Piece, Integer>  piece){
        if(piece.getValue() < 24) {
            int pos = piece.getValue() + 1;
            piece.setValue(pos);
        }
    }

    /**
     * Increments by one the <em>position</em> of the <em>playerPiece</em> when a <em>Vatican Report</em> occurs,
     * an opponent's {@link Leader} or {@link Resource} is discarded.
     */
    public void moveOnePosition(){
        addFaithPoint(playerPiece);
    }

    /**
     * Increments by one the <em>position</em> of the <em>lorenzoPiece</em> when a <em>Vatican Report</em> occurs,
     * single player's {@link Leader} or {@link Resource} is discarded, {@link SoloActionToken#ADD2FAITH ADD2FAITH}
     * or {@link SoloActionToken#SHUFFLE_ADD1FAITH SHUFFLE_ADD1FAITH} <em>Solo Action tokens</em> are revealed.
     */
    public void moveLorenzoOnePosition(){
        addFaithPoint(lorenzoPiece);
    }

    /**
     * Checks if the <em>Piece</em> passed as a parameter is currently in a <em>Pope Space</em>.
     *
     * @param piece <em>FaithTrack</em> piece which can be either a multiplayer <em>FaithMarker</em> or a solo mode
     * <em>Black Cross</em> token.
     *
     * @return the boolean returned value of FaithCell's {@link FaithCell#isPopeSpace() isPopeSpace} method
     */
    public boolean isPieceInPopeSpaceForTheFirstTime(MutablePair<Piece, Integer> piece){
        return (track.get(piece.getValue()).isPopeSpace()) && !checkPopeSpace(piece);
    }

    private boolean checkPopeSpace(MutablePair<Piece, Integer> piece){
        return playerTilesStatus.get(track.get(piece.getValue()).getZone().getZoneNumber());
    }

    /**
     * @return  true if the PlayerPiece is currently in a Pope Space along FaithTrack.
     */
    public boolean isPlayerInPopeSpaceForTheFirstTime(){
        return isPieceInPopeSpaceForTheFirstTime(playerPiece);
    }

    /**
     * <em>Solo Mode</em> method to check if <em>LorenzoPiece</em> is in <em>PopeSpace</em>
     * @return true if the <em>LorenzoPiece</em> is currently in a <em>Pope Space</em> along {@link FaithTrack}.
     */
    public boolean isLorenzoInPopeSpaceForTheFirstTime(){
        return isPieceInPopeSpaceForTheFirstTime(lorenzoPiece);
    }

    private MutablePair<Piece, Integer> getPiece(MutablePair<Piece, Integer> piece){
        MutablePair<Piece, Integer> pieceCopy = new MutablePair<>();
        pieceCopy.setLeft(piece.getLeft());
        pieceCopy.setRight(piece.getRight());
        return pieceCopy;
    }

    /**
     *
     * @return Piece
     */
    public MutablePair<Piece, Integer> getPlayerPiece() {
        return getPiece(playerPiece);
    }

    /**
     *
     * @return Piece
     */
    public MutablePair<Piece, Integer> getLorenzoPiece() {
            return getPiece(lorenzoPiece);
        }

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

    /**
     * Invoked when <em>Vatican Report</em> occurs, this method checks if the <em>popeSpacePosition</em> passed as
     * a parameter matches or is a previously crossed <em>track</em> position of the {@link Piece#PLAYER PLAYER} <em>Piece</em>
     *
     * @param popeSpacePosition The track position which has led to the <em>Vatican Report</em>
     */
    public void turnPopeFavourTile(int popeSpacePosition) {

        FaithZone currentZone = track.get(playerPiece.getValue()).getZone();

        playerTilesStatus.put(currentZone.getZoneNumber(), true); //pope space has been reported
        lorenzoTilesStatus.put(currentZone.getZoneNumber(), true);

        int currentZoneNumber = currentZone.getZoneNumber();

        FaithZone popeSpaceZone = track.get(popeSpacePosition).getZone();
        int popeSpaceZoneNumber = popeSpaceZone.getZoneNumber();

        if(tiles.get(popeSpaceZoneNumber).getTileState().equals(TileState.DISCARDED)) //already checked tile state
            return;

        if(currentZone.getZoneNumber() == popeSpaceZone.getZoneNumber())
            tiles.get(currentZoneNumber).setTileState(TileState.ACTIVE);

        else if(playerPiece.getValue() > popeSpacePosition)
            tiles.get(popeSpaceZoneNumber).setTileState(TileState.ACTIVE);

        else
            tiles.get(popeSpaceZoneNumber).setTileState(TileState.DISCARDED);
    }

    public Optional<Pair<Integer, TileState>> getStateOfLastTurnedTile(){
        return IntStream
                .range(0, tiles.size())
                .boxed()
                .filter(position -> !tiles.get(position).getTileState().equals(TileState.INACTIVE))
                .reduce((first,second) -> second).map(position ->
                        new Pair<>(
                                position,
                                tiles.get(position).getTileState()));
    }

    /**
     * <p>If the current Piece's position equals to 25 the game ends as
     * the {@link Piece} parameter has reached the last place of this <em>FaithTrack</em>.<br>
     * In case of <em>Solo mode</em>, if {@link Piece#LORENZO LORENZO} <em>Black cross</em>
     * current position equals to 25 the game ends and the player loses the game.
     *
     * @param piece <em>FaithTrack</em> piece which can be either a multiplayer <em>FaithMarker</em> or a solo mode
     * <em>Black Cross</em> token.
     *
     * @return true if current position equals 25, otherwise false
     */
    public boolean hasReachedLastSpace(MutablePair<Piece, Integer> piece){
        return piece.getValue() == 24;
    }

    /**
     * Calculates the amount of <em>Victory Points</em> player scores from the {@link FaithCell#points VictoryPoints} of
     * the last {@link FaithCell FaithCell} giving points, if crossed.
     *
     * @return <em>Victory Points</em> from {@link PopeFavourTile PopeFavourTiles}
     */
    public int getPointsFromFaithTrackCell(){
        return track.stream()
                .filter(cell -> (track.indexOf(cell) <= playerPiece.getValue()))
                .mapToInt(FaithCell::getPoints)
                .max()
                .orElse(0);
    }

    /**
     * Calculates the amount of <em>Victory Points</em> player score from the {@link PopeFavourTile PopeFavourTiles}
     * having the current state set to {@link TileState#ACTIVE ACTIVE}
     *
     * @return sum of <em>Victory Points</em> from {@link PopeFavourTile PopeFavourTiles}
     */
    public int getPointsFromPopeFavourTiles(){
        return tiles.stream()
                .filter(tile -> (tile.getTileState().equals(TileState.ACTIVE)))
                .mapToInt(PopeFavourTile::getVictoryPoints)
                .max()
                .orElse(0);

    }

    public int getLorenzoPosition(){
        return getPiecePosition(lorenzoPiece);
    }

    public int getPlayerPosition(){
        return getPiecePosition(playerPiece);
    }


}

