package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.player.LeaderState;
import javafx.util.Pair;

import java.util.List;

/**
 * Abstract Leader Class. Each leader may override the "basic" methods according to its needs
 */

public abstract class Leader
{
    private LeaderState state;
    private int victoryPoints;
    public List<Pair<Resource, Integer>> requirements;



    public LeaderState getState()
    {
        return state;
    }

    /**
     * Basic Leader activation method
     * @param gamemodel != NULL
     * Ensures that the leader's status is ACTIVE after the call
     */

    public void activate(GameModel gamemodel)
    {
        state = LeaderState.ACTIVE; //assumo che il leader attivato sia "in cima"
    }
    /**
     * Basic Leader discard method. It's the same for all leaders
     * @param gamemodel!= NULL
     * Ensures that the Player's Faith Points are increased by 1
     */
    public void discard(GameModel gamemodel)
    {
        state = LeaderState.DISCARDED;
        //gamemodel.addBadFaith;
    }


    /*/**
     * Every requirement is checked before activating a leader
     * @param player != NULL
     * Returns True if the player has at least all the resources in each pair, in the given quantity
     */
    /*public boolean areRequirementsSatisfied(GameModel gamemodel)
    {
        for (Pair<Object, Integer> requirement : requirements)
        {
            if (player.getPersonalBoard().numOfResourcesOfType(requirement.getkey) < requirement.getvalue)
                return false;
        }
        return true;

    }*/

}
