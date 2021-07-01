package it.polimi.ingsw.network.messages.servertoclient;

public class PingMessageFromServer extends ServerToClientMessage{

    String message;

    public PingMessageFromServer(String message) {
       this.message = message;
    }

    public String getPingMessage(){
        return message;
    }
}
