package it.polimi.ingsw.server.model;
/**
 *  <p>Enum class for <em>Market Marbles</em> of <code>MarketBoard</code><br>
 *  Each Marble but White is mapped to a different <code>Resource</code>
 *  <ul>
 *  <li>{@link #WHITE}
 *  <li>{@link #BLUE}
 *  <li>{@link #GRAY}
 *  <li>{@link #YELLOW}
 *  <li>{@link #PURPLE}
 *  <li>{@link #RED}
 *  </ul>
 */
public enum Marble {

    /**
     * According to the official rulebook this <em>Marble</em> hasn't a mapped Resource. <br>
     * Any possible mapping to a resource due to dedicated leader effect is done in the <code>MarketBoard</code>
     */
    WHITE(Resource.EMPTY),
    /**
     * This <em>Marble</em> is mapped to the <em>SHIELD</em> Resource
     */
    BLUE(Resource.SHIELD),

    /**
     * This <em>Marble</em> is mapped to the <em>STONE</em> Resource
     */
    GRAY(Resource.STONE),

    /**
     * This <em>Marble</em> is mapped to the <em>GOLD</em> Resource
     */
    YELLOW(Resource.GOLD),

    /**
     * This <em>Marble</em> is mapped to the <em>SERVANT</em> Resource
     */
    PURPLE(Resource.SERVANT),

    /**
     * This <em>Marble</em> is mapped to the <em>FAITH</em> Resource
     */
    RED(Resource.FAITH);

    private final Resource mappedResource;

    Marble(final Resource mappedResource) {
        this.mappedResource = mappedResource;
    }

    /** Returns
      * @return mapped Resource corresponding to the <em>Marble</em> on which method is called
    */
    public Resource getConvertedMarble() {
        return this.mappedResource;
    }
}
