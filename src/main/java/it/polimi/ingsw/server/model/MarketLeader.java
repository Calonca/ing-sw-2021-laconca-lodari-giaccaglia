package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.player.LeaderState;
import javafx.util.Pair;

import java.util.List;

/**
 * This Leader class doesn't need extra methods
 */

public class MarketLeader extends Leader
{
    private LeaderState state;
    public List<Pair<Resource, Integer>> requirementsResources;
    public List<Pair<DevelopmentCardColor, Integer>> requirementsCards;
    private Resource bonus;
}