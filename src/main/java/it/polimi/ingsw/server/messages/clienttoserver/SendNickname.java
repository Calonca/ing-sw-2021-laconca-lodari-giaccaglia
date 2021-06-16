package it.polimi.ingsw.server.messages.clienttoserver;

import it.polimi.ingsw.network.messages.servertoclient.MatchesData;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.controller.SessionController;

import java.io.IOException;

public class SendNickname extends it.polimi.ingsw.network.messages.clienttoserver.SendNickname implements ServerMessage {

    public SendNickname(String nickname) {
        super(nickname);
    }

    @Override
    public void processMessage(ClientHandler clientHandler) throws IOException {
        clientHandler.setNickname(nickname);
        clientHandler.sendAnswerMessage(new MatchesData(SessionController.getInstance().matchesData(clientHandler)));
    }
}
