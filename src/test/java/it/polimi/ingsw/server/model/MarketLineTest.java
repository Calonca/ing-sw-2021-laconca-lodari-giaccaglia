package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.market.MarketLine;
import org.junit.Test;

import static org.junit.Assert.*;

public class MarketLineTest {

    @Test
    public void getLineNumber() {
        MarketLine[] lines = MarketLine.values();

        for(MarketLine line : lines){
            assertEquals(line.getLineNumber(), line.ordinal());
        }
    }

    @Test
    public void valueOf() {
        assertNotNull(MarketLine.valueOf("FIRST_ROW"));
        assertNotNull(MarketLine.valueOf("SECOND_ROW"));
        assertNotNull(MarketLine.valueOf("THIRD_ROW"));
        assertNotNull(MarketLine.valueOf("FIRST_COLUMN"));
        assertNotNull(MarketLine.valueOf("SECOND_COLUMN"));
        assertNotNull(MarketLine.valueOf("THIRD_COLUMN"));
    }
}