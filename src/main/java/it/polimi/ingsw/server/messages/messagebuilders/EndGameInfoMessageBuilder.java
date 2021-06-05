package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.network.EndGameInfo;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EndGameInfoMessageBuilder {

    public static Map<Integer, EndGameInfo.PlayerInfo> endGameInfoMap(GameModel gameModel){

        Map<Integer, Player> players = gameModel.getMatchPlayers();

        return players.keySet().stream().collect(Collectors.toMap(

                playerIndex -> playerIndex, playerIndex -> {

                    Player player = players.get(playerIndex);

                    return new EndGameInfo.PlayerInfo(

                            player.getCurrentGamePoints(),
                            player.getMatchOutCome(),
                            player.getPlayerPosition(),
                            player.getLorenzoPosition()
                    );
                }
        ));

    }

    public static List<Integer> getPlayersEndingTheGame(GameModel gameModel) {
        return new ArrayList<>(gameModel.getPlayersEndingTheGame().keySet());
    }

}
