package it.polimi.ingsw.server.controller;

public enum EndGameReason {

    //common reasons
    TRACK_END(" has reached the last space of his Faith Track!"),

    MULTIPLE_TRACK_END(" have reached the last space of their Faith Track"),

    SEVENTH_CARD_PURCHASED(" has  bought his 7th Development Card!"),

    //solo game reasons

    TRACK_END_SOLO("You Won! You reached the last space of the Faith Track!"),

    SEVENTH_CARD_PURCHASED_SOLO("You bought the 7th Development Card, you won!"),

    NO_MORE_DEVCARD_TYPE( " is no longer available in the grid!"),

    LORENZO_REACHED_END("Black Cross token reaches the final space of your Faith Track before your Faith Marker, you lose the game!");

    private final String endGameReason;

    EndGameReason(final String endGameReason)
    {
        this.endGameReason = endGameReason;
    }

    public String getEndGameReason(){

        return this.endGameReason;
    }


}
