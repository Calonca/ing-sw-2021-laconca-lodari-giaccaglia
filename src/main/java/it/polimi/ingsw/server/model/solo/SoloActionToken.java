package it.polimi.ingsw.server.model.solo;
import it.polimi.ingsw.network.assets.devcards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.server.model.player.track.FaithTrack;


/**
 *  <p>Enum class for <em>Solo Action Tokens</em> for the <em>Solo Mode</em>.<br>
 *  Each Token represents an effect applied once it is picked from the {@link SinglePlayerDeck}.</p>
 *  <ul>
 *  <li>{@link #DISCARD2GREEN}
 *  <li>{@link #DISCARD2BLUE}
 *  <li>{@link #DISCARD2YELLOW}
 *  <li>{@link #DISCARD2PURPLE}
 *  <li>{@link #SHUFFLE_ADD1FAITH}
 *  <li>{@link #ADD2FAITH}
 *  </ul>
 */
public enum SoloActionToken {

    /**
     * When this effect is applied 2 green {@link DevelopmentCard DevelopmentCards} are discarded
     * from the belonging {@link DevelopmentCardDeck} following the {@link #applyEffect(GameModel) applyEffect} rules.
     */
    DISCARD2GREEN{

        final DevelopmentCardColor color = DevelopmentCardColor.GREEN;

        @Override
        public void applyEffect(GameModel gameModel) {
            discardCards(color, gameModel, 2);
        }
    },

    /**
     * When this effect is applied 2 blue {@link DevelopmentCard DevelopmentCards}  are discarded
     * from the belonging {@link DevelopmentCardDeck} following the {@link #applyEffect(GameModel) applyEffect} rules.
     */
    DISCARD2BLUE {

        final DevelopmentCardColor color = DevelopmentCardColor.BLUE;

        @Override
        public void applyEffect(GameModel gameModel) {
            discardCards(color, gameModel, 2);
        }
    },

    /**
     * When this effect is applied 2 yellow {@link DevelopmentCard DevelopmentCards} are discarded
     * from the belonging {@link DevelopmentCardDeck} following the
     * {@link #discardCards(DevelopmentCardColor, GameModel, int) discardCardFromShop} rules.
     */
    DISCARD2YELLOW{

        final DevelopmentCardColor color = DevelopmentCardColor.YELLOW;

        @Override
        public void applyEffect(GameModel gameModel) {
            discardCards(color, gameModel, 2);
        }
    },

    /**
     * When this effect is applied 2 purple {@link DevelopmentCard DevelopmentCards} are discarded
     * from the belonging {@link DevelopmentCardDeck} following the
     * {@link #discardCards(DevelopmentCardColor, GameModel, int) discardCardFromShop} rules.
     */
    DISCARD2PURPLE{
        final DevelopmentCardColor color = DevelopmentCardColor.PURPLE;

        @Override
        public void applyEffect(GameModel gameModel) {
            discardCards(color, gameModel, 2);

        }
    },

    /**
     * When this effect is applied the {@link FaithTrack#lorenzoPiece lorenzoPiece} of the {@link FaithTrack}, representing the
     * <em>Faith Marker</em> of <em>Lorenzo il Magnifico</em>, is moved forward by 1 spaces through the
     * {@link GameModel} method  {@link GameModel#addFaithPointToOtherPlayers() addFaithPointToOtherPlayers}.<br>
     * Finally the {@link SinglePlayerDeck} is shuffled by calling the {@link GameModel#shuffleSoloDeck() shuffleSoloDeck}
     * method, in order to create a new order in stack.
     */
    SHUFFLE_ADD1FAITH{
        @Override
        public void applyEffect(GameModel gameModel) {
            gameModel.addFaithPointToOtherPlayers();
            gameModel.shuffleSoloDeck();
        }
    },

    /**
     * When this effect is applied the {@link FaithTrack#lorenzoPiece lorenzoPiece} of the {@link FaithTrack}, representing the Faith Marker
     * of <em>Lorenzo il Magnifico</em>, is moved forward by 2 spaces by calling the {@link GameModel} method
     * {@link GameModel#addFaithPointToOtherPlayers() addFaithPointToOtherPlayers}.
     */
    ADD2FAITH {
        @Override
        public void applyEffect(GameModel gameModel) {
            gameModel.addFaithPointToOtherPlayers();
            gameModel.addFaithPointToOtherPlayers();
        }
    };

    /**
     * Abstract method overridden by the <em>ActionTokens</em> to implement the specific effect associated with.
     * @param gameModel The {@link GameModel} of the current game, on which the effect is applied in solo mode.
     */
    public abstract void applyEffect(GameModel gameModel);

    /**
     * When this effect is applied, due to an <em>Action Token</em>, {@link DevelopmentCard}s of the indicated type
     * are discarded from the belonging {@link DevelopmentCardColor} located in the
     * {@link CardShop}, starting from the bottom of the grid, from the lowes level to the highest.
     *
     * @param color The {@link DevelopmentCardColor} of the cards to be discarded.
     * @param gameModel The {@link GameModel} of the current game, through which the
     * {@link GameModel#discardCardFromShop(DevelopmentCardColor, int) discardCardFromShop}
     * method is accessed to modify the {@link CardShop}.
     * @param amount The number of {@link DevelopmentCard}s to discard.
     */
    public void discardCards(DevelopmentCardColor color, GameModel gameModel, int amount){
        gameModel.discardCardFromShop(color, amount);
    }
}


