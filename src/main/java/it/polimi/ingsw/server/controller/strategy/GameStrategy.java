package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

public abstract interface GameStrategy
{

    public  abstract State execute(GameModel gamemodel);
}
