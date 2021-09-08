package it.polimi.ingsw.server.messages.clienttoserver;

import it.polimi.ingsw.network.messages.servertoclient.CreatedMatchStatus;
import it.polimi.ingsw.network.messages.servertoclient.MatchesData;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.controller.Match;
import it.polimi.ingsw.server.controller.SessionController;

public class CreateMatchRequest extends it.polimi.ingsw.network.messages.clienttoserver.CreateMatchRequest implements ServerMessage {

    public CreateMatchRequest(int maxPlayers, String nickName) {
        super(maxPlayers, nickName);
    }

    /**
     * Method invoked in the server to process the message.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {

        clientHandler.getMatch().ifPresentOrElse(
             match->{
                 //Player is already in a match, resend matches data
                 clientHandler.sendAnswerMessage(new MatchesData(SessionController.getInstance().matchesData(clientHandler)));
                 clientHandler.sendAnswerMessage(new CreatedMatchStatus(null, CreatedMatchStatus.motive.OTHER));
             },
             ()->{
                 //Match created successfully
                 Match match = SessionController.getInstance().addNewMatch(maxPlayers, clientHandler);
                 SessionController.getInstance().addPlayerToMatch(match.getMatchId(),nickName,clientHandler);
                 SessionController.getInstance().notifyPlayersInLobby();
                 clientHandler.sendAnswerMessage(new CreatedMatchStatus(match.getMatchId(),null));
                 clientHandler.sendAnswerMessage(new MatchesData(SessionController.getInstance().matchesData(clientHandler)));
                 SessionController.getInstance().startMatchAndNotifyStateIfPossible(match);

             });
    }
}
