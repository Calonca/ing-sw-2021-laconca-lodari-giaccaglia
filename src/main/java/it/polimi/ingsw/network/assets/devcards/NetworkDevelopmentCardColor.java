package it.polimi.ingsw.network.assets.devcards;

/**
 * Development Cards colors. New colors can be added anytime
 */
public enum NetworkDevelopmentCardColor {

    GREEN,
    BLUE,
    PURPLE,
    YELLOW,
    INVALID;

    /**
     * Array containing "physical" {@link it.polimi.ingsw.server.model.cards.DevelopmentCardColor DevelopmentCardColors}, used to get {@link it.polimi.ingsw.server.model.cards.DevelopmentCardColor}
     * from it's number in the ordering.
     */
    private static final NetworkDevelopmentCardColor[] vals = NetworkDevelopmentCardColor.values();


    /**
     * Return the {@link NetworkDevelopmentCardColor} corresponding to given value in the {@link NetworkDevelopmentCardColor} ordering,
     * returns {@link NetworkDevelopmentCardColor#INVALID} if the given value is outside the array
     * @param rNum int representing the {@link NetworkDevelopmentCardColor} ordering
     * @return a {@link it.polimi.ingsw.server.model.cards.DevelopmentCardColor}
     */
    public static NetworkDevelopmentCardColor fromInt(int rNum){
        return rNum>vals.length||rNum<0? INVALID: vals[rNum];
    }
}