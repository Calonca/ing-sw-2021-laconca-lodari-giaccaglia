package it.polimi.ingsw.server.model.player.track;

import it.polimi.ingsw.server.utils.Deserializator;
import org.apache.commons.lang3.tuple.MutablePair;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.*;

public class FaithTrackTest {

    private FaithTrack testTrack = new FaithTrack();
    private MutablePair<FaithTrack.Piece, Integer> lorenzo;
    private MutablePair<FaithTrack.Piece, Integer>  player;

    @Before
    public void setUp() {
       testTrack = Deserializator.faithTrackDeserialization();
       testTrack.initTrackCells();
       testTrack.moveLorenzoOnePosition();
    }

    @Test
    public void moveOnePosition() {

        testTrack.moveOnePosition();
        lorenzo = testTrack.getLorenzoPiece();
        player = testTrack.getPlayerPiece();

        assertEquals(testTrack.getPiecePosition(lorenzo), 0);
        assertEquals(testTrack.getPiecePosition(player), 1);

        for(int i=0; i<=24; i++) {
            testTrack.moveOnePosition();
        }

        lorenzo = testTrack.getLorenzoPiece();
        player = testTrack.getPlayerPiece();

        assertEquals(testTrack.getPiecePosition(lorenzo), 0);
        assertEquals(testTrack.getPiecePosition(player), 24);


    }

    @Test
    public void moveLorenzoOnePosition() {

        testTrack.moveLorenzoOnePosition();
        lorenzo = testTrack.getLorenzoPiece();
        player = testTrack.getPlayerPiece();


        assertEquals(testTrack.getPiecePosition(lorenzo), 1);
        assertEquals(testTrack.getPiecePosition(player), 0);

        for(int i=0; i<=24; i++) {
            testTrack.moveLorenzoOnePosition();
        }

        lorenzo = testTrack.getLorenzoPiece();
        player = testTrack.getPlayerPiece();

        assertEquals(testTrack.getPiecePosition(lorenzo), 24);
        assertEquals(testTrack.getPiecePosition(player), 0);

    }

    @Test
    public void isPieceInPopeSpace() {

        testNotInPopeSpace(6);

        setUp();
        testNotInPopeSpace(11);

        setUp();
        testNotInPopeSpace(23);

        setUp();
        testInPopeSpace();

    }

    private void testNotInPopeSpace(int position){

        for(int i=0; i<position; i++) {
            testTrack.moveLorenzoOnePosition();
            testTrack.moveOnePosition();
        }
        lorenzo = testTrack.getLorenzoPiece();
        player = testTrack.getPlayerPiece();

        assertFalse(testTrack.isPieceInPopeSpaceForTheFirstTime(lorenzo));
        assertFalse(testTrack.isPieceInPopeSpaceForTheFirstTime(player));

    }

    private void testInPopeSpace(){

        for(int i = 0; i< 24; i++) {
            testTrack.moveLorenzoOnePosition();
            testTrack.moveOnePosition();
        }

        lorenzo = testTrack.getLorenzoPiece();
        player = testTrack.getPlayerPiece();

        assertTrue(testTrack.isPieceInPopeSpaceForTheFirstTime(lorenzo));
        assertTrue(testTrack.isPieceInPopeSpaceForTheFirstTime(player));

    }

    @Test
    public void testPosition() {

        testGetPiecePosition(0);

        setUp();
        testGetPiecePosition(24);

        setUp();
        testGetPiecePosition(13);

        setUp();
        testGetPiecePosition(20);


    }

    private void testGetPiecePosition(int position) {

        for(int i=0; i<position; i++) {
            testTrack.moveLorenzoOnePosition();
            testTrack.moveOnePosition();
        }
        lorenzo = testTrack.getLorenzoPiece();
        player = testTrack.getPlayerPiece();

        assertEquals(position, testTrack.getPiecePosition(lorenzo));
        assertEquals(position, testTrack.getPiecePosition(player));

        assertEquals(position, testTrack.getPlayerPosition());
        assertEquals(position, testTrack.getLorenzoPosition());

    }

