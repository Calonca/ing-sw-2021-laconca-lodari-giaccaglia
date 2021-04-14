package it.polimi.ingsw.controller.strategy.cardmarket;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.player.State;

public class AcquireCard extends CardMarketStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT CHOOSECARDEVENT
        //RICEVO MESSAGGIO INTERO 4
        //CARDS ARE MAPPED AS FOLLOWS       INT%3 IS THE LEVEL, INT/3 IS THE COLOR
        //
        int chosencard=4;
        int level=4%3; //1      CARTA LIVELLO 2
        int color=4/3; //1      BLU
        gamemodel.getCardShop().purchaseCard(DevelopmentCardColor.fromInt(color),level);
        //if(gamemodel.getCardShop().pickPurchasedCard().isAvailable(gamemodel.getCurrentPlayer().getPersonalBoard()));
            ////vengono rimosse le risorse


        return State.CHOOSING_RESOURCES_FOR_DEVCARD;
    }
}