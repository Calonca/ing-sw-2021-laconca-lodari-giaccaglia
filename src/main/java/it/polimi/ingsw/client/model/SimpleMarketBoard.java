package it.polimi.ingsw.client.model;

import it.polimi.ingsw.network.assets.marbles.MarbleAsset;

public class SimpleMarketBoard {

    private MarbleAsset[][] marbleMatrix;
    private MarbleAsset slideMarble;


    public SimpleMarketBoard(){}

    public SimpleMarketBoard(MarbleAsset[][] marbleMatrix, MarbleAsset slideMarble){
        this.marbleMatrix = marbleMatrix;
        this.slideMarble = slideMarble;
    }

}
