package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.GameModel;

import java.util.*;

/**
 * Stack of {@link SoloActionToken SoloActionTokens} for <em>Solo Mode</em>, implemented using a {@link List}.
 */
public class SinglePlayerDeck {

    /**
     * <p>Represents the actual structure of the stack of {@link SoloActionToken SoloActionTokens}.
     */
    private final List<SoloActionToken> actionTokens = Arrays.asList(SoloActionToken.values());

    /**
     * New stack is created by shuffling the default order of {@link SoloActionToken SoloActionTokens}.
     */
    public SinglePlayerDeck() {
        shuffleActionTokens();
    }

    /**
     * Shuffles {@link SinglePlayerDeck#actionTokens} to change the default order of
     * {@link SoloActionToken SoloActionTokens}.
     */
    public void shuffleActionTokens(){
        Collections.shuffle(actionTokens);
    }

    /**
     * Returns {@link SoloActionToken} on top of tokens' stack, by getting the first element of
     * {@link SinglePlayerDeck#actionTokens} list.
     *
     * @return {@link SoloActionToken} on top of the stack
     */
    public SoloActionToken showToken(){
        SoloActionToken token;
        return token = actionTokens.get(0);
    }

    /**
     * Invokes the {@link SoloActionToken#applyEffect applyEffect} of top {@link SoloActionToken}
     *
     * @param gameModel The {@link GameModel} of the current game, on which the effect is applied.
     */
    public void activateToken(GameModel gameModel){
        actionTokens.get(0).applyEffect(gameModel);
    }

}
