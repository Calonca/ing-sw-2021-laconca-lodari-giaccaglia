package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.market.Marble;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Util
{
    // Implementing Fisher–Yates shuffle
    public static void shuffleArray(Marble[]marbles)
    {
        Random rnd = ThreadLocalRandom.current();
        for (int i = marbles.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            Marble marble = marbles[index];
            marbles[index] = marbles[i];
            marbles[i] = marble;
        }
    }

    /**
     * Returns the sum of two arrays a and b, if the dimensions differ it will sum with zero
     * if both array are shorten then the given length zeroes will be added to them
     * @param a the first array
     * @param b the second array
     * @param length the requested lenght for the sum array
     * @return sum of two arrays
     */
    public static int[] sumArray(final int[] a,final int[] b,int length){
        final int max = Math.max(Math.max(a.length, b.length), length);
        final int[] aa = IntStream.concat(Arrays.stream(a),IntStream.generate(()->0)).limit(max).toArray();
        final int[] bb = IntStream.concat(Arrays.stream(b),IntStream.generate(()->0)).limit(max).toArray();
        return IntStream.range(0,max).map((i)->aa[i]+ bb[i]).toArray();
    }

}