package it.polimi.ingsw.server.model;

/**
 * Enum class for
 */

public enum MarketLine {

    FIRST_ROW(0),
    SECOND_ROW(1),
    THIRD_ROW(2),
    FIRST_COLUMN(3),
    SECOND_COLUMN(4),
    THIRD_COLUMN(5),
    FOURTH_COLUMN(6);

    private final int lineNumber;
    MarketLine(final int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber(){
        return this.lineNumber;
    }
}
