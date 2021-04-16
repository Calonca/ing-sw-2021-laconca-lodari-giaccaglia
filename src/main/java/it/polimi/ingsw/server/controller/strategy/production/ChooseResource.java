package it.polimi.ingsw.server.controller.strategy.production;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

public class ChooseResource extends ProductionStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT CHOOSERESOURCEFORCHOICEEVENT
        return State.CHOOSING_CARD_FOR_PRODUCTION;
    }
}