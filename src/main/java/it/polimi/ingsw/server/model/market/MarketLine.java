package it.polimi.ingsw.server.model.market;

/**
 * Enum class for MarketBoard rows and columns selection
 */
public enum MarketLine {

    FIRST_ROW(0),
    SECOND_ROW(1),
    THIRD_ROW(2),
    FIRST_COLUMN(3),
    SECOND_COLUMN(4),
    THIRD_COLUMN(5),
    FOURTH_COLUMN(6),
    INVALID_LINE(-1);

    private final int lineNumber;

    MarketLine(final int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber(){
        return this.lineNumber;
    }

    /**
     * Array containing "physical" {@link MarketLine MarketLines}, used to get {@link MarketLine}
     * from it's number in the ordering.
     */
    private static final MarketLine[] vals = MarketLine.values();


    /**
     * Return the {@link MarketLine} corresponding to given value in the {@link MarketLine} ordering,
     * returns {@link MarketLine#INVALID_LINE} if the given value is outside the array
     * @param rNum int representing the {@link MarketLine} ordering
     * @return a {@link MarketLine}
     */
    public static MarketLine fromInt(int rNum){
        return rNum>vals.length||rNum<0? INVALID_LINE: vals[rNum];
    }

}
