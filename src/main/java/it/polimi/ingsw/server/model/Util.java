package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import it.polimi.ingsw.server.model.cards.CardShop;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.market.Marble;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
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

    private List<DevelopmentCard> cardsFromJsonHandler(){
        Gson gson = new Gson();
        String Cards = null;
        try {
            Cards = Files.readString(Path.of("src/main/resources/config/DevelopmentCardConfig.json"), StandardCharsets.US_ASCII);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DevelopmentCard[] cardsArray = gson.fromJson(Cards, DevelopmentCard[].class);
        return (Arrays.asList(cardsArray));

    }

    public static void main(String[] args) {

        Util util = new Util();
        List<DevelopmentCard> cards = util.cardsFromJsonHandler();
        List<DevelopmentCard> blue = cards.stream().filter(card -> card.getCardType() == DevelopmentCardColor.BLUE).collect(Collectors.toList());
        List<DevelopmentCard> yellow = cards.stream().filter(card -> card.getCardType() == DevelopmentCardColor.YELLOW).collect(Collectors.toList());
        List<DevelopmentCard> green = cards.stream().filter(card -> card.getCardType() == DevelopmentCardColor.GREEN).collect(Collectors.toList());
        List<DevelopmentCard> purple = cards.stream().filter(card -> card.getCardType() == DevelopmentCardColor.PURPLE).collect(Collectors.toList());

      //  List<DevelopmentCard> blue1 = blue.stream().filter(blue -> blue.getLevel() == 1)
        int color = 5;
    }


}