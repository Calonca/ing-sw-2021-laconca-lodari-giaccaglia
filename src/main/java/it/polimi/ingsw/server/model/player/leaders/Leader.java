package it.polimi.ingsw.server.model.player.leaders;

import com.sun.glass.ui.EventLoop;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import javafx.util.Pair;
import java.util.List;

/**
 * Abstract Leader Class. Each leader may override the "basic" methods according to its needs
 * TODO AreRequirementsSatisfied Test
 */

public abstract class Leader
{


    public boolean isPast() {
        return past;
    }

    protected boolean past;
    protected LeaderState state;
    protected int victoryPoints;
    protected List<Pair<Resource, Integer>> requirementsResources;
    protected List<Pair<DevelopmentCardColor, Integer>> requirementsCards;
    protected int requirementsCardsLevel=1;


    public LeaderState getState(){
        return state;
    }

    /**
     * Basic Leader activation method
     * @param gamemodel != NULL
     * Ensures that the leader's status is ACTIVE after the call
     */
    public abstract void activate(GameModel gamemodel);

    /**
     * Basic Leader discard method. It's the same for all leaders
     * @param gamemodel!= NULL
     * Ensures that the Player's Faith Points are increased by 1
     */
    public void discard(GameModel gamemodel){
        state = LeaderState.DISCARDED;
        gamemodel.getCurrentPlayer().moveOnePosition();
    }

    /**
     * Every requirement is checked before activating a leader
     * @param gamemodel != NULL
     * Returns True if the player has at least all the resources in each pair, in the given quantity
     */
    public boolean areRequirementsSatisfied(GameModel gamemodel){
        int temp=0;
        //TODO FAR CICLARE NEL METODO? USO LA STESSA COSA ANCHE IN DEVELOPMENTCARD
        for (Pair<Resource, Integer> requirementsResource : requirementsResources) {
            if (gamemodel.getCurrentPlayer().getPersonalBoard().getNumberOf(requirementsResource.getKey()) < requirementsResource.getValue())
                return false;
        }
        for (Pair<DevelopmentCardColor, Integer> requirementsCard : requirementsCards) {
            for (int j = 0; j < gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().size(); j++)
                temp +=gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(j).howManyOfColor(requirementsCard.getKey(),requirementsCardsLevel);
            if (temp < requirementsCard.getValue())
                return false;
            temp=0;

        }
        return true;
    }

    public void setPast(boolean past) {
        this.past = past;
    }

    public boolean anyLeaderPlayable(GameModel gamemodel){
        for (Leader leader : gamemodel.getCurrentPlayer().getLeaders())
            if (leader.getState()==LeaderState.INACTIVE)
                return true;
        return false;
    }

}
