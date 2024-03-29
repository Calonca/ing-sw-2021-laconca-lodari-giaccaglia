package it.polimi.ingsw.server.model.market;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.board.Box;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class MarketBoardTest {

    GameModel gameModelTest;
    int numberOfWhiteMarbles;

    @Before
    public void setUp() {
        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        players.put(1,"testPlayer2");
        players.put(2,"testPlayer3");

        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        gameModelTest = new GameModel(players, true, null, onlineUsers);
    }

    @Test
    public void marketTest() {

        MarketLine[] lines = MarketLine.values();
        List<Resource> resources = new ArrayList<>();

        resources.add(Resource.STONE);
        resources.add(Resource.GOLD);
        resources.add(Resource.SERVANT);
        resources.add(Resource.SHIELD);

        for (MarketLine line : lines) {
            if (!line.equals(MarketLine.INVALID_LINE))
                pickLineTest(line, resources);
        }
    }

    private void pickLineTest(MarketLine line, List<Resource> mappedResource) {

        gameModelTest.chooseLineFromMarketBoard(line);

        numberOfWhiteMarbles = gameModelTest.getNumberOfWhiteMarblesInPickedLine();

        for (int i = 0; i < numberOfWhiteMarbles; i++)
            gameModelTest.convertWhiteMarbleInPickedLine(mappedResource.get(i).getResourceNumber());

        gameModelTest.updateMatrixAfterTakingResources();


    }

    @Test
    public void areThereWhiteMarblesTest() {

        for (MarketLine line : MarketLine.values()) {
            if (!line.equals(MarketLine.INVALID_LINE))
                testWhiteMarble(line);
        }

    }

    private void testWhiteMarble(MarketLine testLine) {

        Marble[] pickedMarbles;
        gameModelTest.chooseLineFromMarketBoard(testLine);
        pickedMarbles = gameModelTest.getPickedMarbles();
        System.out.println(Arrays.toString(pickedMarbles));
        boolean areThereWhiteMarbles = Arrays.asList(pickedMarbles).contains(Marble.WHITE);
        assertEquals(areThereWhiteMarbles, gameModelTest.areThereWhiteMarblesInPickedLine());
    }

    @Test
    public void getNumberOfWhiteMarblesInPickedLineTest() {

        for (MarketLine line : MarketLine.values()) {
            if (!line.equals(MarketLine.INVALID_LINE))
                testNumberOfWhiteMarbles(line);
        }
    }

    private void testNumberOfWhiteMarbles(MarketLine testLine) {
        List<Marble> pickedMarbles;
        gameModelTest.chooseLineFromMarketBoard(testLine);
        pickedMarbles = Arrays.asList(gameModelTest.getPickedMarbles());
        System.out.println(pickedMarbles);
        int occurrences = pickedMarbles.stream().filter(marble -> marble == (Marble.WHITE)).mapToInt(i -> 1).sum();
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
            if (!line.equals(MarketLine.INVALID_LINE)) {
                for (Resource resource : resources)
                    mappedResourcesTest(line, resource);
            }
        }

    }

    private void mappedResourcesTest(MarketLine testLine, Resource mappedResource) {


        List<Marble> pickedMarbles;
        int whiteMarblesQuantity = 0;
        gameModelTest.chooseLineFromMarketBoard(testLine);
        pickedMarbles = Arrays.asList(gameModelTest.getPickedMarbles());
        System.out.println(pickedMarbles);
        if (gameModelTest.areThereWhiteMarblesInPickedLine())
            whiteMarblesQuantity = gameModelTest.getNumberOfWhiteMarblesInPickedLine();

            for (int i = 0; i < whiteMarblesQuantity; i++)
                gameModelTest.convertWhiteMarbleInPickedLine(mappedResource.getResourceNumber());


        Box mappedResources = gameModelTest.getBoxOfResourcesFromMarketBoard();
        List<Marble> marbles = new ArrayList<>();


        marbles.add(Marble.YELLOW);
        marbles.add(Marble.RED);
        marbles.add(Marble.GRAY);
        marbles.add(Marble.PURPLE);
        marbles.add(Marble.BLUE);

        for (Marble marble : marbles) {
            if (marble != Marble.WHITE && marble != Marble.RED) {
                int occurrences = pickedMarbles.stream().filter(m -> m == (marble)).mapToInt(i -> 1).sum();
                if (marble.getConvertedMarble() == mappedResource)
                    occurrences += whiteMarblesQuantity;

                assertEquals(mappedResources.getNumberOf(marble.getConvertedMarble()), occurrences);
            }
        }

    }

}



