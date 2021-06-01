package it.polimi.ingsw.server.messages.clienttoserver.events;

import it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent.LeaderValidation;
import it.polimi.ingsw.server.model.states.State;
import it.polimi.ingsw.server.model.GameModel;

import java.util.UUID;

/**
 * Client side {@link it.polimi.ingsw.network.messages.clienttoserver.events.Event Event} created when a {@link State#INITIAL_PHASE INITIAL_PHASE}
 * or {@link State#FINAL_PHASE FINAL_PHASE} game turn action is performed and has
 * to be processed to accomplish server-side client validation. Both game phases events are handled by the same
 * validation handler since most of phases are in common, as seen in {@link State States}.
 */
public class InitialOrFinalPhaseEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.InitialOrFinalPhaseEvent implements Validable, LeaderValidation {

    @Override
    public boolean validate(GameModel gameModel) {
        return isGameStarted(gameModel) && propagateValidation(gameModel);
    }

    public int getChoice(){
        return choice;
    }

    public UUID getLeaderId(){
        return leaderId;
    }

    private boolean propagateValidation(GameModel gameModel){

        if(choice == 0){

            return  isGameStarted(gameModel)
                    && choice==0
                    && validateLeaderNumber(gameModel, leaderId)
                    && validateLeaderAvailability(gameModel, leaderId);
        }

        else if(choice == 1){

            return  isGameStarted(gameModel)
                    && validateLeaderAvailability(gameModel, leaderId)
                    && validateLeaderNumber(gameModel, leaderId)
                    && validateLeaderRequirements(gameModel, leaderId);

        }

        else return choice == 2;
    }


}
