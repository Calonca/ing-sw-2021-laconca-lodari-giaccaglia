package it.polimi.ingsw.server.model.market;

import com.rits.cloning.Cloner;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.board.Box;
import it.polimi.ingsw.server.model.states.State;
import it.polimi.ingsw.server.utils.Deserializator;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * Matrix data structure to store {@link Marble MarketMarbles} representing {@link Resource Resources}
 * available in the MarketBoard.
 */
public class MarketBoard {

    /**
     * <p>Represents the actual structure of the (rows x columns) marble matrix
     */
    private Marble[][] marbleMatrix;

    /**
     * {@link Marble MarketMarbles} picked from the MarketBoard and stored for white marble identification
     * and conversion process.
     */
    private Marble[] pickedMarbles;

    /**
     * Converted {@link Marble MarketMarbles} picked from the MarketBoard and stored in a
     * {@link Box} of Resources for player to store;
     */
    private Box mappedResources;

    /**
     * Number of White {@link Marble MarketMarbles} picked from the MarketBoard, necessary for
     * conversion process.
     */
    private int whiteMarblesQuantity;

    private int faithPointsFromMarbles;

    /**
     * {@link MarketBoard#marbleMatrix} number of rows.
     */
    private int rows;

    /**
     * {@link MarketBoard#marbleMatrix} number of columns.
     */
    private int columns;

    /**
     * Extra {@link Marble} to place on the top right corner of MarketBoard slide.
     * Changes value everytime {@link MarketBoard#updateMatrix} is invoked.
     */
    private Marble slideMarble;

    /**
     * Each row and column of the {@link MarketBoard#marbleMatrix} has a corresponding {@link MarketLine}.
     * A <em>MarketLine</em> is needed to pick an entire row or column during {@link State#CHOOSING_RESOURCE_FOR_PRODUCTION}
     * turn phase.
     */
    private MarketLine line;

    public static MarketBoard initializeMarketBoard() throws IOException {

        MarketBoard marketBoard = Deserializator.marketBoardDeserialization();

        List<Marble> marbles  = Arrays
                .stream(marketBoard.marbleMatrix)
                .flatMap(Arrays::stream).collect(Collectors.toList());

        Collections.shuffle(marbles);

        marketBoard.marbleMatrix = IntStream.range(0,marketBoard.columns * marketBoard.rows)
                .mapToObj((pos)->new Pair<>(pos,marbles.get(pos)))
                .collect(groupingBy((e)->e.getKey()% marketBoard.rows))
                .values()
                .stream()
                .map(MarketBoard::pairToValue)
                    .toArray(Marble[][]::new);

        int randomRowPos = ThreadLocalRandom.current().nextInt(0, marketBoard.rows);
        int randomColumnPos = ThreadLocalRandom.current().nextInt(0, marketBoard.columns);
        marketBoard.slideMarble = Marble.WHITE;
        Marble temporaryMarble = marketBoard.marbleMatrix[randomRowPos][randomColumnPos];
        marketBoard.marbleMatrix[randomRowPos][randomColumnPos] = marketBoard.slideMarble;
        marketBoard.slideMarble = temporaryMarble;

        return marketBoard;
    }

    public Marble[][] getMarbleMatrix(){
        return new Cloner().deepClone(marbleMatrix);
    }

    public Marble getSlideMarble(){
        return slideMarble;
    }

    public Marble[] getPickedMarbles(){
        return pickedMarbles;
    }

    public int getRows(){
        return rows;
    }

    public int getColumns(){
        return columns;
    }

    private static Marble[] pairToValue(List<Pair<Integer, Marble>> pos_marArray){
        return pos_marArray.stream().map(Pair::getValue).toArray(Marble[]::new);
    }

    /**
     * Selects a whole {@link MarketBoard#marbleMatrix} row or column as an array of {@link Marble Marbles}.
     *
     * @param line {@link MarketLine} corresponding to the row or column to get.
     *
     */
    public void chooseMarketLine(MarketLine line){

        this.line = line;
        int lineNumber = line.getLineNumber();
        mappedResources = Box.discardBox();

        pickedMarbles =  (lineNumber < rows) ?
                marbleMatrix[lineNumber]
                : getColumn(lineNumber);

       calculateFaithPointsFromMarbles();

       Map<Marble, Long> resourceOccurrences = getResourcesOccurrences(pickedMarbles);

       mapMarblesToResources(resourceOccurrences);

       countNumberOfWhiteMarbles(resourceOccurrences);

    }

    public int getFaithPointsFromMarbles(){
        return faithPointsFromMarbles;
    }

    private void calculateFaithPointsFromMarbles(){

        faithPointsFromMarbles = Arrays.stream(pickedMarbles)
                .filter(marble -> marble.equals(Marble.RED))
                .mapToInt(marble -> 1).sum();

    }