    @Test
    public void testTurnPopeFavourTile() throws Exception {

        testFavourTile(11, 8 , TileState.ACTIVE, 0);

        setUp();
        testFavourTile(16, 16, TileState.ACTIVE, 1);

        setUp();
        testFavourTile(17,24, TileState.DISCARDED, 2);

    }

    private void testFavourTile(int position, int popeSpacePosition, TileState expected, int tileNumber) throws NoSuchFieldException, IllegalAccessException {

        for(int i=0; i<position; i++) {
            testTrack.moveLorenzoOnePosition();
            testTrack.moveOnePosition();
        }

        testTrack.turnPopeFavourTile(popeSpacePosition);

        List<PopeFavourTile> tiles;
        Class<?> FaithClass = testTrack.getClass();
        Field field = FaithClass.getDeclaredField("tiles");
        field.setAccessible(true);
        tiles = (List<PopeFavourTile>) field.get(testTrack);
        assertEquals(tiles.get(tileNumber).getTileState(), expected);

    }

    @Test
    public void hasReachedLastSpace() {

        for(int i=0; i<=23; i++) {
            testTrack.moveLorenzoOnePosition();
            testTrack.moveOnePosition();
        }

        lorenzo = testTrack.getLorenzoPiece();
        player = testTrack.getPlayerPiece();

        assertTrue(testTrack.hasReachedLastSpace(lorenzo));
        assertTrue(testTrack.hasReachedLastSpace(player));

        testTrack = Deserializator.faithTrackDeserialization();

        for(int i=0; i<=14; i++) {
            testTrack.moveLorenzoOnePosition();
            testTrack.moveOnePosition();
        }

        lorenzo = testTrack.getLorenzoPiece();
        player = testTrack.getPlayerPiece();

        assertFalse(testTrack.hasReachedLastSpace(lorenzo));
        assertFalse(testTrack.hasReachedLastSpace(player));

    }

    @Test
    public void victoryPointsFromFaithTrackCell() {

        verifyCellVictoryPoints(2, 0);
        verifyCellVictoryPoints(5, 1);
        verifyCellVictoryPoints(8, 2);
        verifyCellVictoryPoints(9, 4);
        verifyCellVictoryPoints(14, 6);
        verifyCellVictoryPoints(17, 9);
        verifyCellVictoryPoints(18, 12);
        verifyCellVictoryPoints(23, 16);
        verifyCellVictoryPoints(24, 20);

    }

    private void verifyCellVictoryPoints(int position, int expectedVictoryPoints) {

        setUp();

        for(int i=0; i<position; i++) {
            testTrack.moveOnePosition();
        }

        int lorenzoVictoryPoints = testTrack.getPointsFromFaithTrackCell();
        assertEquals(expectedVictoryPoints, lorenzoVictoryPoints);

    }

    @Test
    public void victoryPointsFromPopeFavourTiles() {

        verifyPopeFavourTilesVictoryPoints(10, 16,0);
        verifyPopeFavourTilesVictoryPoints(10, 8,2);
        verifyPopeFavourTilesVictoryPoints(17, 16,3);
        verifyPopeFavourTilesVictoryPoints(14, 16,3);
        verifyPopeFavourTilesVictoryPoints(18, 24,0);
        verifyPopeFavourTilesVictoryPoints(24, 24,4);

    }

    private void verifyPopeFavourTilesVictoryPoints(int piecePosition, int popeSpacePosition, int expectedVictoryPoints) {

        setUp();

        for(int i=0; i<piecePosition; i++) {
            testTrack.moveLorenzoOnePosition();
            testTrack.moveOnePosition();
        }

        testTrack.turnPopeFavourTile(popeSpacePosition);
        assertEquals(testTrack.getPointsFromPopeFavourTiles(), expectedVictoryPoints);

    }

}