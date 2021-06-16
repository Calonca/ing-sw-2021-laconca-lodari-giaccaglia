package it.polimi.ingsw.server.messages.clienttoserver;

import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.servertoclient.EventNotValid;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.controller.SessionController;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;

import java.io.IOException;

public class EventMessage extends ClientToServerMessage implements ServerMessage {
    protected Validable event;

    public Validable getEvent() {
        return event;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) throws IOException {
        clientHandler.getMatch().ifPresent(match-> {
            try {

                ((Event) event).setPlayerNickname(match.getPlayerNicknameFromHandler(clientHandler));
                match.validateEvent(event);
                match.transitionToNextState(event);
                SessionController.getInstance().saveMatch(match);
                SessionController.getInstance().saveSessionController();

            } catch (EventValidationFailedException e) {
                try {
                    clientHandler.sendAnswerMessage(new EventNotValid(this));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }
}
