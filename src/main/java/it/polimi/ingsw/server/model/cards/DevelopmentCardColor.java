package it.polimi.ingsw.server.model.cards;

/**
 * Development Cards colors. New colors can be added anytime
 */
public enum DevelopmentCardColor {

    GREEN,
    BLUE,
    PURPLE,
    YELLOW;

    private static final DevelopmentCardColor[] vals = {GREEN,BLUE,PURPLE,YELLOW};

    public static DevelopmentCardColor fromInt(int rNum){
        return vals[rNum];
    }
}