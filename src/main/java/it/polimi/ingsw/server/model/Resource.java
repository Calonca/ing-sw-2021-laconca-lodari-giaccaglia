package it.polimi.ingsw.server.model;

/**
 * Enum class for resources. Resources order is the same as in Box.
 * New resources are to be added after STONE.
 * For each TOCHOOSE the player will perform a choice, selecting a basic resource.
 * Each BADFAITH point will be consumed and all other players will advance on the FaithTrack.
 */

public enum Resource
{
    GOLD(0),
    SERVANT(1),
    SHIELD(2),
    STONE(3),
    FAITH(4),
    BADFAITH(5),
    TOCHOOSE(6),
    EMPTY(7);

    private final int resourceNumber;

    Resource(final int resourceNumber)
    {
        this.resourceNumber= resourceNumber;
    }

    public int getResourceNumber()
    {
        return this.resourceNumber;
    }

}
