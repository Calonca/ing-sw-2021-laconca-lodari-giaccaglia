package it.polimi.ingsw.server.controller.strategy.cardmarket;

import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * This implementation allows the user to choose one resource at a time. As soon as the requirements are met,
 * the resources are taken from their Depots and Strongbox, transitioning to the next game phase.
 */
public class PayingResourcesForDevelopmentCard implements GameStrategy {

    List<Element> elementsToUpdate = new ArrayList<>();

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event) throws EventValidationFailedException
    {
        event.validate(gamemodel);

  /*      //ON EVENT CHOOSERESOURCEEVENT
        //MESSAGE IS POSITION AND 0 OR 1 FOR DEPOT OR STRONGBOX. IF POSITION IS 0 CHECKS FOR VALIDATION
     //   boolean isDepot=false;
    //    int msg=3;
            if(isDepot)
            gamemodel.getCurrentPlayer().getPersonalBoard().getWarehouseLeadersDepots().selectResourceAt(msg);
        else
            gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().selectResourceAt(msg);

        // need total of resource selected and remove all selected for warehouse leader depots
        if(msg==0)
        for(int i=0; i<gamemodel.getPurchasedCard().getCostList().size();i++)
            if(gamemodel.getPurchasedCard().getCostList().get(i).getValue()==
                    gamemodel.getCurrentPlayer().getPersonalBoard().getWarehouseLeadersDepots().getTotalSelected()+
                            gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().getNSelected(gamemodel.getPurchasedCard().getCostList().get(i).getKey()))
            {*/
        //TODO
        gamemodel.getCurrentPlayer().getPersonalBoard().getWarehouseLeadersDepots().removeSelected();
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().removeSelected();
        return new Pair<>(State.CHOOSING_POSITION_FOR_DEVCARD, elementsToUpdate);

    }
}