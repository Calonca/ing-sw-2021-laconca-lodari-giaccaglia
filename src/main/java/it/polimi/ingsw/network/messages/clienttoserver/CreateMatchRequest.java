package it.polimi.ingsw.network.messages.clienttoserver;


public class CreateMatchRequest extends ClientToServerMessage {

    protected int maxPlayers;
    protected String nickName;
    public CreateMatchRequest(int maxPlayers,String nickName){
        this.maxPlayers = maxPlayers;
        this.nickName   = nickName  ;
    }
}
