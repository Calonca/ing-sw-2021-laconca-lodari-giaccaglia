package it.polimi.ingsw.network.assets.devcards;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Enum class for resources. Resources order is the same as in Box.
 * New resources are to be added after STONE.
 * For each TOCHOOSE the player will perform a choice, selecting a basic resource.
 * Each BADFAITH point will be consumed and all other players will advance on the FaithTrack.
 */

public enum NetworkResource
{
    GOLD(0),
    SERVANT(1),
    SHIELD(2),
    STONE(3),
    FAITH(4),
    BADFAITH(5),
    TOCHOOSE(6),
    EMPTY(7);

    private final int resourceNumber;
    /**
     * Indicated the number of "physical" resources
     */
    public static final int nRes = 4;


    /**
     * Array containing "physical" resources, used to get resource from it's number in the ordering.
     */
    private static final NetworkResource[] vals = NetworkResource.values();


    NetworkResource(final int resourceNumber)
    {
        this.resourceNumber= resourceNumber;
    }

    /**
     * Return the  {@link NetworkResource} corresponding to given value in the {@link NetworkResource} ordering,
     * returns {@link NetworkResource#EMPTY} if the given value is outside the array
     * @param rNum int representing the {@link NetworkResource} ordering
     * @return a {@link NetworkResource}
     */
    public static NetworkResource fromInt(int rNum){
        return rNum>vals.length||rNum<0 ? EMPTY: vals[rNum];
    }
    /**
     * Returns a {@link Stream} of {@link NetworkResource Resources} of the given size,
     * if size is greater than the number of {@link NetworkResource resources} concatenates {@link NetworkResource#EMPTY} until reaching the given size
     * @param size the size of the {@link Stream}
     * @return a {@link Stream} of {@link it.polimi.ingsw.server.model.Resource Resources}
     */
    public static Stream<NetworkResource> getStream(int size){
        return Stream.concat(Arrays.stream(vals),Stream.generate(()-> NetworkResource.EMPTY)).limit(size);
    }

    public int getResourceNumber()
    {
        return this.resourceNumber;
    }

}