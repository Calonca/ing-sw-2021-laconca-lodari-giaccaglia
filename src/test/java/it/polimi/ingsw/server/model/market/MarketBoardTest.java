package it.polimi.ingsw.server.model.market;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.board.Box;
import it.polimi.ingsw.server.model.player.track.PopeFavourTile;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class MarketBoardTest {

    GameModel gameModelTest;
    boolean areThereWhiteMarbles;
    int numberOfWhiteMarbles;
    Box mappedResources;

    @Before
    public void setUp() throws Exception {
        List<String> players = new ArrayList<>();
        players.add("testPlayer");
        boolean isSinglePlayer = true;
        gameModelTest = new GameModel(players, isSinglePlayer);
        areThereWhiteMarbles = false;
    }

    @Test
    public void marketTest() {

        MarketLine[] lines = MarketLine.values();
        List<Resource> resources = new ArrayList<>();

        resources.add(Resource.STONE);
        resources.add(Resource.GOLD);
        resources.add(Resource.SERVANT);
        resources.add(Resource.SHIELD);

        for (MarketLine line : lines)
            pickLineTest(line, resources);
    }

    private void pickLineTest(MarketLine line, List<Resource> mappedResource) {

        gameModelTest.chooseLineFromMarketBoard(line);

        areThereWhiteMarbles = gameModelTest.areThereWhiteMarblesInPickedLine();
        numberOfWhiteMarbles = gameModelTest.getNumberOfWhiteMarblesInPickedLine();

        for (int i = 0; i < numberOfWhiteMarbles; i++)
            gameModelTest.convertWhiteMarbleInPickedLine(mappedResource.get(i));

        mappedResources = gameModelTest.getBoxResourcesFromMarketBoard();
        gameModelTest.updateMatrixAfterTakingResources();


    }

    @Test
    public void areThereWhiteMarblesTest() {

        for(MarketLine  line : MarketLine.values())
            testWhiteMarble(line);

    }

    private void testWhiteMarble(MarketLine testLine){

        Marble[] pickedMarbles;
        pickedMarbles = gameModelTest.chooseLineFromMarketBoard(testLine);
        System.out.println(Arrays.toString(pickedMarbles));
        boolean areThereWhiteMarbles = Arrays.asList(pickedMarbles).contains(Marble.WHITE);
        assertEquals(areThereWhiteMarbles, gameModelTest.areThereWhiteMarblesInPickedLine());
    }

    @Test
    public void getNumberOfWhiteMarblesInPickedLineTest(){

        for(MarketLine  line : MarketLine.values())
            testNumberOfWhiteMarbles(line);

    }

    private void testNumberOfWhiteMarbles(MarketLine testLine){
        List<Marble> pickedMarbles;
        pickedMarbles = Arrays.asList(gameModelTest.chooseLineFromMarketBoard(testLine));
        System.out.println(pickedMarbles);
        int occurrences = pickedMarbles.stream().filter(marble -> marble ==(Marble.WHITE)).mapToInt(i -> 1).sum();
        System.out.println(occurrences);

        assertEquals(occurrences, gameModelTest.getNumberOfWhiteMarblesInPickedLine());

    }

    @Test
    public void getMappedResourcesBoxTest() {

        List<Resource> resources = new ArrayList<>();

        resources.add(Resource.STONE);
        resources.add(Resource.GOLD);
        resources.add(Resource.SERVANT);
        resources.add(Resource.SHIELD);

        for (MarketLine line : MarketLine.values()) {
            for(Resource resource : resources)
                mappedResourcesTest(line, resource, resources);
        }

    }

    private void mappedResourcesTest(MarketLine testLine, Resource mappedResource, List<Resource> resources) {

        List<Marble> pickedMarbles;
        pickedMarbles = Arrays.asList(gameModelTest.chooseLineFromMarketBoard(testLine));
        System.out.println(pickedMarbles);
        if(gameModelTest.areThereWhiteMarblesInPickedLine())
            for(int i=0; i<gameModelTest.getNumberOfWhiteMarblesInPickedLine(); i++)
                gameModelTest.convertWhiteMarbleInPickedLine(mappedResource);


        Box mappedResources = gameModelTest.getBoxResourcesFromMarketBoard();
        List<Marble> marbles = new ArrayList<>();

        int whiteMarblesQuantity = gameModelTest.getNumberOfWhiteMarblesInPickedLine();

        marbles.add(Marble.YELLOW);
        marbles.add(Marble.RED);
        marbles.add(Marble.GRAY);
        marbles.add(Marble.PURPLE);
        marbles.add(Marble.BLUE);

        for(Marble marble : marbles) {
            if (marble != Marble.WHITE && marble != Marble.RED) {
                int occurrences = pickedMarbles.stream().filter(m -> m == (marble)).mapToInt(i -> 1).sum();
                if (marble.getConvertedMarble() == mappedResource)
                    occurrences += whiteMarblesQuantity;

                assertEquals(mappedResources.getNumberOf(marble.getConvertedMarble()), occurrences);
            }
        }

    }

    


}

