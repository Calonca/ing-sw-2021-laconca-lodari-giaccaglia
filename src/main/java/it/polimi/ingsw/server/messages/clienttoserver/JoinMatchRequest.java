package it.polimi.ingsw.server.messages.clienttoserver;

import it.polimi.ingsw.network.messages.servertoclient.JoinStatus;
import it.polimi.ingsw.network.messages.servertoclient.MatchesData;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.controller.Match;
import it.polimi.ingsw.server.controller.SessionController;

import java.io.IOException;
import java.util.UUID;

public class JoinMatchRequest extends it.polimi.ingsw.network.messages.clienttoserver.JoinMatchRequest implements ServerMessage {


    public JoinMatchRequest(UUID matchId, String nickName) {
        super(matchId, nickName);
    }

    @Override
    public void processMessage(ClientHandler clientHandler) throws IOException {

        SessionController sessionController = SessionController.getInstance();

        if(sessionController.getMatch(matchId).isEmpty()){
            notifyRefusedJoin(clientHandler, JoinStatus.motive.NOT_EXISTS);
            return;
        }

        Match match = sessionController.getMatch(matchId).get();

        if(!match.isNicknameAvailable(nickName)){
            notifyRefusedJoin(clientHandler, JoinStatus.motive.NICKNAME_ALREADY_EXISTING);
            return;
        }

        int playerIndex = sessionController.addPlayerToMatch(matchId,nickName,clientHandler);

        boolean isGameActive = match.isGameActive();

            if(playerIndex!=-1) {

                if(isGameActive) {  //at least one player is already in game or  at least one player was previously online and playing.

                    clientHandler.sendAnswerMessage(new JoinStatus(this, matchId, JoinStatus.motive.SUCCESS, playerIndex));
                    clientHandler.getMatch().ifPresent(m-> sessionController.joinMatchAndNotifyStateIfPossible(m, nickName));

                }
                else {

                    sessionController.notifyPlayersInLobby(clientHandler);
                    clientHandler.sendAnswerMessage(new JoinStatus(this, matchId, JoinStatus.motive.SUCCESS, playerIndex));
                    clientHandler.getMatch().ifPresent(sessionController::startMatchAndNotifyStateIfPossible);
                }

            }

        else
            notifyRefusedJoin(clientHandler, JoinStatus.motive.ALREADY_FULL);

    }

    private void notifyRefusedJoin(ClientHandler clientHandler, JoinStatus.motive motive) throws IOException {

        clientHandler.sendAnswerMessage(new JoinStatus(this, null, motive, -1));
        clientHandler.sendAnswerMessage(new MatchesData(SessionController.getInstance().matchesData(clientHandler)));

    }
}
