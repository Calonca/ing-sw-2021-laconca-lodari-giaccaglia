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
        int playerIndex = SessionController.getInstance().addPlayerToMatchAndStartWhenReady(matchId,nickName,clientHandler);
        if(playerIndex!=-1) {
            clientHandler.sendAnswerMessage(new JoinStatus(this, matchId, null, playerIndex));
        }else {
            clientHandler.sendAnswerMessage(new JoinStatus(this, null, JoinStatus.motive.OTHER, 0));
            clientHandler.sendAnswerMessage(new MatchesData(SessionController.getInstance().matchesData(clientHandler)));
        }
    }
}
