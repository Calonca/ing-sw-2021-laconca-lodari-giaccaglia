package it.polimi.ingsw.server.model.market;

import it.polimi.ingsw.server.model.Resource;

import java.util.Arrays;

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
     * This <em>Marble</em> is mapped to the {@link Resource#SHIELD SHIELD} Resource
     */
    BLUE(Resource.SHIELD),

    /**
     * This <em>Marble</em> is mapped to the {@link Resource#STONE STONE} Resource
     */
    GRAY(Resource.STONE),

    /**
     * This <em>Marble</em> is mapped to the {@link Resource#GOLD GOLD} Resource
     */
    YELLOW(Resource.GOLD),

    /**
     * This <em>Marble</em> is mapped to the {@link Resource#SERVANT SERVANT} Resource
     */
    PURPLE(Resource.SERVANT),

    /**
     * This <em>Marble</em> is mapped to the {@link Resource#FAITH FAITH} Resource
     */
    RED(Resource.FAITH),


    INVALID(Resource.EMPTY);


    private final Resource mappedResource;
    private static final Marble[] vals = Marble.values();

    Marble(final Resource mappedResource) {
        this.mappedResource = mappedResource;
    }

    public static Marble fromInt(int marbleNum){
        return marbleNum>vals.length|| marbleNum<0 ? INVALID: vals[marbleNum];
    }

    public static int getMarbleNumber(Marble marble){
        return Arrays.asList(vals).indexOf(marble);
    }

    /** Returns
      * @return mapped {@link Resource} corresponding to the <em>Marble</em> on which method is called
    */
    public Resource getConvertedMarble() {
        return this.mappedResource;
    }

}
