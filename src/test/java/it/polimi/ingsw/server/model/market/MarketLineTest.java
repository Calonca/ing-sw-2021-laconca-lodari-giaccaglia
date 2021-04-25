package it.polimi.ingsw.server.model.market;

import it.polimi.ingsw.server.model.market.MarketLine;
import org.junit.Test;

import static org.junit.Assert.*;

public class MarketLineTest {

    @Test
    public void getLineNumber() {
        MarketLine[] lines = MarketLine.values();

        for(MarketLine line : lines){
            if(!line.equals(MarketLine.INVALID_LINE))
                assertEquals(line.getLineNumber(), line.ordinal());
            else
                assertEquals(line.getLineNumber(), -1);
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