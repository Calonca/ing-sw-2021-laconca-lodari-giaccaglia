package it.polimi.ingsw.controller.strategy.leader;

import it.polimi.ingsw.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

public abstract class LeaderStrategy extends GameStrategy
{
    public abstract State execute(GameModel gamemodel);

}
