package it.polimi.ingsw.server.messages.clienttoserver;

import it.polimi.ingsw.network.messages.servertoclient.JoinStatus;
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
        if(SessionController.getInstance().addPlayerToMatch(matchId,nickName,clientHandler))
            clientHandler.sendAnswerMessage(new JoinStatus(this,true,null));
        else
            clientHandler.sendAnswerMessage(new JoinStatus(this,false, JoinStatus.motive.OTHER));
    }
}
