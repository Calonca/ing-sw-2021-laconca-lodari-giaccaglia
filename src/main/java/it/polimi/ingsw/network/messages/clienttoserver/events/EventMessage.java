package it.polimi.ingsw.network.messages.clienttoserver.events;

import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

public class EventMessage extends ClientToServerMessage {
    protected Event event;

    public EventMessage(Event event) {
        super();
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
