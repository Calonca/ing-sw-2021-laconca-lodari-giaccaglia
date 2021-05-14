package it.polimi.ingsw.network.assets.leaders;


/**
 *  <p>Enum class for the <em>Leader State</em> of a {@link NetworkLeaderCard Leader}.
 *  <ul>
 *  <li>{@link #DISCARDED}
 *  <li>{@link #ACTIVE}
 *  <li>{@link #INACTIVE}
 *  </ul>
 */

public enum NetworkLeaderState {
    /**
     * <em>Leader State</em> consequence of discarding a {@link NetworkLeaderCard Leader} during a <em>Leader Action</em> game phase.
     */
    DISCARDED,

    /**
     * <em>Leader State</em> consequence of playing a {@link NetworkLeaderCard Leader} through during a <em>Leader Action</em> game phase.
     */
    ACTIVE,

    /**
     * Default <em>Leader State</em>
     */
    INACTIVE,
}
