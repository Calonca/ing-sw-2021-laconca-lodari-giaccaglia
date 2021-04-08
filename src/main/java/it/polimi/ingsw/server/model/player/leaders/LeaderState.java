package it.polimi.ingsw.server.model.player.leaders;

/**
 *  <p>Enum class for the <em>Leader State</em> of a {@link Leader Leader}.
 *  <ul>
 *  <li>{@link #DISCARDED}
 *  <li>{@link #ACTIVE}
 *  <li>{@link #INACTIVE}
 *  </ul>
 */

public enum LeaderState {
    /**
     * <em>Leader State</em> consequence of discarding a {@link Leader Leader} through {@link Leader#discard Discard}
     * method during a <em>Leader Action</em> game phase.
     */
    DISCARDED,

    /**
     * <em>Leader State</em> consequence of playing a {@link Leader Leader} through {@link Leader#activate Activate}
     * method during a <em>Leader Action</em> game phase.
     */
    ACTIVE,

    /**
     * Default <em>Leader State</em>
     */
    INACTIVE,
}
