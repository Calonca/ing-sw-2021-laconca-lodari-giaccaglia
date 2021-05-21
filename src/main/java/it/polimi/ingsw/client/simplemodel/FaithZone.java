package it.polimi.ingsw.client.simplemodel;


/**
 *  <p>Enum class for <em>Faith Zones</em> in the {@link SimpleFaithTrack}.<br>
 *  Each Zone is mapped to an Int value for finding belonging Zone of the {@link FaithCell FaithCells} along the Faith Track.</p>
 *  <ul>
 *  <li>{@link #NORMAL}
 *  <li>{@link #ZONE1}
 *  <li>{@link #ZONE2}
 *  <li>{@link #ZONE3}
 *  </ul>
 */

public enum FaithZone {

    /**
     * <em>Faith Track Zone</em> corresponding to {@link FaithCell FaithCell} not giving <em>Victory Points (VP)</em>
     * nor belonging to a <em>Vatican Report section</em>.
     */
    NORMAL(-1),

    /**
     * <em>Faith Track Zone</em> corresponding to {@link FaithCell FaithCell}
     * belonging to the first <em>Vatican Report section</em>.
     */
    ZONE1(0),

    /**
     * <em>Faith Track Zone</em> corresponding to {@link FaithCell FaithCell}
     * belonging to the second <em>Vatican Report section</em>.
     */
    ZONE2(1),

    /**
     * <em>Faith Track Zone</em> corresponding to {@link FaithCell FaithCell}
     * belonging to the third <em>Vatican Report section</em>.
     */
    ZONE3(2);

    private final int zoneNumber;

    FaithZone(final int zoneNumber) {
        this.zoneNumber = zoneNumber;
    }

    /**
     * Invoked when inspecting the {@link SimpleFaithTrack} during <em>Vatican Report</em> to check if Vatican Report section conditions are met.
     * @return zoneNumber Int value corresponding to the <em>Zone Number</em> on which method is called
     */
    public int getZoneNumber() {
        return this.zoneNumber;
    }

}
