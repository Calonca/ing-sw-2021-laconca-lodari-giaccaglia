package it.polimi.ingsw.server.model.player.leaders;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import javafx.util.Pair;

import java.util.List;

/**
 * Abstract Leader Class. Each leader may override the "basic" methods according to its needs
 */

public abstract class Leader{

    protected LeaderState state;
    protected int victoryPoints;
    protected List<Pair<Resource, Integer>> requirementsResources;
    protected List<Pair<DevelopmentCardColor, Integer>> requirementsCards;
    protected int requirementsCardsLevel=1;

    public LeaderState getState(){
        return state;
    }

    public List<Pair<Resource, Integer>> getRequirementsResources() {
        return requirementsResources;
    }

    public List<Pair<DevelopmentCardColor, Integer>> getRequirementsCards() {
        return requirementsCards;
    }

    public int getRequirementsCardsLevel() {
        return requirementsCardsLevel;
    }

    public int getLeaderPoints(){
        return victoryPoints;
    }

    /**
     * Basic Leader activation method
     * @param gamemodel != NULL
     * Ensures that the leader's status is ACTIVE after the call
     */
    public abstract void activate(GameModel gamemodel);

    /**
     * Basic Leader discard method. It's the same for all cards.leaders
     * @param gamemodel!= NULL
     * Ensures that the Player's Faith Points are increased by 1
     */
    public void discard(GameModel gamemodel){
        state = LeaderState.DISCARDED;
        gamemodel.getCurrentPlayer().moveOnePosition();
    }


}
