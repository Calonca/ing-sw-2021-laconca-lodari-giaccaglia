package it.polimi.ingsw.server.model.solo;

import it.polimi.ingsw.server.model.GameModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stack of {@link SoloActionToken SoloActionTokens} for <em>Solo Mode</em>, implemented using a {@link List}.
 */
public class SinglePlayerDeck {

    /**
     * <p>Represents the actual structure of the stack of {@link SoloActionToken SoloActionTokens}.
     */
    private final List<SoloActionToken> actionTokens = Arrays.stream(SoloActionToken.values()).filter(token -> token!=SoloActionToken.EMPTY).collect(Collectors.toList());

    private SoloActionToken lastActivatedToken;

    /**
     * New stack is created by shuffling the default order of {@link SoloActionToken SoloActionTokens}.
     */
    public SinglePlayerDeck() {
        shuffleActionTokens();
        lastActivatedToken = SoloActionToken.EMPTY;

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
        return actionTokens.get(0);
    }

    public SoloActionToken showLastActivatedToken(){
        return lastActivatedToken;
    }

    /**
     * Invokes the {@link SoloActionToken#applyEffect applyEffect} of top {@link SoloActionToken}
     *
     * @param gameModel The {@link GameModel} of the current game, on which the effect is applied.
     */
    public void activateToken(GameModel gameModel){
        SoloActionToken actionToken = actionTokens.remove(0);
        lastActivatedToken = actionToken;
        actionTokens.add(actionToken);
        actionToken.applyEffect(gameModel);
    }

}
