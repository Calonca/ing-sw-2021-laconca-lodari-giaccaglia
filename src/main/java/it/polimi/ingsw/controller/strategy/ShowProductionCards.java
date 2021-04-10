package it.polimi.ingsw.controller.strategy;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

public class ShowProductionCards extends GameStrategy
{
    public void execute(GameModel gamemodel)
    {
        //ON EVENT CHOOSEPRODUCTIONEVENT
        gamemodel.getCurrentPlayer().setCurrentState(State.CHOOSING_CARD_FOR_PRODUCTION);
    }
}
