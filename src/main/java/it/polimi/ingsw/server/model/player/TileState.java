package it.polimi.ingsw.server.model.player;

/**
 *  <p>Enum class for <em>Tile State</em> of <em>Pope's Favour tiles</em> in the Faith Track.<br>
 *  <ul>
 *  <li>{@link #ACTIVE}
 *  <li>{@link #INACTIVE}
 *  <li>{@link #DISCARDED}
 *  </ul>
 */

public enum TileState {

    /**
     *<em>Tile state</em> corresponding to the "face-up" <em>Pope Favour tile</em> when Vatican Report requirements are met
     */
    ACTIVE,

    /**
     *Initial <em>Tile state</em> corresponding to the <em>"non active"</em> side (the side with a red X) up.
     */
    INACTIVE,

    /**
     * <em>Tile state</em> corresponding to the discarded <em>Pope Favour tile</em> when Vatican Report requirements aren't met
     */
    DISCARDED
}