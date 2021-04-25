package it.polimi.ingsw.server.messages.clienttoserver.events.leaderphaseevent;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.leaders.Leader;
import it.polimi.ingsw.server.model.player.leaders.LeaderState;

public interface LeaderValidation {

    default boolean validateLeaderNumber(GameModel gameModel, int leaderNumber){
        return validateNumber(gameModel, leaderNumber);
    }

    default boolean validateLeaderAvailability(GameModel gameModel, int leaderNumber){
        return validateAvailability(gameModel, leaderNumber);
    }

    default boolean validateLeaderRequirements(GameModel gameModel, int leaderNumber){
        return validateRequirements(gameModel, leaderNumber);
    }

    /**
     * Method to verify if chosen {@link PlayLeaderEvent#leaderNumber} is a legit value according to {@link Leader Leaders}
     * list size.
     * @return true if {@link PlayLeaderEvent#leaderNumber} has a positive value ranging inside {@link Leader Leaders}
     * list size, otherwise false.
     */
    private boolean validateNumber(GameModel gameModel, int leaderNumber){
        return (gameModel.getCurrentPlayer().getLeaders().size() > leaderNumber) && (leaderNumber>=0) && validateLeaderRequirements(gameModel, leaderNumber);
    }

    /**
     * Method to verify if chosen Leader is activable or discardable by checking the {@link LeaderState}.
     * Player shouldn't be able to select a previously discarded/activated leader.
     * If this happens, client is modified thus event has to be refused.
     * @return true if chosen {@link Leader}'s current {@link LeaderState} is {@link LeaderState#INACTIVE INACTIVE}, otherwise false.
     */
    private boolean validateAvailability(GameModel gameModel, int leaderNumber){

        if(validateLeaderNumber(gameModel, leaderNumber)) {
            LeaderState selectedLeaderState = gameModel.getCurrentPlayer().getLeaders().get(leaderNumber).getState();
            return selectedLeaderState.equals(LeaderState.INACTIVE);
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
    private boolean validateRequirements(GameModel gameModel, int leaderNumber){
        Leader requiredLeader = gameModel.getCurrentPlayer().getLeaders().get(leaderNumber);
        return gameModel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(requiredLeader);
    }
}
