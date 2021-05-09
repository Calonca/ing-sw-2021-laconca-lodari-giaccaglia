package it.polimi.ingsw.server.model.utils;
import it.polimi.ingsw.server.model.market.Marble;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Util
{
    /**
     * Returns the sum of two arrays a and b, an array of the given length<br>
     * If the dimensions differ it will sum with zero.<br>
     * If any array is shorten then the given length zeroes will be added until reaching the given length.<br>
     * If any array is longer than the given length the return array will be cut to length.<br>
     * @param a the first array, != null
     * @param b the second array, != null
     * @param len the requested length for the sum array, positive
     * @return sum of two arrays
     */
    public static int[] sumArray(final int[] a,final int[] b,int len){
        final int[] aa = IntStream.concat(Arrays.stream(a),IntStream.generate(()->0)).limit(len).toArray();
        final int[] bb = IntStream.concat(Arrays.stream(b),IntStream.generate(()->0)).limit(len).toArray();
        return IntStream.range(0,len).map((i)->aa[i]+ bb[i]).toArray();
    }

}