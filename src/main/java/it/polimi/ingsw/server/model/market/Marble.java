package it.polimi.ingsw.server.model.market;


import it.polimi.ingsw.server.model.Resource;

/**
 *  <p>Enum class for <em>Market Marbles</em> of  {@link MarketBoard }<br>
 *  Each Marble but White is mapped to a different {@link Resource}.
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
     * According to the official rulebook this <em>Marble</em> hasn't a mapped {@link Resource}. <br>
     * Any possible mapping to a resource due to dedicated leader effect is done in the{@link MarketBoard}
     */
    WHITE(Resource.EMPTY),
    /**
     * This <em>Marble</em> is mapped to the {@link Resource#SHIELD SHIELD} NetworkResource
     */
    BLUE(Resource.SHIELD),

    /**
     * This <em>Marble</em> is mapped to the {@link Resource#STONE STONE} NetworkResource
     */
    GRAY(Resource.STONE),

    /**
     * This <em>Marble</em> is mapped to the {@link Resource#GOLD GOLD} NetworkResource
     */
    YELLOW(Resource.GOLD),

    /**
     * This <em>Marble</em> is mapped to the {@link Resource#SERVANT SERVANT} NetworkResource
     */
    PURPLE(Resource.SERVANT),

    /**
     * This <em>Marble</em> is mapped to the {@link Resource#FAITH FAITH} NetworkResource
     */
    RED(Resource.FAITH);

    private final Resource mappedResource;

    Marble(final Resource mappedResource) {
        this.mappedResource = mappedResource;
    }

    /** Returns
      * @return mapped {@link Resource} corresponding to the <em>Marble</em> on which method is called
    */
    public Resource getConvertedMarble() {
        return this.mappedResource;
    }
}
