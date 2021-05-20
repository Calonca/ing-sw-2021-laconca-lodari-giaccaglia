package it.polimi.ingsw.client.simplemodel;

public enum SimpleMarketLine {

        FIRST_ROW(0),
        SECOND_ROW(1),
        THIRD_ROW(2),
        FIRST_COLUMN(3),
        SECOND_COLUMN(4),
        THIRD_COLUMN(5),
        FOURTH_COLUMN(6),
        INVALID_LINE(-1);

        private final int lineNumber;

        SimpleMarketLine(final int lineNumber) {
            this.lineNumber = lineNumber;
        }

        public int getLineNumber(){
            return this.lineNumber;
        }

        /**
         * Array containing "physical" {@link it.polimi.ingsw.server.model.market.MarketLine MarketLines}, used to get {@link it.polimi.ingsw.server.model.market.MarketLine}
         * from it's number in the ordering.
         */
        private static final SimpleMarketLine[] vals = SimpleMarketLine.values();

        /**
         * Return the {@link SimpleMarketLine} corresponding to given value in the {@link SimpleMarketLine} ordering,
         * returns {@link SimpleMarketLine#INVALID_LINE} if the given value is outside the array
         * @param rNum int representing the {@link SimpleMarketLine} ordering
         * @return a {@link SimpleMarketLine}
         */
        public static SimpleMarketLine fromInt(int rNum){
            return rNum>vals.length||rNum<0? INVALID_LINE: vals[rNum];
        }

    }

