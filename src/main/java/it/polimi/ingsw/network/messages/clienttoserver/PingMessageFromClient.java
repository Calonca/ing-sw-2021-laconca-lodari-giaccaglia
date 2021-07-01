package it.polimi.ingsw.network.messages.clienttoserver;

public class PingMessageFromClient extends ClientToServerMessage{


    String message;

    public PingMessageFromClient(String message) {
        this.message = message;
    }

    public String getPingMessage(){
        return message;
    }


}
