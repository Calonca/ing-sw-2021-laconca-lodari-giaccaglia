package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.marbles.MarbleAsset;
import javafx.util.Pair;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

public class SimpleMarketBoard extends SimpleModelElement{

    private MarbleAsset[][] marbleMatrix;
    private int marketColumns;
    private int marketRows;
    private int faithPointsFromPickedMarbles;
    private MarbleAsset slideMarble;
    private List<MarbleAsset> pickedMarbles;
    private transient Path boardAsset = Paths.get("src/main/resources/assets/punchboard/MarketBoard.png");


    public SimpleMarketBoard(){}

    public SimpleMarketBoard(UUID[][] marbleMatrix, int marketColumns, int marketRows, UUID slideMarbleId, List<UUID> pickedMarblesId, int faithPointsFromPickedMarbles){

        this.marbleMatrix = new MarbleAsset[marketRows][marketColumns];

        for(int i=0; i<marketRows; i++){
            for(int j=0; j<marketColumns; j++){
                this.marbleMatrix[i][j] = MarbleAsset.fromUUID(marbleMatrix[i][j]);
            }
        }

        this.slideMarble = MarbleAsset.fromUUID(slideMarbleId);
        this.pickedMarbles = pickedMarblesId.stream().map(MarbleAsset::fromUUID).collect(Collectors.toList());
        this.marketColumns = marketColumns;
        this.marketRows = marketRows;
        this.faithPointsFromPickedMarbles = faithPointsFromPickedMarbles;

    }

    @Override
    public void update(SimpleModelElement element){

        SimpleMarketBoard serverMarketBoard = (SimpleMarketBoard) element;
        this.marbleMatrix = serverMarketBoard.marbleMatrix;
        this.slideMarble = serverMarketBoard.slideMarble;
        this.pickedMarbles = serverMarketBoard.pickedMarbles;
        this.faithPointsFromPickedMarbles = serverMarketBoard.faithPointsFromPickedMarbles;

    }

    public void setBoardAsset(String path){
        this.boardAsset = Paths.get(path);
    }

    public MarbleAsset getSlideMarble(){
        return slideMarble;
    }

    public List<MarbleAsset> getPickedMarbles(){
        return pickedMarbles;
    }

    public MarbleAsset[][] getMarbleMatrix(){
        return marbleMatrix;
    }

    public int getFaithPointsFromPickedMarbles(){
        return faithPointsFromPickedMarbles;
    }



}
