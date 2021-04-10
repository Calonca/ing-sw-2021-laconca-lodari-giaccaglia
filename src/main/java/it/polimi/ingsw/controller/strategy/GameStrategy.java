package it.polimi.ingsw.controller.strategy;

import it.polimi.ingsw.server.model.GameModel;

public abstract class GameStrategy
{
    public abstract void execute(GameModel gamemodel);
}
