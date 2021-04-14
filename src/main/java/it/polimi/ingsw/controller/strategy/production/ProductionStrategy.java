package it.polimi.ingsw.controller.strategy.production;

import it.polimi.ingsw.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

public abstract class ProductionStrategy extends GameStrategy
{
    public abstract State execute(GameModel gamemodel);

}
