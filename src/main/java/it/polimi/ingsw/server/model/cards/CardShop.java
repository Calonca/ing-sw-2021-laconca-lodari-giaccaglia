package it.polimi.ingsw.server.model.cards;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.server.model.player.*;

/**
 * Represents a matrix like data structure to store the purchasable {@link DevelopmentCard DevelopmentCards} during
 * player's turn. Cards are stored in {@link DevelopmentCardDeck DevelopmentCardDecks} according to
 * {@link DevelopmentCardColor} and {@link DevelopmentCard#level CardLevel}
 */
public class CardShop {

    /**
     * Indicates the maximum level a {@link DevelopmentCardColor} inside <em>devDecks</em> may have.
     */
    private Integer maxLevel;


    /**
     * <p>Represents the actual structure of the grid, implemented by using a {@link Map} where values, {@link List Lists} of
     * <em>Decks</em> with same color cards, are accessed through {@link DevelopmentCardColor} as the key.<br>
     * Each List member is a Deck containing {@link DevelopmentCard DevelopmentCards} of the same level. No different decks
     * inside the same List have the same level value.
     */
    private Map<DevelopmentCardColor, List<DevelopmentCardDeck>> devDecks;


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
        int discarded = 0;
        for(int i=0; i<amount; i++){
            for(int j = 0; j< maxLevel; j++) {

                if (!devDecks.get(color).get(j).isDeckEmpty()) {
                    devDecks.get(color).get(j).getCard();
                    discarded++;
                    break;
                }
            }
        }
        if (discarded!= amount)
            isColorOutOfStock(color);
    }


    /**
     * <p>Checks if one type of {@link DevelopmentCardColor} is no longer available in
     * {@link CardShop#devDecks devDecks}, meaning that a color list inside {@link CardShop#devDecks devDecks}
     * has only empty {@link DevelopmentCardDeck Decks}.
     *
     * @return true if a list of {@link DevelopmentCardDeck DevelopmentCardDecks} has only empty Decks, otherwise false.
     */
    public boolean isSomeColourOutOfStock(){
        return Arrays.stream(DevelopmentCardColor.values())
                .map(color -> devDecks.get(color))
                .anyMatch(deckList -> deckList.stream()
                        .allMatch(DevelopmentCardDeck::isDeckEmpty));
    }

    /**
     * Gets no longer available {@link DevelopmentCardColor} by checking each List of
     * {@link DevelopmentCardDeck DevelopmentCardDecks}. Has to be invoked only if
     * {@link CardShop#isColorOutOfStock isColorOutOfStock} method invocation has returned true, meaning that a color
     * no longer available actually exists.
     *
     * @return {@link DevelopmentCardColor} no longer available in the {@link CardShop#devDecks devDecks} structure.
     */
    public DevelopmentCardColor getColourOutOfStock() {
        return Arrays.stream(DevelopmentCardColor.values())
                .filter(color -> devDecks.get(color)
                        .stream()
                        .allMatch(DevelopmentCardDeck::isDeckEmpty))
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
        return devDecks.get(color)
                .stream()
                .allMatch(DevelopmentCardDeck::isDeckEmpty);
    }

}
