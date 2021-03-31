package it.polimi.ingsw.server.model.player;
import java.util.*;


public class FaithTrack {

    public enum Piece{
        PLAYER(0),
        LORENZO(0);
        private int position;
        Piece(int position) {
            this.position = position;
        }
        public void incrementPosition(){
            this.position++;
        }
    }

    private final Piece playerPiece = Piece.PLAYER;
    private final Piece lorenzoPiece = Piece.LORENZO;
    private List<FaithCell> track;
    private List<PopeFavourTile> tiles;

    private void addFaithPoint(Piece piece){
        if(piece.position < 25)
            piece.incrementPosition();
    }

    public void moveOnePosition(){
        addFaithPoint(playerPiece);
    }

    public void moveLorenzoOnePosition(){
        addFaithPoint(lorenzoPiece);
    }

    public boolean isPieceInPopeSpace(Piece piece){
        return(track.get(piece.position).isPopeSpace());
    }

    public int getPiecePosition(Piece piece){
        return piece.position;
    }

    public void turnPopeFavourTile(int popeSpacePosition) {

        FaithZone currentZone = track.get(playerPiece.position).getZone();
        int currentZoneNumber = currentZone.getZoneNumber();

        FaithZone popeSpaceZone = track.get(popeSpacePosition).getZone();
        int popeSpaceZoneNumber = popeSpaceZone.getZoneNumber();

        if(currentZone.getZoneNumber() == popeSpaceZone.getZoneNumber())
            tiles.get(currentZoneNumber).setTileState(TileState.ACTIVE);

        else if(playerPiece.position > popeSpacePosition)
            tiles.get(popeSpaceZoneNumber).setTileState(TileState.ACTIVE);

        else
            tiles.get(popeSpaceZoneNumber).setTileState(TileState.DISCARDED);
    }

    public boolean hasWon(Piece piece){
        return piece.position == 24;
    }

    public int victoryPointsFromFaithTrackCells(){
        return track.stream()
                .filter(cell -> (track.indexOf(cell) <= playerPiece.position))
                .mapToInt(FaithCell::getPoints)
                .sum();
    }

    public int victoryPointsFromPopeFavourTiles(){
        return tiles.stream()
                .filter(tile -> (tile.getTileState().equals(TileState.ACTIVE)))
                .mapToInt(PopeFavourTile::getVictoryPoints)
                .max()
                .orElse(0);

    }
}



/*    public static void main(String[] args) throws IOException {

        /*FaithTrack testTrack = new FaithTrack("/Users/pablo/IdeaProjects/ing-sw-2021-laconca-lodari-giaccaglia/operatorList.json");
        System.out.println(testTrack.track.get(6).getPoints());
        Gson gson = new GsonBuilder().create();
        Writer writer = new FileWriter("popeTilesList.json");
        gson.toJson(testTrack.ti, writer);
        System.out.println(5);
        writer.flush(); //flush data to file   <---
        writer.close(); //close write
    } */
