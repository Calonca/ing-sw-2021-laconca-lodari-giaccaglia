package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.network.messages.servertoclient.State;

public abstract interface GameStrategy
{

    public  abstract State execute(GameModel gamemodel);
}
