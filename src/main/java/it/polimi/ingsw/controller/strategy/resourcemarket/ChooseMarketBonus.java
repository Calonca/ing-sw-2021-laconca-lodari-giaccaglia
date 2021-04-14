package it.polimi.ingsw.controller.strategy.resourcemarket;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.market.MarketLine;
import it.polimi.ingsw.server.model.player.State;
import javafx.util.Pair;

public class ChooseMarketBonus extends ResourceMarketStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT CHOOSEWHITEMARBLECONVERSIONEVENT
        //RICEVO MESSAGGIO RISORSA Resource.SHIELD (DI CUI CONTROLLO LA CORRETTEZZA)

        //gamemodel.getBoxResourcesFromMarketBoard().selectN(1,Resource.TOCHOOSE);
        //gamemodel.getBoxResourcesFromMarketBoard().removeSelected();
        //gamemodel.getBoxResourcesFromMarketBoard().addResource(new Pair<>(1, Resource.SHIELD));

        gamemodel.convertWhiteMarbleInPickedLine(Resource.SHIELD);

        //if(gamemodel.getBoxResourcesFromMarketBoard().getNumberOf(Resource.TOCHOOSE)>0)
        if(gamemodel.areThereWhiteMarblesInPickedLine())
            return State.CHOOSING_WHITEMARBLE_CONVERSION;


        return State.CHOOSING_POSITION_FOR_RESOURCES;
    }
}
