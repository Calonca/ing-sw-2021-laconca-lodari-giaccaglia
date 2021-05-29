package it.polimi.ingsw.server.controller.strategy.resourcemarket;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.controller.strategy.Final;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.List;

/**
 * This implementation calculates the movements on the Faith Track based on the resources left in the
 * discardbox.
 */
public class DiscardingResources implements GameStrategy {
    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event)
    {

        //TODO
        gamemodel.getCurrentPlayer().getPersonalBoard().discardResources();
        int temp=gamemodel.getCurrentPlayer().getPersonalBoard().getBadFaithToAdd();
        for(int i=0;i<temp;i++)
        {
            gamemodel.addFaithPointToOtherPlayers();
            //check vatican report
            //gamemodel.haswon;
            //if someone won, set state
        }

        temp=gamemodel.getCurrentPlayer().getPersonalBoard().getFaithToAdd();
        for(int i=0;i<temp;i++)
        {
            gamemodel.getCurrentPlayer().moveOnePosition();
            //check vatican report
            //gamemodel.haswon;
        }

        return new Final().execute(gamemodel, event);

    }


}
