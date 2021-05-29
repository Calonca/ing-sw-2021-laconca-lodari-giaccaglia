package it.polimi.ingsw.server.messages.clienttoserver;

import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.servertoclient.EventNotValid;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.controller.EventValidationFailedException;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.io.IOException;
import java.util.List;

public class EventMessage extends ClientToServerMessage implements ServerMessage {

    protected Validable event;

    public Event getEvent() {
        return (Event) event;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) throws IOException {
        clientHandler.getMatch().ifPresent(m-> {
            try {
                m.validateEvent(event);
                Pair<State, List<Element>> stateAndElements = m.transitionToNextState(event);
                try {
                    clientHandler.sendAnswerMessage(stateAndElements.getKey().toStateMessage(m.getGame(), stateAndElements.getValue()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
