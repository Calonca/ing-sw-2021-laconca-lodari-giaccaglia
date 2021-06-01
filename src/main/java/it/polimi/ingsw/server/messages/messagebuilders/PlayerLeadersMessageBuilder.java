package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.leaders.LeaderState;
import javafx.util.Pair;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerLeadersMessageBuilder {

    //key -> leaderId
    //value -> Pair where :
                        //key -> true if leader state is ACTIVE, false if INACTIVE
                        //value -> true if INACTIVE leader is playable , otherwise false. If leader is ACTIVE, value is false


    public static Map<UUID, Pair<Boolean, Boolean>> playerLeadersMap(GameModel gameModel){

        return gameModel.getCurrentPlayer().getLeadersUUIDs().stream().collect(Collectors.toMap(
                leaderId -> leaderId,
                leaderId -> new Pair<>(
                        gameModel.getCurrentPlayer().getLeader(leaderId).get().getState().equals(LeaderState.ACTIVE),
                        gameModel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(gameModel.getCurrentPlayer().getLeader(leaderId).get()))));

    }

}