    private void mapMarblesToResources(Map<Marble, Long> resourceOccurrences){

        //all marbles but white ones are mapped to their corresponding resources
        resourceOccurrences
                .entrySet()
                .stream()
                .filter(e -> e.getKey()!=Marble.WHITE)
                .forEach(
                        e -> mapResource(e.getKey().getConvertedMarble().getResourceNumber(),
                                e.getValue().intValue())
                );

    }

    private void countNumberOfWhiteMarbles(Map<Marble, Long> resourceOccurrences){

        //counts number of white marbles in the picked line
        whiteMarblesQuantity = resourceOccurrences
                .entrySet()
                .stream()
                .filter(e -> e.getKey()==Marble.WHITE)
                .mapToInt(e -> Math.toIntExact(e.getValue())).sum();


    }

    private void mapResource(int resourceNumber, int quantity){

        int [] resourcesQuantites =IntStream.range(0, 5)
                .map(i ->  (i==resourceNumber) ? quantity : 0).toArray();

        mappedResources.addResources(resourcesQuantites);

    }

    private Map<Marble, Long> getResourcesOccurrences(Marble [] pickedMarbles){
        return Arrays.stream(pickedMarbles)
                .collect(groupingBy(Function.identity(), counting()));
    }

    /**
     * @return true if currently stored {@link MarketLine} contains any @link Marble#WHITE WHITE MARBLE}, otherwise returns
     * false.
     */
    public boolean areThereWhiteMarbles(){
        return whiteMarblesQuantity>0;
    }

    /**
     * Method to get the amount of {@link Marble#WHITE WHITE MARBLES} contained in selected {@link MarketLine}.
     * @return number of {@link Marble#WHITE WHITE MARBLES} in previously selected {@link MarketLine}
     * when {@link MarketBoard#pickedMarbles} method is invoked.
     */
    public int getNumberOfWhiteMarbles(){
        return whiteMarblesQuantity;
    }

    /**
     * Method to convert a {@link Resource} passed as a parameter for a {@link Marble#WHITE WHITE MARBLE} resource
     * picked together with other {@link Marble Marbles} after {@link MarketBoard#pickedMarbles} method invocation.
     * @param mappedResource {@link Resource} of {@link Marble#WHITE WHITEMARBLE} to convert when
     * corresponding {@link it.polimi.ingsw.server.model.player.leaders.Leader} effect has been enabled.
     */
    public void convertWhiteMarble(int mappedResource) {
        mapResource(mappedResource, 1);
        whiteMarblesQuantity--;
    }

    public void convertAllWhiteMarblesInPickedLine(int mappedResource){
        while(whiteMarblesQuantity>0)
            convertWhiteMarble(mappedResource);
    }

    public Box getMappedResourcesBox(){
        return mappedResources;
    }

    /**
     * <p>Invoked after {@link MarketBoard#chooseMarketLine} method execution, to update {@link MarketBoard#marbleMatrix}
     * structure by inserting the {@link MarketBoard#slideMarble} in the chosen line.<br>
     * <em>If the chosen line is a row, Marble in first position becomes the next <em>slideMarble</em> and the current <em>slideMarble</em>
     * is inserted in the last position of the line, after Marbles shifting one position to
     * the left.<br>
     * <br>If the chosen line is a column, Marble in first position becomes the next <em>slideMarble</em> and the current <em>slideMarble</em>
     * is inserted in the last position of the column, after Marbles shifting one position down.
     *
     */
    public void updateMatrix() {

        int lineNumber = line.getLineNumber();
        Marble nextExternalMarble;

        if (lineNumber < rows) {
            nextExternalMarble = marbleMatrix[lineNumber][0];
                System.arraycopy(marbleMatrix[lineNumber], 1, marbleMatrix[lineNumber], 0, columns - 1);
                marbleMatrix[lineNumber][columns-1] = slideMarble;
        }

        else {

            nextExternalMarble = marbleMatrix[0][lineNumber - 3];

            for(int i=1; i<rows; i++)
                marbleMatrix[i-1][lineNumber-3] = marbleMatrix[i][lineNumber-3];

                marbleMatrix[rows-1][lineNumber-3] = slideMarble;
        }
        slideMarble = nextExternalMarble;

    }

    private Marble[] getColumn(int column) {
        return pairToValue(
                    IntStream.range(0, rows)
                    .mapToObj(i -> new Pair<>(i, marbleMatrix[i][column-rows]))
                    .collect(Collectors.toList())
        );
    }

    /* public static void main(String[] args) {
        MarketBoard resourcesMarket = initializeMarketBoard("/Users/pablo/IdeaProjects/ing-sw-2021-laconca-lodari-giaccaglia/target/classes/config/MarketBoardConfig.json");
        System.out.println(Arrays.deepToString(resourcesMarket.marbleMatrix));
        System.out.println(Arrays.toString(resourcesMarket.chooseMarketLine(MarketLine.FIRST_COLUMN)));
        resourcesMarket.updateMatrix(MarketLine.FIRST_ROW);
        System.out.println(Arrays.deepToString(resourcesMarket.marbleMatrix));
    }*/

}


