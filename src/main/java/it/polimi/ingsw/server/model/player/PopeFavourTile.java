package it.polimi.ingsw.server.model.player;

public class PopeFavourTile {

    private final int victoryPoints;
    private TileState state;
    private final FaithZone zone;

    public PopeFavourTile(int victoryPoints, FaithZone zone) {
        this.victoryPoints = victoryPoints;
        this.state = TileState.INACTIVE;
        this.zone = zone;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public TileState getTileState() {
        return state;
    }

    public void setTileState(TileState tileState) {
        this.state = tileState;
    }

    public FaithZone getZone() {
        return zone;
    }

}
