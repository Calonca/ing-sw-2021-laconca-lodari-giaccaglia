package it.polimi.ingsw.server.model.market;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.board.Box;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
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

        for(MarketLine line : lines)
                pickLineTest(line, resources);
    }

    private void pickLineTest(MarketLine line, List<Resource> mappedResource){

        gameModelTest.chooseLineFromMarketBoard(line);

        areThereWhiteMarbles = gameModelTest.areThereWhiteMarblesInPickedLine();
        numberOfWhiteMarbles = gameModelTest.getNumberOfWhiteMarblesInPickedLine();

        for (int i = 0; i < numberOfWhiteMarbles; i++)
            gameModelTest.convertWhiteMarbleInPickedLine(mappedResource.get(i));

        mappedResources = gameModelTest.getBoxResourcesFromMarketBoard();
        gameModelTest.updateMatrixAfterTakingResources();

    }
}

