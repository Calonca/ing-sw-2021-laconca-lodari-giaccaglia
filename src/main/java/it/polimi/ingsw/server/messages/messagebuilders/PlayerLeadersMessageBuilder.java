package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.leaders.LeaderState;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerLeadersMessageBuilder {

    public static Map<UUID, Boolean> playerLeaders(GameModel gameModel){

        return gameModel.getCurrentPlayer().getLeadersUUIDs().stream().collect(Collectors.toMap(
                leaderId -> leaderId,
                leaderId -> gameModel.getCurrentPlayer().getLeader(leaderId).get().getState().equals(LeaderState.ACTIVE)));

    }

}
