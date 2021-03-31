package it.polimi.ingsw.server.model.player;

/**
 * A DISCARDED card can no longer be played.
 * An ACTIVE card has already been played.
 * An inactive card can be played.
 */

public enum LeaderState {
    DISCARDED,
    ACTIVE,
    INACTIVE,
}
