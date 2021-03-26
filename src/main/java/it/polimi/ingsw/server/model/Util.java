package it.polimi.ingsw.server.model;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Util
{
    // Implementing Fisher–Yates shuffle
    public static void shuffleArray(Marble []marbles)
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



}