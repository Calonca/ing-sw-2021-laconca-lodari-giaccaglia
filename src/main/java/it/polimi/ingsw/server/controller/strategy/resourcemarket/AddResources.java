package it.polimi.ingsw.server.controller.strategy.resourcemarket;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.State;
/**
 * This implementation allows the user to move one at a time the resources from one of the depot lines to an empty
 * space. Before being able to proceed, the user needs to place all of the resources from the market either in the
 * DiscardBox or in one of their depot lines. For each resource in the DiscardBox all the other players get a faith point
 */
public class AddResources extends ResourceMarketStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT MOVERESOURCEEVENT
        //MESSAGE IS TWO INT, STARTING POSITION AND ENDING POSITION. 0,0 IS FOR TERMINATION
        int msg1=3;
        int msg2=4;
        if(msg1!=0)
        {
            gamemodel.getCurrentPlayer().getPersonalBoard().move(msg1,msg2);
            return State.CHOOSING_POSITION_FOR_RESOURCES;
        }
        else
            for(Resource resource : Resource.values())
                if(gamemodel.getBoxResourcesFromMarketBoard().getNumberOf(resource)!=0)
                    return State.CHOOSING_POSITION_FOR_RESOURCES;
                else
                {
                    int k=0;
                    for(Resource resource2 : Resource.values())
                        k+=gamemodel.getCurrentPlayer().getPersonalBoard().getDiscardBox().getNumberOf(resource2);
                    for (int j=0; j<k;j++)
                        gamemodel.addFaithPointToOtherPlayers();
                    return State.FINAL_PHASE;
                }


        return State.CHOOSING_CARD_FOR_PRODUCTION;
    }}
