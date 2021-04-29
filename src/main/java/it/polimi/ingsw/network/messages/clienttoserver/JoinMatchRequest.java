package it.polimi.ingsw.network.messages.clienttoserver;


import java.util.UUID;

public class JoinMatchRequest extends ClientToServerMessage {
    protected UUID matchId;
    protected String nickName;

    public JoinMatchRequest(UUID matchId, String nickName){
        this.matchId = matchId;
        this.nickName   = nickName  ;
    }

}
