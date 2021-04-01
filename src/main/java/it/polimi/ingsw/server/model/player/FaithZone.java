package it.polimi.ingsw.server.model.player;

/**
 *  <p>Enum class for <em>Faith Zones</em> in the Faith Track.<br>
 *  Each Zone is mapped to an Int value for finding belonging <em>Faith Cells</em> along the Faith Track.</p>
 *  <ul>
 *  <li>{@link #NORMAL}
 *  <li>{@link #ZONE1}
 *  <li>{@link #ZONE2}
 *  <li>{@link #ZONE3}
 *  </ul>
 */

public enum FaithZone {

    /**
     * <em>Faith Track Zone</em> corresponding to Faith Cell not giving <em>Victory Points (VP)</em>
     * nor belonging to a <em>Vatican Report section</em>.
     */
    NORMAL(-1),

    /**
     * <em>Faith Track Zone</em> corresponding to Faith Cell
     * belonging to the first <em>Vatican Report section</em>.
     */
    ZONE1(0),

    /**
     * <em>Faith Track Zone</em> corresponding to Faith Cell
     * belonging to the second <em>Vatican Report section</em>.
     */
    ZONE2(1),

    /**
     * <em>Faith Track Zone</em> corresponding to Faith Cell
     * belonging to the third <em>Vatican Report section</em>.
     */
    ZONE3(2);

    private final int zoneNumber;

    FaithZone(final int zoneNumber) {
        this.zoneNumber = zoneNumber;
    }

    /**
     * <p>Called when inspecting Faith Track during <em>Vatican Report</em> to check if Vatican Report section conditions are met.<br>
     * @return zoneNumber Int value corresponding to the <em>Zone Number</em> on which method is called
     * </p>
     */
    public int getZoneNumber() {
        return this.zoneNumber;
    }
}
