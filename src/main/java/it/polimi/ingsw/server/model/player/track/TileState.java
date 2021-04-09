package it.polimi.ingsw.server.model.player.track;

import it.polimi.ingsw.server.model.player.track.FaithTrack;
import it.polimi.ingsw.server.model.player.track.PopeFavourTile;

/**
 *  <p>Enum class for the <em>Tile State</em> of <em>Pope's Favour tiles</em> in the {@link FaithTrack}.<br>
 *  <ul>
 *  <li>{@link #ACTIVE}
 *  <li>{@link #INACTIVE}
 *  <li>{@link #DISCARDED}
 *  </ul>
 */

public enum TileState {

    /**
     *<em>Tile state</em> corresponding to the "face-up" {@link PopeFavourTile} when Vatican Report requirements are met.
     */
    ACTIVE,

    /**
     *Initial <em>Tile state</em> corresponding to the <em>"non active"</em> side (the side with a red X) up of the
     * {@link PopeFavourTile}.
     */
    INACTIVE,

    /**
     * <em>Tile state</em> corresponding to the discarded {@link PopeFavourTile} when Vatican Report requirements aren't met.
     */
    DISCARDED
}