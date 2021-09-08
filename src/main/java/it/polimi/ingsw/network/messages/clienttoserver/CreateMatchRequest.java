package it.polimi.ingsw.network.messages.clienttoserver;


public class CreateMatchRequest extends ClientToServerMessage {

    protected final int maxPlayers;
    protected final String nickName;
    public CreateMatchRequest(int maxPlayers,String nickName){
        this.maxPlayers = maxPlayers;
        this.nickName   = nickName  ;
    }
}
