package it.polimi.ingsw.controller.strategy;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

public class ChooseCard extends GameStrategy
{
    public void execute(GameModel gamemodel)
    {
        //ON EVENT CARDSHOPEVENT
        gamemodel.getCurrentPlayer().setCurrentState(State.SHOWING_CARD_SHOP);
    }
}
