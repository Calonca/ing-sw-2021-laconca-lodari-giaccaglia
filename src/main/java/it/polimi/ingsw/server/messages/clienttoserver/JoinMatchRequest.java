package it.polimi.ingsw.server.messages.clienttoserver;

import it.polimi.ingsw.network.messages.servertoclient.JoinStatus;
import it.polimi.ingsw.network.messages.servertoclient.MatchesData;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.controller.SessionController;

import java.io.IOException;
import java.util.UUID;

public class JoinMatchRequest extends it.polimi.ingsw.network.messages.clienttoserver.JoinMatchRequest implements ServerMessage {


    public JoinMatchRequest(UUID matchId, String nickName) {
        super(matchId, nickName);
    }

    @Override
    public void processMessage(ClientHandler clientHandler) throws IOException {
        int playerIndex = SessionController.getInstance().addPlayerToMatch(matchId,nickName,clientHandler);
        if(playerIndex!=-1) {
            SessionController.getInstance().notifyPlayersInLobby(clientHandler);
            clientHandler.sendAnswerMessage(new JoinStatus(this, matchId, null, playerIndex));
            clientHandler.getMatch().ifPresent(m->
                    SessionController.getInstance().startMatchAndNotifyStateIfPossible(m)
                    );
        }else {
            clientHandler.sendAnswerMessage(new JoinStatus(this, null, JoinStatus.motive.OTHER, 0));
            clientHandler.sendAnswerMessage(new MatchesData(SessionController.getInstance().matchesData(clientHandler)));
        }
    }
}
