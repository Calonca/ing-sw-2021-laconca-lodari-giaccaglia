package it.polimi.ingsw.network.assets.devcards;

/**
 * Development Cards colors. New colors can be added anytime
 */
public enum DevelopmentCardColor {

    GREEN,
    BLUE,
    PURPLE,
    YELLOW,
    INVALID;

    /**
     * Array containing "physical" {@link DevelopmentCardColor DevelopmentCardColors}, used to get {@link DevelopmentCardColor}
     * from it's number in the ordering.
     */
    private static final DevelopmentCardColor[] vals = DevelopmentCardColor.values();


    /**
     * Return the {@link DevelopmentCardColor} corresponding to given value in the {@link DevelopmentCardColor} ordering,
     * returns {@link DevelopmentCardColor#INVALID} if the given value is outside the array
     * @param rNum int representing the {@link DevelopmentCardColor} ordering
     * @return a {@link DevelopmentCardColor}
     */
    public static DevelopmentCardColor fromInt(int rNum){
        return rNum>vals.length||rNum<0? INVALID: vals[rNum];
    }
}