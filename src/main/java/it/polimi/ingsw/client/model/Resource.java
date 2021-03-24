package it.polimi.ingsw.client.model;

/**
 * Enum class for resources. Resources order is the same as in Box.
 * New resources are to be added after STONE.
 * For each TOCHOOSE the player will perform a choice, selecting a basic resource.
 * Each BADFAITH point will be consumed and all other players will advance on the FaithTrack.
 * @author PSP10
 */

public enum Resource
{
    GOLD,
    SERVANT,
    SHIELD,
    STONE,
    FAITH,
    BADFAITH,
    TOCHOOSE,
    WHITEMARBLE,
    EMPTY,
}
