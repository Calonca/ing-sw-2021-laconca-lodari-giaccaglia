package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.GameModel;

import java.util.*;

public class SinglePlayerDeck {

    private final List<SoloActionToken> actionTokens = Arrays.asList(SoloActionToken.values());

    public SinglePlayerDeck() {
        shuffleActionTokens();
    }

    public void shuffleActionTokens(){
        Collections.shuffle(actionTokens);
    }

    public SoloActionToken showToken(){
        SoloActionToken token;
        return token = actionTokens.get(0);
    }

    public void activateToken(GameModel gameModel){
        actionTokens.get(0).applyEffect(gameModel);
    }

}
