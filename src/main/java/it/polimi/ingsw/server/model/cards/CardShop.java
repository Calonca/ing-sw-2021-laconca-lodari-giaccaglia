package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.solo.SoloActionToken;
import it.polimi.ingsw.server.model.states.State;
import it.polimi.ingsw.server.utils.Deserializator;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Represents a matrix like data structure to store the purchasable {@link DevelopmentCard DevelopmentCards} during
 * player's turn. Cards are stored in {@link DevelopmentCardDeck DevelopmentCardDecks} according to
 * {@link DevelopmentCardColor} and {@link DevelopmentCard#level CardLevel}
 */
public class CardShop {

    /**
     * Indicates the maximum level a {@link DevelopmentCardColor} inside <em>devDecks</em> may have.
     */
    private int maxLevel;


    /**
     * Temporary variable to store purchased {@link DevelopmentCard} during {@link State}
     */
    private DevelopmentCard purchasedCard = null;

    public CardShop(Map<DevelopmentCardColor, Map<Integer, DevelopmentCardDeck>> devDecks) {
        this.maxLevel = 4;
        this.devDecks = devDecks;
    }

    public static CardShop initializeCardShop() throws IOException {

        return Deserializator.cardShopDeserialization();
    }

    
    /**
     * <p>Represents the actual structure of the grid, implemented by using a {@link Map} where values, {@link List Lists} of
     * <em>Decks</em> with same color cards, are accessed through {@link DevelopmentCardColor} as the key.<br>
     * Each List member is a Deck containing {@link DevelopmentCard DevelopmentCards} of the same level. No different decks
     * inside the same List have the same level value.
     */
        private Map<DevelopmentCardColor, Map<Integer, DevelopmentCardDeck>> devDecks;

    /**
     * <p>Solo mode method to discard development cards from the <em>CardShop</em> when dedicated
     * {@link SoloActionToken SoloActionTokens}effects are applied.<br>
     * After the effectively removal process takes place, {@link CardShop#isColorOutOfStock isColorOutOfStock} method
     * verifies if one type of {@link DevelopmentCardColor} is no longer available in the grid. In that case the game
     * ends immediately and the solo player loses the game.</p>
     *
     * @param color The {@link DevelopmentCardColor} type to be discarded.
     * @param amount Specified number of cards to be discarded.
     */
    public void discardCard(DevelopmentCardColor color, int amount){

        // TODO IMPLEMENTARE LOGICA SINGLE PLAYER DI FINE GIOCO
        int discarded = 0;
        for(int i=0; i<amount; i++){
            for(int j = 1; j<= maxLevel; j++) {

                if (!devDecks.get(color).get(j).isDeckEmpty()) {
                    devDecks.get(color).get(j).getCard();
                    discarded++;
                    break;
                }
            }
        }
    }

    public int getMaxCardLevel(){
        return maxLevel;
    }

    /**
     * Method for purchasing a {@link DevelopmentCard}. Firstly the card is chosen,
     * then the payment process takes place.
     *
     * @param color Color of the {@link DevelopmentCard} to purchase.
     * @param level {@link DevelopmentCard#level} of the {@link DevelopmentCard} to purchase.
     * Has to be lower than the {@link CardShop#maxLevel} of this CardShop.
     */
    public void purchaseCard(DevelopmentCardColor color, int level){
        purchasedCard = devDecks.get(color).get(level).getCardCopyOnTop();
    }



    public DevelopmentCard getCopyOfCardOnTop(DevelopmentCardColor color, int level){
        return devDecks.get(color).get(level).getCardCopyOnTop();
    }

    public boolean isDeckEmpty(DevelopmentCardColor color, int level){
        return devDecks.get(color).get(level).isDeckEmpty();
    }

    public boolean isLevelOfColourOutOfStock(DevelopmentCardColor color, int level){
        if(level <= maxLevel)
            return devDecks.get(color).get(level).isDeckEmpty();
        else
            return false;
    }

    /**
     * Method to retrieve previously selected card through {@link CardShop#discardCard},
     * after resources' payment has completed successfully.
     *
     * @return purchased {@link DevelopmentCard}
     */

    public DevelopmentCard takePurchasedCard(){
        return devDecks.get(purchasedCard.getCardType()).get(purchasedCard.getLevel()).getCard();
    }

    public DevelopmentCard getCopyOfPurchasedCard(){
        return Objects.nonNull(purchasedCard) ? (new DevelopmentCard(purchasedCard)) : null;
    }


    /**
     * <p>Checks if one type of {@link DevelopmentCardColor} is no longer available in
     * {@link CardShop#devDecks devDecks}, meaning that a color list inside {@link CardShop#devDecks devDecks}
     * has only empty {@link DevelopmentCardDeck Decks}.
     *
     * @return true if a list of {@link DevelopmentCardDeck DevelopmentCardDecks} has only empty Decks, otherwise false.
     */
    public boolean isSomeColourOutOfStock(){
        return Arrays.stream(DevelopmentCardColor.values()).filter(color -> !color.equals(DevelopmentCardColor.INVALID))
                .anyMatch(this::isColorOutOfStock);
    }

    /**
     * Gets no longer available {@link DevelopmentCardColor} by checking each List of
     * {@link DevelopmentCardDeck DevelopmentCardDecks}. Has to be invoked only if
     * {@link CardShop#isColorOutOfStock isColorOutOfStock} method invocation has returned true, meaning that a color
     * no longer available actually exists.
     *
     * @return {@link DevelopmentCardColor} no longer available in the {@link CardShop#devDecks devDecks} structure.
     */
    public DevelopmentCardColor getColorOutOfStock() {
        return Arrays.stream(DevelopmentCardColor.values())
                .filter(this::isColorOutOfStock)
                .findFirst()
                .get();
    }

    /**
     * <p>Checks the specified type of {@link DevelopmentCardColor} is no longer available in
     * {@link CardShop#devDecks devDecks}, meaning that the corresponding color list inside {@link CardShop#devDecks devDecks}
     * has only empty {@link DevelopmentCardDeck Decks}.
     *
     * @param color {@link DevelopmentCardColor} to check availability.
     *
     * @return true if the specified color list of {@link DevelopmentCardDeck DevelopmentCardDecks} has only empty Decks, otherwise false.
     */
    public boolean isColorOutOfStock(DevelopmentCardColor color){
        return devDecks.get(color).values()
                .stream()
                .allMatch(DevelopmentCardDeck::isDeckEmpty);
    }

    public Map<DevelopmentCardColor, Map<Integer, Pair<Integer, List<DevelopmentCard>>>> getSimpleCardShop(){

        return devDecks.keySet()
                .stream()
                .collect(
                        Collectors.toMap(colorKey -> colorKey ,  colorKey ->
                    devDecks.get(colorKey)
                            .keySet()
                            .stream()
                            .collect(Collectors.toMap(intKey -> intKey,

                                    intKey-> {



                              int deckSize = devDecks.get(colorKey).get(intKey).getDeckSize();


                              int startOfRange = (deckSize>2) ? (deckSize-2) : 0;


                              List<DevelopmentCard> cards = IntStream.range(startOfRange, deckSize)
                                        .boxed()
                                        .sorted(Collections.reverseOrder())
                                        .map(
                                        position ->  devDecks.get(colorKey).get(intKey).getCardCopyFromPosition(position))
                                        .filter(Optional::isPresent).map(Optional::get)
                                        .collect(Collectors.toList());

                              return new Pair<>(deckSize, cards);

                            }

                        ))));

    }
}
