package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.marbles.MarbleAsset;
import javafx.util.Pair;

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
    private MarbleAsset slideMarble;
    private MarbleAsset[] pickedMarbles;
    private transient Path boardAsset = Paths.get("src/main/resources/assets/punchboard/MarketBoard.png");


    public SimpleMarketBoard(){}

    public SimpleMarketBoard(UUID[][] marbleMatrix, int marketColumns, int marketRows, UUID slideMarbleId){

        List<MarbleAsset> marbles  = Arrays
                .stream(marbleMatrix)
                .flatMap(Arrays::stream)
                .map(MarbleAsset::fromUUID)
                .collect(Collectors.toList());

        this.marbleMatrix =  IntStream.range(0, marketColumns*marketRows)
                .mapToObj((pos)->new Pair<>(pos,marbles.get(pos)))
                .collect(groupingBy((e)->e.getKey()% marketRows))
                .values()
                .stream()
                .map(SimpleMarketBoard::pairToValue)
                .toArray(MarbleAsset[][]::new);

        this.slideMarble = MarbleAsset.fromUUID(slideMarbleId);

    }

    @Override
    public void update(SimpleModelElement element){
        SimpleMarketBoard serverMarketBoard = (SimpleMarketBoard) element;
        this.marbleMatrix = serverMarketBoard.marbleMatrix;
        this.slideMarble = serverMarketBoard.slideMarble;
        this.pickedMarbles = serverMarketBoard.pickedMarbles;
    }

    public void setBoardAsset(String path){
        this.boardAsset = Paths.get(path);
    }

    public MarbleAsset getSlideMarble(){
        return slideMarble;
    }

    public MarbleAsset[] getPickedMarbles(){
        return pickedMarbles;
    }

    public MarbleAsset[][] getMarbleMatrix(){
        return marbleMatrix;
    }

    /**
     * Selects a whole {@link SimpleMarketBoard#marbleMatrix} row or column as an array of {@link MarbleAsset Marbles}.
     *
     * @param line {@link SimpleMarketLine} corresponding to the row or column to get.
     *
     */
    public MarbleAsset[] chooseMarketLine(SimpleMarketLine line){

        int lineNumber = line.getLineNumber();

        pickedMarbles =  (lineNumber < marketRows) ?
                marbleMatrix[lineNumber]
                : getColumn(lineNumber);

        return pickedMarbles;
    }

    private MarbleAsset[] getColumn(int column) {
        return pairToValue(
                IntStream.range(0, marketRows)
                        .mapToObj(i -> new Pair<>(i, marbleMatrix[i][column-marketRows]))
                        .collect(Collectors.toList())
        );
    }

    private static MarbleAsset[] pairToValue(List<Pair<Integer, MarbleAsset>> pos_marArray){
        return pos_marArray.stream().map(Pair::getValue).toArray(MarbleAsset[]::new);
    }


}
