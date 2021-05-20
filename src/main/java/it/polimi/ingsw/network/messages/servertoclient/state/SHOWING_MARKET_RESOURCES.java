package it.polimi.ingsw.network.messages.servertoclient.state;


import java.util.UUID;

public class SHOWING_MARKET_RESOURCES extends StateInNetwork{

    private UUID[][] marbleMatrix;
    private int marketColumns;
    private int marketRows;
    private UUID slideMarble;

    public SHOWING_MARKET_RESOURCES () {}

    public SHOWING_MARKET_RESOURCES(int player , UUID[][] marbleMatrix, UUID slideMarble, int marketRows, int marketColumns){
        super(player);
        this.marbleMatrix = marbleMatrix;
        this.slideMarble = slideMarble;
        this.marketRows = marketRows;
        this.marketColumns = marketColumns;
    }

    public UUID[][] getMarbleMatrix() {
        return marbleMatrix;
    }

    public UUID getSlideMarble() {
        return slideMarble;
    }

    public int getMarketColumns() {
        return marketColumns;
    }

    public int getMarketRows() {
        return marketRows;
    }

}
