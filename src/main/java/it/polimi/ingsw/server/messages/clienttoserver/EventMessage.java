package it.polimi.ingsw.server.messages.clienttoserver;

import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.messages.servertoclient.EventNotValid;
import it.polimi.ingsw.network.messages.servertoclient.state.StateMessage;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.states.State;

import java.io.IOException;

public class EventMessage extends ClientToServerMessage implements ServerMessage {
    protected Validable event;

    public Validable getEvent() {
        return event;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) throws IOException {
        clientHandler.getMatch().ifPresent(m-> {
            try {
                m.validateEvent(event);
                State state = m.transitionToNextState(event);
         /*       try {
                    clientHandler.sendAnswerMessage(new StateMessage(this, state.toStateMessage(m.getGame())));
                } catch (IOException e) {
                    e.printStackTrace();
                } */ //TODO RESTORE COMMENTED OUT CODE
            } catch (EventValidationFailedException e) {
                try {
                    clientHandler.sendAnswerMessage(new EventNotValid(this));
                // todo restore commented code    clientHandler.sendAnswerMessage(new StateMessage(this, m.getGame().getCurrentPlayer().getCurrentState().toStateMessage(m.getGame())));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }
}
