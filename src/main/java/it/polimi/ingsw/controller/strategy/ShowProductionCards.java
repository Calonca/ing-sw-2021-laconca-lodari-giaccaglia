package it.polimi.ingsw.controller.strategy;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

public class ShowProductionCards extends GameStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT CHOOSEPRODUCTIONEVENT
        return State.CHOOSING_CARD_FOR_PRODUCTION;
    }
}
