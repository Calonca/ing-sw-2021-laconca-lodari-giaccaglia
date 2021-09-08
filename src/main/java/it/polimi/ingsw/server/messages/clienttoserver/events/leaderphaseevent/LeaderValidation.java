package it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.leaders.Leader;
import it.polimi.ingsw.server.model.player.leaders.LeaderState;

import java.util.Optional;
import java.util.UUID;

public interface LeaderValidation {

    default boolean validateLeaderNumber(GameModel gameModel, UUID leaderNumber){
        return validateLeaderId(gameModel, leaderNumber);
    }

    default boolean validateLeaderAvailability(GameModel gameModel, UUID leaderNumber){
        return validateAvailability(gameModel, leaderNumber);
    }

    default boolean validateLeaderRequirements(GameModel gameModel, UUID leaderNumber){
        return validateRequirements(gameModel, leaderNumber);
    }

    /**
     * Method to verify if chosen  is a legit value according to {@link Leader Leaders}
     * map key values.
     * @return true if  has a positive value ranging inside {@link Leader Leaders}
     * map key values, otherwise false.
     */
    private boolean validateLeaderId(GameModel gameModel, UUID leaderNumber){
        return (gameModel.getCurrentPlayer().getLeader(leaderNumber).isPresent());
    }

    /**
     * Method to verify if chosen Leader is activable or discardable by checking the {@link LeaderState}.
     * Player shouldn't be able to select a previously discarded/activated leader.
     * If this happens, client is modified thus event has to be refused.
     * @return true if chosen {@link Leader}'s current {@link LeaderState} is {@link LeaderState#INACTIVE INACTIVE}, otherwise false.
     */
    private boolean validateAvailability(GameModel gameModel, UUID leaderId){

        if(validateLeaderNumber(gameModel, leaderId)) {
            Optional<Leader> selectedLeaderState = gameModel.getCurrentPlayer().getLeader(leaderId);
            return selectedLeaderState.isPresent() && selectedLeaderState.get().getState().equals(LeaderState.INACTIVE);
        }
        else return false;
    }

    /**
     * Method to verify is chosen Leader level, colour and resources requirements are met by {@link GameModel#currentPlayer}.
     *
     *  @return true if {@link GameModel#currentPlayer}'s {@link it.polimi.ingsw.server.model.Resource Resources} and
     * {@link it.polimi.ingsw.server.model.cards.DevelopmentCard DevelopmentCards} satisfy leader activation requirements,
     * otherwise false.
     */
    private boolean validateRequirements(GameModel gameModel, UUID leaderNumber){
        Optional<Leader> requiredLeader = gameModel.getCurrentPlayer().getLeader(leaderNumber);

        return requiredLeader.isPresent() && gameModel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(requiredLeader.get());
    }
}
